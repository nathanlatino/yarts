package com.framework.ecs.states.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.ecs.EcsUtils;

public class TargetComponent implements Component, Poolable {

    private Array<Entity> targets = new Array<>();
    private float projectileCooldown = 1f;
    private float attackCooldown = 1f;

    @Override
    public void reset() {
        targets.clear();
        projectileCooldown = 0;
        attackCooldown = 0;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Entity getFirstTarget() {
        return targets.get(0);
    }

    public Array<Entity> getTargets() {
        return targets;
    }

    public boolean hasTargets() {
        return targets.size > 0;
    }

    public float getProjectileCooldown() {
        return projectileCooldown;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/


    public void setTarget(Entity target) {
        this.targets.insert(0, target);
    }

    public void appendTarget(Entity target) {
        this.targets.insert(targets.size, target);
    }

    public void removeTarget(Entity target) {
        targets.removeValue(target, false);
    }

    public void incrementProjectileCooldown(float dt) {
        projectileCooldown += dt;
    }
    public void resetProjectileCooldown() {
        projectileCooldown = 0;
    }

    public void incrementAttackCooldown(float dt) {
        attackCooldown += dt;
    }
    public void resetAttackCooldown() {
        attackCooldown = 0;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        targets.forEach(i -> sb.append(EcsUtils.identify(i)).append(", "));
        return sb.append(']').toString();
    }
}