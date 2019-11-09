package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.meta.components.Player1Component;
import com.framework.ecs.meta.components.Player2Component;
import com.framework.ecs.states.components.TargetComponent;
import com.framework.enums.PlayerType;

public class TargetSystem extends IteratingSystem {


    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public TargetSystem() {
        super(Family.all(TargetComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        getEntities().forEach(entity -> processEntity(entity, dt));
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        if (Mapper.passiveState.has(entity)) {
            PlayerType player = Mapper.owner_m.get(entity).getOwner();

            Family family = Family.all(player == PlayerType.P1 ? Player2Component.class : Player1Component.class).get();

            for (Entity e : getEngine().getEntitiesFor(family)) {
                if (!EcsUtils.isDead(e) && EcsUtils.targetInSight(entity, e)) {
                    // if (EcsUtils.targetInSight(entity, e)) {
                    EcsUtils.appendTarget(entity, e);
                }
            }
        }
    }
}
