package com.framework.ecs.states.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DeadComponent implements Component, Poolable {

    private boolean beeingDestroyed = false;
    private float timer = 0;

    @Override
    public void reset() {
        beeingDestroyed = false;
        timer = 0;
    }

    public float getTimer() {
        return timer;
    }

    public void incrementTimer(float value) {
        this.timer += value;
    }

    public boolean isBeeingDestroyed() {
        return beeingDestroyed;
    }

    public void setBeeingDestroyed(boolean beeingDestroyed) {
        this.beeingDestroyed = beeingDestroyed;
    }
}