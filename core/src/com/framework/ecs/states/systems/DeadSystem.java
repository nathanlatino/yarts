package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.states.components.DeadComponent;
import com.framework.enums.EntityType;
import com.yarts.entities.BuildingFactory;
import com.yarts.entities.UnitsFactory;

public class DeadSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public DeadSystem() {
        super(Family.all(DeadComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float dt) {
        DeadComponent dc = Mapper.deadState.get(entity);
        dc.incrementTimer(dt);

        if (dc.getTimer() > .5f) {
            Mapper.animation_m.get(entity).setLooping(false);
        }

        if (!Mapper.deadState.get(entity).isBeeingDestroyed()) {
            if (EcsUtils.getType(entity) == EntityType.BUILDING) {
                Mapper.deadState.get(entity).setBeeingDestroyed(true);
                BuildingFactory.getInstance().destroyBuilding(entity, getEngine());
            }
            else {
                UnitsFactory.getInstance().destroyUnit(entity);
                Mapper.deadState.get(entity).setBeeingDestroyed(true);
            }
        }
    }
}