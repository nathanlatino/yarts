package com.framework.ecs.states.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class MovingComponent implements Component, Poolable {

    private boolean stuck = false;

    @Override
    public void reset() {
    }

}