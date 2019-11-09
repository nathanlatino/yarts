package com.framework.ecs.specialized.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ProjectileComponent implements Component, Poolable {

    Entity owner = null;
    Entity target = null;
    float timer = 0;
    float fadeTimer = 0;
    boolean contact = false;
    Vector2 vectorToTarget = new Vector2();

    @Override
    public void reset() {
        owner = null;
        target = null;
        vectorToTarget = new Vector2();
        contact = false;
        timer = 0;
        fadeTimer = 0;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Entity getOwner() {
        return owner;
    }

    public Entity getTarget() {
        return target;
    }

    public Vector2 getVectorToTarget() {
        return vectorToTarget;
    }

    public float getTimer() {
        return timer;
    }

    public float getFadeTimer() {
        return fadeTimer;
    }

    public boolean isContact() {
        return contact;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public void setVectorToTarget(Vector2 vectorToTarget) {
        this.vectorToTarget = vectorToTarget;
    }

    public void incrementTimer(float dt) {
        this.timer += dt;
    }

    public void incrementFadeTimer(float dt) {
        this.fadeTimer += dt;
    }

    public void setContact(boolean contact) {
        this.contact = contact;
        timer = 0;
    }
}