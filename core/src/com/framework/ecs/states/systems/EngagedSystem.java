package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.framework.core.factories.StateFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.states.components.EngagedComponent;
import com.framework.enums.EntityType;
import com.yarts.entities.ProjectilesFactory;

public class EngagedSystem extends BaseStateSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public EngagedSystem() {
        super(Family.all(EngagedComponent.class).get());
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

        if (type == EntityType.WORKER) {

            Entity target = EcsUtils.getFirstTarget(entity);
            if (target != null) {
                if (!EcsUtils.targetIsConsumed(entity)) {

                    if (EcsUtils.targetInRange(entity, target)) {

                        Mapper.body_m.get(entity).getBody().setLinearDamping(1000);

                        EcsUtils.lookAt(entity, EcsUtils.getFirstTarget(entity));

                        if (Mapper.target_m.get(entity).getAttackCooldown() > .6f) {
                            Mapper.target_m.get(entity).resetAttackCooldown();

                            if (!EcsUtils.isFull(entity)) {
                                EcsUtils.consume(entity, target);
                            } else {
                                Entity closest = EcsUtils.findClosestSpawn(entity, getEngine());
                                if (closest != null) {
                                    Mapper.body_m.get(entity).getBody().setLinearDamping(0);
                                    EcsUtils.setTarget(entity, closest);
                                    StateFactory.getInstance().setEngagingState(entity, closest);
                                }
                            }
                        }
                    }
                    else if (Mapper.target_m.get(entity).getAttackCooldown() > .6f) {
                        Mapper.body_m.get(entity).getBody().setLinearDamping(0);
                        StateFactory.getInstance().setEngagingState(entity, target);
                    }
                }
            }
        }

        else {
            if (EcsUtils.hasTargetsInSight(entity)) {

                Entity target = EcsUtils.getFirstTarget(entity);
                if (target != null) {

                    if (EcsUtils.targetIsAlive(entity)) {

                        if (type == EntityType.RANGED) {

                            EcsUtils.lookAt(entity, target);

                            if (Mapper.target_m.get(entity).getProjectileCooldown() > 4.2f) {
                                ProjectilesFactory.getInstance().createProjectile(entity, target);
                            }

                            if (EcsUtils.targetInRange(entity, target)) {
                                StateFactory.getInstance().setDisengagingState(entity, target);
                            }
                        }


                        else if (type == EntityType.SOLDIER) {

                            if (EcsUtils.targetInRange(entity, target)) {
                                Mapper.body_m.get(entity).getBody().setLinearDamping(1000);
                                EcsUtils.lookAt(entity, EcsUtils.getFirstTarget(entity));
                                if (Mapper.target_m.get(entity).getAttackCooldown() > .6f) {
                                    Mapper.target_m.get(entity).resetAttackCooldown();
                                    EcsUtils.attack(entity, target);
                                }
                            }
                            else if (Mapper.target_m.get(entity).getAttackCooldown() > .6f) {
                                Mapper.body_m.get(entity).getBody().setLinearDamping(0);
                                StateFactory.getInstance().setEngagingState(entity, target);
                            }
                        }
                    }

                    else {
                        EcsUtils.removeTarget(entity, target);
                        StateFactory.getInstance().setPassiveState(entity);
                    }
                }
            }
            else {
                StateFactory.getInstance().setPassiveState(entity);
            }
        }
    }
}