package com.framework.ecs.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.ecs.core.components.BoundsComponent;
import com.framework.ecs.core.components.TransformComponent;
import com.framework.ecs.Mapper;

public class BoundsSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public BoundsSystem() {
        super(Family.all(BoundsComponent.class, TransformComponent.class).get());
    }

     /*------------------------------------------------------------------*\
     |*							Update Methods
     \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BoundsComponent bc = Mapper.bounds_m.get(entity);
        TransformComponent tc = Mapper.transform_m.get(entity);
        bc.getBounds().setPosition(tc.getX() - bc.getWidth() / 2, tc.getY() - bc.getWidth() / 2);
    }

     /*------------------------------------------------------------------*\
     |*							Private Methods
     \*------------------------------------------------------------------*/
}