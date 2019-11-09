package com.framework.ecs.gui.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.core.managers.PlayerManager;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.meta.components.Player1Component;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.enums.EntityType;

public class CountingSystem extends IteratingSystem {

    private int population;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public CountingSystem() {
        super(Family.all(Player1Component.class, SelectionComponent.class).get());
        population = 0;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        PlayerManager.getInstance().setPopulation(population);
        population = 0;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EntityType type = EcsUtils.getType(entity);
        if (type == EntityType.SOLDIER || type ==  EntityType.WORKER || type == EntityType.RANGED) {
            population++;
        }

    }
}