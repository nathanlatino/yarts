package com.framework.core.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.framework.core.Conf;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.states.components.*;

public class StateFactory {

    private static StateFactory instance;
    private final ComponentFactory componentFactory;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private StateFactory() {
        componentFactory = ComponentFactory.getInstance();
    }

    public static StateFactory getInstance() {
        if (instance == null) {
            instance = new StateFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void setPassiveState(Entity entity) {
        clearState(entity);
        entity.add(componentFactory.createPassiveStateComponent());
        EcsUtils.getSteeringHandler(entity).setArrive(new Vector2(EcsUtils.getPosition(entity)));
        notifyChange(entity, "~PASSIVE~", null, null);
    }

    public void setMovingState(Entity entity, Vector2 destination) {
        clearState(entity);
        entity.add(componentFactory.createMovingStateComponent());
        if (Conf.PATHFINDING) {
            EcsUtils.getSteeringHandler(entity).setFollowPath(destination);
        } else {
            EcsUtils.getSteeringHandler(entity).setArrive(destination);
        }
        notifyChange(entity, "MOVING", null, destination);
    }

    public void setEngagingState(Entity entity, Entity target) {
        clearState(entity);
        entity.add(componentFactory.createEngagingStateComponent());
        EcsUtils.getSteeringHandler(entity).setPoursue(target);
        notifyChange(entity, "ENGAGING", target, null);
    }

    public void setEngagedState(Entity entity, Entity target) {
        clearState(entity);
        entity.add(componentFactory.createEngagedStateComponent());
        notifyChange(entity, "-ENGAGED-", target, null);
    }

    public void setDisengagingState(Entity entity, Entity target) {
        clearState(entity);
        entity.add(componentFactory.createDisengageStateComponent());
        EcsUtils.getSteeringHandler(entity).setFlee(target);
        notifyChange(entity, "DISENGAGE", target, null);
    }

    public void setDeadState(Entity entity) {
        clearState(entity);
        entity.add(componentFactory.createDeadStateComponent());
        notifyChange(entity, "DEAD", null, null);
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/


    private void clearState(Entity entity) {
        entity.remove(PassiveComponent.class);
        entity.remove(MovingComponent.class);
        entity.remove(EngagingComponent.class);
        entity.remove(EngagedComponent.class);
        entity.remove(DisengageComponent.class);
    }

    /*------------------------------------------------------------------*\
    |*							    Debug
    \*------------------------------------------------------------------*/

    private void notifyChange(Entity entity, String state, Entity target, Vector2 position) {
        if (!Conf.STATE_CHANGE_NOTIFICATIONS) return;
        StringBuilder sb = new StringBuilder("[STATE] ");
        sb.append(EcsUtils.getOwner(entity)).append(" ");

        sb.append(EcsUtils.identify(entity)).append(" ").append(state).append(" state ");
        if (position != null) {
            sb.append("towards ").append(position);
        }
        else if (target != null) {
            sb.append("Against ").append(EcsUtils.identify(target));
        }
        System.out.println(sb.toString());
    }
}
