package com.framework.core.factories;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import com.framework.core.managers.PhysicsManager;
import com.framework.enums.ColliderType;
import com.framework.enums.FilterType;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import static com.framework.core.Conf.PPM;

public class BodyFactory {

    private static BodyFactory instance;
    private World world;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private BodyFactory() {
    }

    public static BodyFactory getInstance() {
        if (instance == null) {
            instance = new BodyFactory();
        }
        return instance;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Body createBody(float x, float y, float w, float h, boolean fixedRot, BodyType bType, FilterType fType) {
        return createBoxBody(x, y, w, h, fixedRot, bType, fType);
    }

    public Body createBody(float x, float y, float r, boolean fixedRot, BodyType bType, FilterType fType) {
        return createCircleBody(x, y, r, fixedRot, bType, fType);
    }

    public Body createBody(float x, float y, float[] vertices, boolean fixedRot, BodyType bType, FilterType fType) {
        return createPolygonBody(x, y, vertices, fixedRot, bType, fType);
    }

    public Body createLightBody(float x, float y, float r) {
        CircleShape circleShape = createCircleShape(r);
        FixtureDef fdef = createSensorFixtureDefinition(circleShape);
        Body body = createBody(x / PPM, y / PPM, fdef, true, BodyType.StaticBody);
        circleShape.dispose();
        return body;
    }

    public FixtureDef createCircleSensor(float r) {
        CircleShape circleShape = createCircleShape(r);
        return createSensorFixtureDefinition(circleShape);
    }

    /*------------------------------------------------------------------*\
    |*							    Body
    \*------------------------------------------------------------------*/

    private Body createBoxBody(float x, float y, float w, float h, boolean fixedRot, BodyType bType, FilterType fType) {
        PolygonShape polygonShape = createRectangleShape(w / PPM, h / PPM);
        FixtureDef fDef = createFixtureDefinition(polygonShape, fType);
        Body body = createBody(x / PPM, y / PPM, fDef, true, bType);
        polygonShape.dispose();
        return body;
    }

    private Body createCircleBody(float x, float y, float r, boolean fixedRot, BodyType bType, FilterType fType) {
        CircleShape circleShape = createCircleShape(r / PPM);
        FixtureDef fDef = createFixtureDefinition(circleShape, fType);
        Body body = createBody(x / PPM, y / PPM, fDef, true, bType);
        circleShape.dispose();
        return body;
    }

    private Body createPolygonBody(float x, float y, float[] vertices, boolean fixedRot, BodyType bType, FilterType fType) {
        PolygonShape polygonShape = createPolygoneShape(vertices);
        FixtureDef fDef = createFixtureDefinition(polygonShape, fType);
        Body body = createBody(x / PPM, y / PPM, fDef, true, bType);
        polygonShape.dispose();
        return body;
    }

    private Body createBody(float x, float y, FixtureDef fDef, boolean fixedRot, BodyType bType) {
        BodyDef bodyDef = createBodyDefinition(x, y, fixedRot, bType);
        Body body = world.createBody(bodyDef);
        body.createFixture(fDef).setUserData(ColliderType.BODY);
        if (PhysicsManager.getInstance().isTopDown()) createFrictionJoint(body);
        return body;
    }

    /*------------------------------------------------------------------*\
    |*							    BodyDef
    \*------------------------------------------------------------------*/

    private BodyDef createBodyDefinition(float x, float y, boolean fixedRot, BodyType bType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bType;
        bodyDef.position.x = x;
        bodyDef.position.y = y;
        bodyDef.fixedRotation = fixedRot;
        return bodyDef;
    }

    /*------------------------------------------------------------------*\
    |*							Shapes
    \*------------------------------------------------------------------*/

    public PolygonShape createPolygoneShape(float[] vertices) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);
        return polygonShape;
    }

    private PolygonShape createRectangleShape(float w, float h) {
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(w / 2, h / 2);
        return polygonShape;
    }

    private CircleShape createCircleShape(float r) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(r / 2);
        return circleShape;
    }

    /*------------------------------------------------------------------*\
    |*							Fixtures
    \*------------------------------------------------------------------*/

    private FixtureDef createFixtureDefinition(Shape shape, FilterType filter) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = filter.getBits();
        fixtureDef.filter.groupIndex = 0;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        return fixtureDef;
    }

    private FixtureDef createSensorFixtureDefinition(Shape shape) {
        FixtureDef fixtureDef = createFixtureDefinition(shape, FilterType.SENSOR);
        fixtureDef.isSensor = true;
        fixtureDef.density = 0;
        return fixtureDef;
    }

    /*------------------------------------------------------------------*\
    |*							Friction joint
    \*------------------------------------------------------------------*/

    private void createFrictionJoint(Body body) {
        FrictionJointDef fJoin = new FrictionJointDef();
        fJoin.maxForce = 0;
        fJoin.maxTorque = 0;
        fJoin.initialize(PhysicsManager.getInstance().getFrictionerBox(), body, new Vector2(0, 0));
        world.createJoint(fJoin);
        body.setGravityScale(0);
    }

    /*------------------------------------------------------------------*\
    |*							Tools
    \*------------------------------------------------------------------*/

}
