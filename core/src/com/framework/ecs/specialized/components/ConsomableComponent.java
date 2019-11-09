package com.framework.ecs.specialized.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ConsomableComponent implements Component, Poolable {

    private float quantity = 1f;
    private float maxQuantity = 1f;


    @Override
    public void reset() {
        this.maxQuantity = 1f;
        this.quantity = maxQuantity;
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
        return quantity == maxQuantity;
    }

    public boolean isEmpty() {
        return quantity <= 0;
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


    public void reduceQuantity(float amount) {
        this.quantity -= amount;
    }
}