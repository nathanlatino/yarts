package com.framework.ecs.optional.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.framework.core.Conf;
import com.framework.core.game.map.Map;
import com.framework.ecs.Mapper;
import com.framework.ecs.optional.components.LightComponent;
import com.framework.ecs.optional.components.SunComponent;

public class SunSystem extends IteratingSystem {

    private final float centerX;
    private final float centerY;
    private final float radius = 75;
    private float time = 0;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public SunSystem(Map map) {
        super(Family.all(SunComponent.class).get());
        this.centerX = (map.getMapWidth() / 2) / Conf.PPM;
        this.centerY = (map.getMapWidth() / 2) / Conf.PPM;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float dt) {
        time += dt / 10;
        LightComponent lc = Mapper.light_m.get(entity);
        float x = MathUtils.cos(time) * radius;
        float y = MathUtils.sin(time) * radius;
        lc.getLight().getBody().setTransform(centerX + x,centerY + y, 0);
        lc.getHandler().setTransform(centerX + x, centerY + y, 0);
    }
}