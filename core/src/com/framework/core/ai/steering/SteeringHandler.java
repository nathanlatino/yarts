package com.framework.core.ai.steering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.framework.core.ai.pathfinding.Graph;
import com.framework.core.ai.pathfinding.PathFinderManager;
import com.framework.core.factories.SteeringFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.utils.Box2DLocation;
import com.framework.utils.Utils;


public class SteeringHandler implements Steerable<Vector2> {

    protected static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration(new Vector2());

    // Physics
    protected Body body;

    // Steering data
    public float maxLinearSpeed;
    protected float maxLinearAcceleration;
    protected float maxAngularSpeed;
    protected float maxAngularAcceleration;
    protected float zeroThreshold;

    protected float boundingRadius;
    protected boolean tagged;
    protected boolean independentFacing;

    protected SteeringBehavior<Vector2> steeringBehavior;

    // Tools
    private Vector2 destination;
    private Entity target;

    // Pathfinding
    private PathFinderManager pathFinder;
    private Graph debugPath;

    private SteeringFactory steeringFactory;

    private Entity entity;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public SteeringHandler(Entity entity) {
        this.steeringFactory = SteeringFactory.getInstance();
        this.body = EcsUtils.getBody(entity);

        if ( Mapper.velocity_m.has(entity)) {
            this.maxLinearSpeed = Mapper.velocity_m.get(entity).getMaxSpeed();
            this.maxLinearAcceleration = Mapper.velocity_m.get(entity).getAcceleration();
        } else {
            this.maxLinearSpeed = 2f;
            this.maxLinearAcceleration = 10f;
        }

        this.maxAngularSpeed = 0;
        this.maxAngularAcceleration = 0;
        this.zeroThreshold = 0.00001f;
        this.boundingRadius = 10f;

        this.tagged = false;
        this.independentFacing = false;

        this.destination = null;
        this.target = null;
    }

    /*------------------------------------------------------------------*\
    |*							    Mods
    \*------------------------------------------------------------------*/

    public void setArrive(Vector2 destination) {
        this.reset();
        this.destination = destination;
        this.steeringBehavior = steeringFactory.createArriveBehavior(this, destination);
    }

    public void setFollowPath(Vector2 destination) {
        pathFinder = PathFinderManager.getInstance();
        pathFinder.getDebugPaths().clear();
        Graph nodes = pathFinder.findPath(body.getPosition(), destination);
        SteeringBehavior path;

        try {
            path = steeringFactory.createFollowPathBehavior(this, nodes.getPath());
            this.reset();
            manageDebugPath(pathFinder, nodes);
            this.destination = nodes.getPath().get(nodes.getPath().size-1);
            this.steeringBehavior = path;
        } catch (Exception e) {
            System.out.println("invalid destination");
        }
    }

    public void setPoursue(Entity target) {
        this.reset();
        this.target = target;
        if (Mapper.steering_m.has(target)) {
            this.steeringBehavior = steeringFactory.createPoursueBehavior(this, EcsUtils.getSteeringHandler(target));
        } else {
            System.out.println("Target: " + EcsUtils.identify(target));
        }
    }

    public void setFlee(Entity target) {
        this.reset();
        this.target = target;
        this.steeringBehavior = steeringFactory.createFleeBehavior(this, EcsUtils.getSteeringHandler(target));
    }

    /*------------------------------------------------------------------*\
    |*							Tools
    \*------------------------------------------------------------------*/

    public void clearDebugPath() {
        pathFinder.getDebugPaths().removeValue(debugPath, false);
        debugPath.clear();
    }

    private void manageDebugPath(PathFinderManager pathFinder, Graph nodes) {
        if (this.pathFinder == null) {
            this.pathFinder = pathFinder;
        } else {

            clearDebugPath();
        }
        debugPath = new Graph();
        nodes.forEach(debugPath::add);
        pathFinder.addDebugPath(debugPath);
    }

    public float distanceToDestination() {
        if (target != null) {
            return Mapper.body_m.get(target).getBody().getPosition().dst(getPosition());
        } else {
            return destination.dst(getPosition());
        }
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    public void update(float dt) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, dt);
        }
    }

    protected void applySteering(SteeringAcceleration<Vector2> steering, float dt) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {

            // Internally scales the force by deltaTime
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                // Internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            // If velocity is null, then we can do anything
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * dt);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }

        if (anyAccelerations) {
            // Cap the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }
            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void reset() {
        destination = null;
        target = null;
        // steeringBehavior = null;
        debugPath = new Graph();
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    public Vector2 getDestination() {
        return destination;
    }

    public Entity getTarget() {
        return target;
    }

    /*------------------------------------------------------------------*\
    |*						    Steerable Interface
    \*------------------------------------------------------------------*/

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2DLocation();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return this.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return this.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }
}
