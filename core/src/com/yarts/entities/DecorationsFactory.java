package com.yarts.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.framework.core.Conf;
import com.framework.core.factories.ComponentFactory;
import com.framework.core.factories.EntityFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.optional.components.LightComponent;
import com.framework.ecs.optional.components.ParticlesComponent;
import com.framework.ecs.optional.components.SunComponent;


public class DecorationsFactory {

    private static DecorationsFactory instance;

    private EntityFactory entityFactory;
    private ComponentFactory componentFactory;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private DecorationsFactory() {
        entityFactory = EntityFactory.getInstance();
        componentFactory = ComponentFactory.getInstance();
    }

    public static DecorationsFactory getInstance() {
        if (instance == null) {
            instance = new DecorationsFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Entity createTorch(float x, float y) {
        Entity entity = entityFactory.createRoundDecoration(x, y, 40, 60, "textures/items/torch.png");
        Body body = EcsUtils.getBody(entity);
        Color color = new Color(0xffa500ff);
        LightComponent lc = componentFactory.createLightComponent(0, 10, 500, color, 14, body);
        ParticlesComponent pc = componentFactory.createParticlesComponent(2, 30, .13f, "particles/explosion.pfx");
        Mapper.transform_m.get(entity).setScale(.3f, .3f);
        lc.setOffsetY(11f);
        pc.setOffsetY(11f);
        pc.setOffsetX(-.07f);
        lc.setDansing(true);
        lc.setSoftnessLength(4);
        pc.start();
        entity.add(lc);
        entity.add(pc);
        return entity;
    }


    public Entity createPillard(float x, float y) {
        float width = 16;
        float height = 80;
        Entity entity = entityFactory.createSquareDecoration(x, y, width, height, "textures/items/pillard.png");
        Mapper.transform_m.get(entity).setOriginOffset(0, (2 * width) / Conf.PPM);
        Mapper.transform_m.get(entity).setScale(1f, .95f);

        return entity;

    }

    public Entity createSun(float x, float y) {
        Entity entity = entityFactory.createRoundDecoration(x, y, 200, 200, "textures/fx/fire_ball.png");
        Body body = EcsUtils.getBody(entity);
        Color color = new Color(Color.FIREBRICK);
        LightComponent lc = componentFactory.createLightComponent(0, 0, 256, color, 1500, body);
        ParticlesComponent pc = componentFactory.createParticlesComponent(0, 0, 1f, "particles/explosion.pfx");
        lc.getLight().setSoftnessLength(150);
        pc.start();

        entity.add(pc);
        entity.add(lc);
        entity.add(new SunComponent());
        return entity;
    }

}
