package com.framework.ecs.optional.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.TransformComponent;
import com.framework.ecs.optional.components.ParticlesComponent;

public class ParticlesSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public ParticlesSystem() {
        super(Family.all(ParticlesComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        ParticlesComponent particles_c = Mapper.particles_m.get(entity);
        TransformComponent position_c = Mapper.transform_m.get(entity);

        float x = position_c.getX() + particles_c.getOffsetX();
        float y = position_c.getY() + particles_c.getOffsetY();

        particles_c.getEffect().setPosition(x, y);
        particles_c.getEffect().update(dt);
    }
}