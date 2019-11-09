package com.framework.ecs.optional.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.optional.components.LightComponent;

public class LightSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public LightSystem() {
        super(Family.all(LightComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float dt) {
        LightComponent lc = Mapper.light_m.get(entity);

        float x = lc.getX();
        float y = lc.getY();

        if (lc.isDansing() && MathUtils.random(10) == 0) {
            float cursor = lc.getCursor() + lc.getDirection();

            if (MathUtils.random(1) == 0) {
                x += (cursor *  .005) ;
            } else {
                y += (cursor * .005);
            }
            if (cursor == 6) lc.setDirection(-1);
            if (cursor == -6) lc.setDirection(1);

        }
        if (lc.isDansing() && MathUtils.random(5) == 0) {
            float r = MathUtils.random(lc.distance, lc.getDistance() + lc.getOscillation());
            lc.getLight().setDistance(r);
        }

        lc.setPosition(x, y);
    }
}