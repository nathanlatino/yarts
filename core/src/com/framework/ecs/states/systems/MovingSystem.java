package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.framework.core.factories.StateFactory;
import com.framework.core.notifications.commandes.BuildingEvent;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.states.components.MovingComponent;
import com.framework.enums.EntityType;

public class MovingSystem extends BaseStateSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public MovingSystem() {
        super(Family.all(MovingComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {

        GdxAI.getTimepiece().update(dt);
        super.update(dt);

    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        super.processEntity(entity, dt);

        float distance = EcsUtils.distanceToDestination(entity);


        EcsUtils.updateSteering(entity, dt);

        if (distance < .2f) {
            Mapper.steering_m.get(entity).getSteeringHandler().setArrive(EcsUtils.getDestination(entity));
        }


        // TODO Bloody dirty mate !
        if (EcsUtils.getType(entity) == EntityType.WORKER) {

            boolean isBuilder = entity == BuildingEvent.getInstance().getEntity();

            if (distance <= .1f && !isBuilder) {
                StateFactory.getInstance().setPassiveState(entity);
            }

            // TODO The definition of quick and dirty
            if (isBuilder) {
                if (BuildingEvent.getInstance().isConfirmed() && EcsUtils.distanceToDestination(entity) <= .7f) {
                    BuildingEvent.getInstance().build();
                }
            }
        }
        else if (distance <= .1f || EcsUtils.getSpeed(entity) < .2) {
            StateFactory.getInstance().setPassiveState(entity);
        }
    }
}