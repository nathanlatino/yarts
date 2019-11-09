package com.framework.ecs.states.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.framework.core.factories.StateFactory;
import com.framework.core.notifications.commandes.BuildingEvent;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.enums.EntityType;

public abstract class BaseStateSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public BaseStateSystem(Family family) {
        super(family);
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
        EntityType type = EcsUtils.getType(entity);

        if (EcsUtils.isDead(entity)) {
            StateFactory.getInstance().setDeadState(entity);
        }

        if (type == EntityType.RANGED) {
            Mapper.target_m.get(entity).incrementProjectileCooldown(dt);
        }
        else if (type == EntityType.SOLDIER || type == EntityType.WORKER) {
            Mapper.target_m.get(entity).incrementAttackCooldown(dt);
        }

        doSelectedActions(entity, dt);
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    protected boolean isValidDestination() {
        return true;
    }

    /*------------------------------------------------------------------*\
    |*							    Action
    \*------------------------------------------------------------------*/

    protected void doSelectedActions(Entity entity, float dt) {

        // if (EcsUtils.isSelected(entity)) {
        //
        //     float dist = EcsUtils.getUnprojected(entity).dst(MouseEvent.unprojectedMousePosition());
        //     System.out.println("dist = " + dist);
        // }

        if (Gdx.input.isButtonPressed(1)) {


            if (!EcsUtils.isSelected(entity) || !isValidDestination()) return;

            // TODO Bloody dirty mate !
            if (BuildingEvent.getInstance().getEntity() == entity && BuildingEvent.getInstance().isConfirmed()) {
                BuildingEvent.getInstance().reset();
            }

            Mapper.body_m.get(entity).getBody().setLinearDamping(0);
            Mapper.target_m.get(entity).getTargets().clear();

            Vector2 click = MouseEvent.unprojectedMousePosition();
            Entity target = EcsUtils.rightClickOnEntity(click, getEngine());

            if (target == null) {
                StateFactory.getInstance().setMovingState(entity, click);
            }
            else if (entity != target) {
                EcsUtils.setTarget(entity, target);
                StateFactory.getInstance().setPassiveState(entity);
            }
        }
    }
}