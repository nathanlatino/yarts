package com.framework.ecs.specialized.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CarryComponent implements Component, Poolable {

    private float quantity = 0f;
    private float maxQuantity = 1f;
    private float speed = 1f;

    @Override
    public void reset() {
        this.maxQuantity = 1f;
        this.quantity = maxQuantity;
        this.speed = 1f;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public float getQuantity() {
        return quantity;
    }

    public float getMaxQuantity() {
        return maxQuantity;
    }

    public float getPercent() {
        return quantity / maxQuantity;
    }

    public boolean isFull() {
        return quantity >= maxQuantity;
    }

    public boolean isEmpty() {
        return quantity <= 0;
    }

    public float getSpeed() {
        return speed;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setMaxQuantity(float maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void incrementCarry(float value) {
        this.quantity += value;
    }
}