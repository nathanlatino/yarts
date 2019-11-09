package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.framework.core.factories.StateFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.states.components.DisengageComponent;
import com.framework.enums.EntityType;

public class DisengagingSystem extends BaseStateSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public DisengagingSystem() {
        super(Family.all(DisengageComponent.class).get());
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

        EntityType type = EcsUtils.getType(entity);

        if (type == EntityType.RANGED) {

            if (EcsUtils.hasTargetsInSight(entity)) {
                Entity target = EcsUtils.getFirstTarget(entity);
                EcsUtils.lookAt(entity, target);

                if (EcsUtils.distance(entity, target) >= EcsUtils.getMaxRange(target) - 1) {
                    StateFactory.getInstance().setPassiveState(entity);
                }
            }
            else {
                StateFactory.getInstance().setPassiveState(entity);
            }
        }
        else if (type == EntityType.SOLDIER) {

        }
    }
}