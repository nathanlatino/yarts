package com.framework.ecs.specialized.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;


/**
 * Created by barry on 12/13/15 @ 5:48 PM.
 */
public class HealthComponent implements Component, Poolable {

    private float health = 1f;
    private float maxHealth = 1f;


    @Override
    public void reset() {
        this.maxHealth = 1f;
        this.health = maxHealth;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getPercent() {
        return health / maxHealth;
    }

    public boolean isFull() {
        return health == maxHealth;
    }

    public boolean isDead() {
        return health <= 0;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setMaxHealth(float maxHealth){
        this.maxHealth = maxHealth;
    }

    public void setHealth(float health){
        this.health = health;
    }

    public void reduceHealth(float amount) {
        this.health -= amount;
    }

}
