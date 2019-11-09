package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.framework.core.factories.StateFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.states.components.EngagingComponent;
import com.framework.enums.EntityType;

public class EngagingSystem extends BaseStateSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public EngagingSystem() {
        super(Family.all(EngagingComponent.class).get());
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
        EcsUtils.updateSteering(entity, dt);


        if (!EcsUtils.hasTargets(entity)) {
            StateFactory.getInstance().setPassiveState(entity);
        }

        EntityType type = EcsUtils.getType(entity);

        if (type == EntityType.SOLDIER) {

            Entity target = EcsUtils.hasTargetInRange(entity);

            if (target != null) {
                StateFactory.getInstance().setEngagedState(entity, EcsUtils.getFirstTarget(entity));
            }

            else if (!EcsUtils.targetInSight(entity, EcsUtils.getFirstTarget(entity))){
                StateFactory.getInstance().setPassiveState(entity);
            }

        }
        if (type == EntityType.WORKER) {

            Entity target = EcsUtils.hasTargetInRange(entity);

            if (target != null) {
                if (EcsUtils.getType(target) == EntityType.BUILDING) {

                    Entity closest = EcsUtils.findClosestRessource(entity, getEngine());
                    EcsUtils.unloadCarry(entity);
                    EcsUtils.setTarget(entity, closest);
                    StateFactory.getInstance().setEngagingState(entity, closest);
                } else {
                    StateFactory.getInstance().setEngagedState(entity, EcsUtils.getFirstTarget(entity));
                }
            }

            else if (!EcsUtils.targetInSight(entity, EcsUtils.getFirstTarget(entity))){
                StateFactory.getInstance().setPassiveState(entity);
            }

        }
    }
}