package com.framework.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.framework.core.ai.steering.SteeringHandler;
import com.framework.core.managers.PlayerManager;
import com.framework.ecs.meta.components.BuildingComponent;
import com.framework.ecs.meta.components.Player1Component;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.ecs.specialized.components.ConsomableComponent;
import com.framework.enums.EntityType;
import com.framework.enums.PlayerType;
import com.framework.utils.Utils;
import com.yarts.ClassType;

public abstract class EcsUtils {

    /*------------------------------------------------------------------*\
    |*							Selection
    \*------------------------------------------------------------------*/

    public static Entity rightClickOnEntity(Vector2 position, Engine engine) {
        for (Entity entity : engine.getEntitiesFor(Family.all(SelectionComponent.class).get())) {
            if (Mapper.bounds_m.get(entity).getBounds().contains(position)) {
                return entity;
            }
        }
        return null;
    }

    public static void unselectAll(Engine engine, Entity except) {
        for (Entity entity : engine.getEntitiesFor(Family.all(SelectionComponent.class).get())) {
            if (entity != except) {
                entity.getComponent(SelectionComponent.class).setSelected(false);
            }
        }
    }

    public static void unselectAll(Engine engine) {
        unselectAll(engine, null);
    }
    /*------------------------------------------------------------------*\
    |*							    Actions
    \*------------------------------------------------------------------*/


    public static void updateSteering(Entity entity, float dt) {
        if (!Mapper.steering_m.has(entity)) return;
        Mapper.steering_m.get(entity).getSteeringHandler().update(dt);
    }


    public static void lookAt(Entity entity, Entity target) {
        Mapper.orientation_m.get(entity).setAngle(angleBetween(entity, target));
    }

    /*------------------------------------------------------------------*\
    |*							    Query
    \*------------------------------------------------------------------*/

    public static String identify(Entity entity) {
        return Mapper.class_m.has(entity) ? Mapper.class_m.get(entity).identify() : "Unknown";
    }

    public static boolean isSelected(Entity entity) {
        if (!Mapper.selection_m.has(entity)) return false;
        return Mapper.selection_m.get(entity).isSelected();
    }

    public static PlayerType getOwner(Entity entity) {
        return Mapper.owner_m.get(entity).getOwner();
    }

    public static EntityType getType(Entity entity) {
        return Mapper.type_m.get(entity).getType();
    }

    public static ClassType getClass(Entity entity) {
        return Mapper.class_m.get(entity).getClassType();
    }

    public static Vector2 getDestination(Entity entity) {
        return Mapper.steering_m.get(entity).getSteeringHandler().getDestination();
    }

    public static float distanceToDestination(Entity entity) {
        return Mapper.steering_m.get(entity).getSteeringHandler().distanceToDestination();
    }

    /*------------------------------*\
    |*			  Body
    \*------------------------------*/

    public static Body getBody(Entity entity) {
        if (!Mapper.body_m.has(entity)) return null;
        return Mapper.body_m.get(entity).getBody();
    }

    public static Vector2 getVelocity(Entity entity) {
        return getBody(entity).getLinearVelocity();
    }

    public static float getSpeed(Entity entity) {
        if (!Mapper.body_m.has(entity)) return 0;
        return getVelocity(entity).len();
    }

    public static Vector2 getPosition(Entity entity) {
        if (!Mapper.body_m.has(entity)) return null;
        return getBody(entity).getPosition();
    }

    /*------------------------------*\
    |*			  Orientation
    \*------------------------------*/

    public static float getAngle(Entity entity) {
        return Mapper.orientation_m.has(entity)
                ? Mapper.orientation_m.get(entity).getAngle()
                : getVelocity(entity).angle();
    }


    /*------------------------------*\
    |*			  Steering
    \*------------------------------*/

    public static SteeringHandler getSteeringHandler(Entity entity) {
        return Mapper.steering_m.get(entity).getSteeringHandler();
    }

    /*------------------------------*\
    |*			  Range
    \*------------------------------*/

    public static float getMinRange(Entity entity) {
        return Mapper.range_m.get(entity).getMinRange();
    }

    public static float getMaxRange(Entity entity) {
        return Mapper.range_m.get(entity).getMaxRange();
    }

    /*------------------------------*\
    |*			  Target
    \*------------------------------*/

    public static void setTarget(Entity entity, Entity target) {
        if (!Mapper.target_m.has(entity) || !Mapper.target_m.has(target)) return;
        removeTarget(entity, target);
        Mapper.target_m.get(entity).setTarget(target);
    }

    public static void appendTarget(Entity entity, Entity target) {
        if (!Mapper.target_m.has(entity) || !Mapper.target_m.has(target)) return;
        removeTarget(entity, target);
        Mapper.target_m.get(entity).appendTarget(target);
    }

    public static void removeTarget(Entity entity, Entity target) {
        if (!Mapper.target_m.has(entity)) return;
        try {
            Mapper.target_m.get(entity).removeTarget(target);
        } catch (Exception ignored) {
        }
    }

    public static Array<Entity> getTargets(Entity entity) {
        if (!Mapper.target_m.has(entity)) return null;
        return Mapper.target_m.get(entity).getTargets();
    }

    public static boolean hasTargets(Entity entity) {
        if (!Mapper.target_m.has(entity)) return false;
        return Mapper.target_m.get(entity).hasTargets();
    }

    public static boolean targetInSight(Entity entity, Entity target) {
        if (!Mapper.body_m.has(target)) return false;
        return distance(entity, target) <= getMaxRange(entity) + .2f;
    }

    public static boolean targetInRange(Entity entity, Entity target) {
        if (!Mapper.body_m.has(entity) || !Mapper.body_m.has(target)) return false;

        if (EcsUtils.getType(target) == EntityType.BUILDING) {
            return distance(entity, target) -1 <= getMinRange(entity);
        }
        return distance(entity, target) <= getMinRange(entity);
    }

    /**
     * Target in lign of sight (maxRange)
     */
    public static boolean hasTargetsInSight(Entity entity) {
        if (!Mapper.target_m.has(entity)) return false;
        if (hasTargets(entity)) {
            boolean flag = false;
            for (Entity target : getTargets(entity)) {
                if (targetInSight(entity, target)) {
                    flag = true;
                }
                else {
                    removeTarget(entity, target);
                }
            }
            return flag;
        }
        return false;
    }

    /**
     * Target in body contact (minRange)
     */
    public static Entity hasTargetInRange(Entity entity) {
        if (!Mapper.target_m.has(entity)) return null;
        if (hasTargets(entity)) {
            Entity target = getFirstTarget(entity);
            if (targetInRange(entity, target)) {
                return target;
            }
        }
        return null;
    }

    public static Entity getFirstTarget(Entity entity) {
        if (!Mapper.target_m.has(entity)) return null;
        try {
            return Mapper.target_m.get(entity).getFirstTarget();
        } catch (Exception e) {
            return null;
        }
    }

    public static String showTargets(Entity entity) {
        if (!Mapper.target_m.has(entity)) return null;
        return Mapper.target_m.get(entity).toString();
    }

    /*------------------------------*\
    |*			  Projectile
    \*------------------------------*/

    public static boolean projectileHit(Entity entity) {
        return Mapper.projectile_m.get(entity).isContact();
    }

    public static boolean projectileTooFar(Entity entity) {
        Entity owner = Mapper.projectile_m.get(entity).getOwner();
        return distance(owner, entity) > getMaxRange(owner) + 2f;
    }

    /*------------------------------*\
    |*			  Health
    \*------------------------------*/

    public static boolean isDead(Entity entity) {
        if (!Mapper.health_m.has(entity)) return true;
        return Mapper.health_m.get(entity).isDead();
    }

    public static void attack(Entity entity, Entity target) {
        decrementHealth(target, getDps(entity));
    }

    public static void decrementHealth(Entity target, float damage) {
        if (!Mapper.body_m.has(target)) return;
        Mapper.health_m.get(target).reduceHealth(damage);
    }

    public static void decrementHealth(Entity spell) {
        Entity owner = Mapper.projectile_m.get(spell).getOwner();
        Entity target = Mapper.projectile_m.get(spell).getTarget();
        EcsUtils.decrementHealth(target, Mapper.damage_m.get(owner).getDps());
    }


    public static boolean targetIsAlive(Entity entity) {
        if (EcsUtils.hasTargets(entity) && Mapper.health_m.has(EcsUtils.getFirstTarget(entity))) {
            return true;
        }
        return false;
    }

    /*------------------------------*\
    |*			  Damage
    \*------------------------------*/

    public static float getDps(Entity entity) {
        return Mapper.damage_m.get(entity).getDps();
    }

    /*------------------------------*\
    |*			  Consomable
    \*------------------------------*/

    public static void decrementQuantity(Entity target, float amount) {
        if (!Mapper.body_m.has(target)) return;
        Mapper.consomable_m.get(target).reduceQuantity(amount);
    }

    public static void consume(Entity entity, Entity target) {
        decrementQuantity(target, Mapper.carry_m.get(entity).getSpeed());
        incrementCarry(entity, Mapper.carry_m.get(entity).getSpeed());
    }

    public static boolean isFull(Entity entity) {
        return Mapper.carry_m.get(entity).isFull();
    }

    private static void incrementCarry(Entity entity, float value) {
        Mapper.carry_m.get(entity).incrementCarry(value);
    }

    public static void unloadCarry(Entity entity) {
        PlayerManager.getInstance().incrementGold(Mapper.carry_m.get(entity).getQuantity());
        Mapper.carry_m.get(entity).setQuantity(0);
    }

    public static boolean targetIsConsumed(Entity entity) {
        Entity target = getFirstTarget(entity);
        if (target != null && Mapper.consomable_m.has(target)) {
            return Mapper.consomable_m.get(getFirstTarget(entity)).isEmpty();
        }
        return true;
    }

    public static Entity findClosestSpawn(Entity entity, Engine engine) {
        float minDist = 100000;
        Entity closest = null;
        for (Entity e : engine.getEntitiesFor(Family.all(Player1Component.class, BuildingComponent.class).get())) {
            float dist = distance(entity, e);
            if (dist < minDist) {
                minDist = dist;
                closest = e;
            }
        }
        return closest;
    }

    public static Entity findClosestRessource(Entity entity, Engine engine) {
        float minDist = 100000;
        Entity closest = null;
        for (Entity e : engine.getEntitiesFor(Family.all(ConsomableComponent.class).get())) {
            float dist = distance(entity, e);
            if (dist < minDist) {
                minDist = dist;
                closest = e;
            }
        }
        return closest;
    }


    /*------------------------------------------------------------------*\
    |*							    Tools
    \*------------------------------------------------------------------*/

    public static float distance(Entity entityA, Entity entityB) {
        if (!Mapper.body_m.has(entityA) || !Mapper.body_m.has(entityB)) return 0;
        return getPosition(entityA).dst(getPosition(entityB));
    }

    public static Vector2 vectorToTarget(Entity entity, Vector2 point) {
        return Utils.vectorBetween(getPosition(entity), point);
    }

    public static Vector2 vectorToTarget(Entity entity, Entity target) {
        return vectorToTarget(entity, getPosition(target));
    }


    public static float angleBetween(Entity entity, Entity target) {
        return vectorToTarget(entity, getPosition(target)).angle();
    }

    public static float angleBetween(Entity entity, Vector2 point) {
        return vectorToTarget(entity, point).angle();

    }

}
