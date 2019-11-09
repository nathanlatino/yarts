package com.yarts.entities;

import com.badlogic.ashley.core.Entity;
import com.framework.core.factories.ComponentFactory;
import com.framework.core.factories.EntityFactory;
import com.framework.core.managers.PhysicsManager;
import com.framework.core.managers.TexturesManager;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.BodyComponent;
import com.framework.ecs.core.components.BoundsComponent;
import com.framework.ecs.core.components.SteeringComponent;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.ecs.specialized.components.HealthComponent;
import com.framework.enums.PlayerType;
import com.yarts.ClassType;

public class UnitsFactory {

    private static UnitsFactory instance;

    private static int soldierCount = 0;
    private static int wizardCount = 0;
    private static int archerCount = 0;
    private static int workerCount = 0;

    private EntityFactory entityFactory;
    private ComponentFactory componentFactory;
    private TexturesManager texturesManager;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private UnitsFactory() {
        entityFactory = EntityFactory.getInstance();
        componentFactory = ComponentFactory.getInstance();
        texturesManager = TexturesManager.getInstance();
    }

    public static UnitsFactory getInstance() {
        if (instance == null) {
            instance = new UnitsFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Creation
    \*------------------------------*/

    public Entity createWarrior(float x, float y, PlayerType player) {
        Entity entity = entityFactory.createSoldier(x, y, 100, 10f, 2f, 5f, .5f, 6f, texturesManager.getWarrior(), player);
        entity.add(componentFactory.createClassComponent(ClassType.WARRIOR, ++soldierCount));
        return entity;
    }

    public Entity createWizard(float x, float y, PlayerType player) {
        Entity entity = entityFactory.createRanged(x, y, 100f, 10f, 2.5f, 5, 3f, 6f, texturesManager.getWizard(), player);
        entity.add(componentFactory.createClassComponent(ClassType.WIZARD, ++wizardCount));
        Mapper.transform_m.get(entity).setOriginOffset(0, 0);
        return entity;
    }

    public Entity createArcher(float x, float y, PlayerType player) {
        Entity entity = entityFactory.createRanged(x, y, 100f, 10f, 2f, 5f, 3f, 6f, texturesManager.getArcher(), player);
        entity.add(componentFactory.createClassComponent(ClassType.ARCHER, ++archerCount));
        Mapper.transform_m.get(entity).setOriginOffset(0, .1f);
        return entity;
    }

    public Entity createWorker(float x, float y, PlayerType player) {
        Entity entity = entityFactory.createWorker(x, y, 100f, 10f, 2f, 5f, 70f, texturesManager.getWorker(), player);
        entity.add(componentFactory.createClassComponent(ClassType.WORKER, ++workerCount));
        Mapper.transform_m.get(entity).setOriginOffset(0, .1f);
        return entity;
    }

    /*------------------------------*\
    |*			  Destruction
    \*------------------------------*/

    public void destroyUnit(Entity entity) {
        PhysicsManager.getWorld().destroyBody(Mapper.body_m.get(entity).getBody());
        Mapper.steering_m.get(entity).reset();
        Mapper.body_m.get(entity).reset();
        Mapper.transform_m.get(entity).setZIndex(-1000);
        entity.remove(BodyComponent.class);
        entity.remove(SteeringComponent.class);
        entity.remove(HealthComponent.class);
        entity.remove(SelectionComponent.class);
        entity.remove(BoundsComponent.class);
    }
}
