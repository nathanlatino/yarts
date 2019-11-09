package com.yarts.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.framework.core.factories.ComponentFactory;
import com.framework.core.factories.EntityFactory;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.optional.components.LightComponent;
import com.yarts.ClassType;

public class NeutralFactory {

    private static NeutralFactory instance;

    private static int mineralCount = 0;


    private EntityFactory entityFactory;
    private ComponentFactory componentFactory;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private NeutralFactory() {
        entityFactory = EntityFactory.getInstance();
        componentFactory = ComponentFactory.getInstance();
    }

    public static NeutralFactory getInstance() {
        if (instance == null) {
            instance = new NeutralFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Entity createMineral(float x, float y, float quantity) {
        Entity entity = entityFactory.createRessource(x, y, quantity, "textures/items/mineral.png");
        entity.add(componentFactory.createClassComponent(ClassType.MINERAL, ++mineralCount));
        Mapper.transform_m.get(entity).setScale(1.5f, 1.5f);
        Body body = EcsUtils.getBody(entity);
        Color color = new Color(Color.BLUE);
        LightComponent lc = componentFactory.createLightComponent(0, 0, 8, color, 1, body);
        lc.getLight().setXray(true);
        entity.add(lc);
        return entity;
    }
}
