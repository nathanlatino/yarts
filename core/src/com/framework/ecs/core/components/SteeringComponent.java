package com.framework.ecs.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.core.ai.steering.SteeringHandler;

public class SteeringComponent implements Component, Poolable {

    private SteeringHandler steeringHandler = null;

    @Override
    public void reset() {
        steeringHandler = null;
    }

    public SteeringHandler getSteeringHandler() {
        return steeringHandler;
    }

    public void setSteeringHandler(SteeringHandler steeringHandler) {
        this.steeringHandler = steeringHandler;
    }
}