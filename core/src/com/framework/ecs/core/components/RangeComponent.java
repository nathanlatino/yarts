package com.framework.ecs.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class RangeComponent implements Component, Poolable {

    private Vector2 range = new Vector2(0f, 0f);

    @Override
    public void reset() {
        range = new Vector2(0f, 0f);
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public float getMinRange() {
        return range.x;
    }

    public float getMaxRange() {
        return range.y;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setMinRange(float minRange) {
        this.range.x = minRange;
    }

    public void setMaxRange(float maxRange) {
        this.range.y = maxRange;
    }
}