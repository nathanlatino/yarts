package com.framework.ecs.meta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.enums.EntityType;


public class TypeComponent implements Component, Poolable {

    private EntityType type;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void reset() {
        type = null;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public EntityType getType() {
        return type;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setType(EntityType type) {
        this.type = type;
    }
}
