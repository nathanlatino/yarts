package com.framework.ecs.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;


public class BodyComponent implements Component, Poolable {

    private Body body;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void reset() {
        body = null;
    }

	/*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Body getBody() {
        return body;
    }

    public float getAngle() {
        return body.getLinearVelocity().angle();
    }


	/*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setBody(Body body) {
        this.body = body;
    }

}
