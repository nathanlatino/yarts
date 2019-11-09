package com.framework.ecs.meta.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.ecs.Mapper;
import com.framework.ecs.meta.components.BuildingComponent;
import com.framework.ecs.states.components.DeadComponent;

public class BuildingSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public BuildingSystem() {
        super(Family.all(BuildingComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if (Mapper.health_m.get(entity).getHealth() <= 0) {
            entity.add(new DeadComponent());
        }


    }
}