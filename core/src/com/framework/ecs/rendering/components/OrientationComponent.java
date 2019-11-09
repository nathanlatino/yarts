package com.framework.ecs.rendering.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.enums.Orientations;

public class OrientationComponent implements Component, Poolable {

    private Orientations orientation = Orientations.SOUTH;
    private float angle = 270;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void reset() {
        orientation = Orientations.SOUTH;
        angle = 270;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Orientations getOrientation() {
        return orientation;
    }

    public boolean isSetTo(Orientations orientation) {
        return this.orientation == orientation;
    }

    public float getAngle() {
        return angle;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setOrientation(Orientations orientation) {
        this.orientation = orientation;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
