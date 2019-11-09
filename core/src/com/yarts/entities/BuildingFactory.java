package com.yarts.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.framework.core.factories.ComponentFactory;
import com.framework.core.factories.EntityFactory;
import com.framework.core.managers.PhysicsManager;
import com.framework.core.managers.PlayerManager;
import com.framework.core.managers.TexturesManager;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.BodyComponent;
import com.framework.ecs.core.components.BoundsComponent;
import com.framework.ecs.core.components.SteeringComponent;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.ecs.specialized.components.HealthComponent;
import com.framework.enums.PlayerType;
import com.yarts.ClassType;

public class BuildingFactory {

    private static BuildingFactory instance;

    private static int barracksCount = 0;
    private static int spawnCount = 0;

    private EntityFactory entityFactory;
    private ComponentFactory componentFactory;
    private TexturesManager texturesManager;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private BuildingFactory() {
        entityFactory = EntityFactory.getInstance();
        componentFactory = ComponentFactory.getInstance();
        texturesManager = TexturesManager.getInstance();
    }

    public static BuildingFactory getInstance() {
        if (instance == null) {
            instance = new BuildingFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Creation
    \*------------------------------*/


    public Entity createSpawn(float x, float y, PlayerType player) {
        Entity entity = entityFactory.createBuilding(x, y, 64, 64, 400, "textures/buildings/large_building.png", player);
        entity.add(componentFactory.createClassComponent(ClassType.SPAWN, ++spawnCount));
        Mapper.transform_m.get(entity).setScale(1.3f, 1.3f);
        Mapper.transform_m.get(entity).setOriginOffset(0, .7f);
        return entity;
    }

    public Entity createBarrack(float x, float y, PlayerType player) {
        int price = 100;
        if (PlayerManager.getInstance().getGold() >= price) {
            PlayerManager.getInstance().DecrementGold(price);
            Entity entity = entityFactory.createBuilding(x, y, 32, 32, 400, "textures/buildings/small_building.png", player);
            entity.add(componentFactory.createClassComponent(ClassType.BARRACK, ++barracksCount));
            Mapper.transform_m.get(entity).setScale(1.15f, 1.15f);
            Mapper.transform_m.get(entity).setOriginOffset(0, .3f);

            return entity;
        }
        System.out.println("Not enough money mate !");
        return null;
    }

    /*------------------------------*\
    |*			  Destruction
    \*------------------------------*/

    public void destroyBuilding(Entity entity, Engine engine) {
        PhysicsManager.getWorld().destroyBody(Mapper.body_m.get(entity).getBody());
        Mapper.steering_m.get(entity).reset();
        Mapper.body_m.get(entity).reset();
        Mapper.transform_m.get(entity).setZIndex(-1000);
        entity.remove(BodyComponent.class);
        entity.remove(SteeringComponent.class);
        entity.remove(HealthComponent.class);
        entity.remove(SelectionComponent.class);
        entity.remove(BoundsComponent.class);
        engine.removeEntity(entity);
    }
}
