package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.framework.core.factories.StateFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.states.components.PassiveComponent;
import com.framework.enums.EntityType;

public class PassiveSystem extends BaseStateSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public PassiveSystem() {
        super(Family.all(PassiveComponent.class).get());
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

        EcsUtils.updateSteering(entity, dt);
        Mapper.body_m.get(entity).getBody().setLinearDamping(0);
        super.processEntity(entity, dt);


        EntityType type = EcsUtils.getType(entity);

        if (type != EntityType.WORKER) {

            if (EcsUtils.hasTargetsInSight(entity) && EcsUtils.targetIsAlive(entity)) {

                if (type == EntityType.RANGED) {
                    StateFactory.getInstance().setEngagedState(entity, EcsUtils.getFirstTarget(entity));
                }

                else if (type == EntityType.SOLDIER) {
                    StateFactory.getInstance().setEngagingState(entity, EcsUtils.getFirstTarget(entity));
                }
            }
        }
        else {
            if (!EcsUtils.targetIsConsumed(entity)) {
                StateFactory.getInstance().setEngagingState(entity, EcsUtils.getFirstTarget(entity));
            }
        }

    }
}