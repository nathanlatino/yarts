package com.framework.core.managers;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.framework.core.Conf;

public class PhysicsManager {

    private static PhysicsManager instance;

    // World
    private World world;

    // Light
    private RayHandler rayHandler;

    // TopDown worlds;
    private boolean topDown;
    private Body frictionerBox;

    // Refs
    private OrthographicCamera camera;

    /*------------------------------------------------------------------*\
   	|*							Initialization
   	\*------------------------------------------------------------------*/

    private PhysicsManager() {
        this.world = new World(new Vector2(Conf.GRAVITY_X, Conf.GRAVITY_Y), true);
        this.rayHandler = new RayHandler(world);
        this.rayHandler.setAmbientLight(Conf.AMBIENT_COLOR);
        this.rayHandler.setBlurNum(2);

        RayHandler.useDiffuseLight(true);
        RayHandler.setGammaCorrection(true);

        this.topDown = false;
        this.frictionerBox = null;
    }

    public static PhysicsManager getInstance() {
        if (instance == null) {
            instance = new PhysicsManager();
        }
        return instance;
    }

    public static World getWorld() {
        if (instance == null) {
            instance = new PhysicsManager();
        }
        return instance.world;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    public void updateLights() {
        rayHandler.setCombinedMatrix(
                camera.combined,
                camera.position.x,
                camera.position.y,
                camera.viewportWidth,
                camera.viewportHeight
        );
        rayHandler.updateAndRender();

    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods
   	\*------------------------------------------------------------------*/

    public void setTopdownWorld(float centerX, float centerY, float density, float friction) {
        topDown = true;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centerX, centerY);

        frictionerBox = world.createBody(bodyDef);
        PolygonShape worldShape = new PolygonShape();
        worldShape.setAsBox(centerX, centerY);

        FixtureDef fd = new FixtureDef();
        fd.shape = worldShape;
        fd.density = density;
        fd.friction = friction;
        frictionerBox.createFixture(fd);
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Body getFrictionerBox() {
        return frictionerBox;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public boolean isTopDown() {
        return topDown;
    }
}
