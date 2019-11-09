package com.framework.ecs.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class VelocityComponent implements Component, Poolable {

    private float maxSpeed = 0f;
    private float acceleration = 40f;

    @Override
    public void reset() {
        maxSpeed = 0f;
        acceleration = 40f;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
}