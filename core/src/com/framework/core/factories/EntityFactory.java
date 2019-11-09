package com.framework.core.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.framework.enums.EntityType;
import com.framework.enums.FilterType;
import com.framework.enums.PlayerType;
import com.framework.utils.AnimMap;

public class EntityFactory {
    private static EntityFactory instance;

    private final ComponentFactory componentFactory;
    private final BodyFactory bodyFactory;

    private PooledEngine engine;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private EntityFactory() {
        this.componentFactory = ComponentFactory.getInstance();
        this.bodyFactory = BodyFactory.getInstance();
    }

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Initialization
    \*------------------------------------------------------------------*/

    public void setEngine(PooledEngine engine) {
        this.engine = engine;
    }

    /*------------------------------------------------------------------*\
    |*							    CORE
    \*------------------------------------------------------------------*/

    private Entity createEntity(float x, float y, float w, float h) {
        Entity entity = engine.createEntity();
        entity.add(componentFactory.createBoundsComponent(x, y, w, h));
        return entity;
    }

    private Entity createEntity(float x, float y, float r) {
        Entity entity = engine.createEntity();
        return createEntity(x, y, r, r);
    }

    /*------------------------------------------------------------------*\
    |*							    SOLID
    \*------------------------------------------------------------------*/

    private Entity createSolid(float x, float y, float w, float h) {
        Entity entity = createEntity(x, y, w, h);
        return entity;
    }

    private Entity createSolid(float x, float y, float r) {
        Entity entity = createEntity(x, y, r);
        return entity;
    }

    public Entity createEmptyRectangle(float x, float y, float w, float h) {
        Entity entity = createSolid(x, y, w, h);
        Body body = bodyFactory.createBody(x, y, w, h, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTypeComponent(EntityType.EMPTY));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createShadow(float x, float y, float w, float h) {
        Entity entity = createSolid(x, y, w, h);
        Body body = bodyFactory.createBody(x, y, w, h, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTypeComponent(EntityType.EMPTY));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createEmptyPolygone(float x, float y, float[] vertices) {
        Entity entity = engine.createEntity();
        Body body = bodyFactory.createBody(x, y, vertices, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTypeComponent(EntityType.EMPTY));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createDecoration(float x, float y, String file) {
        TextureRegion region = createTextureRegion(file);
        Entity entity = createSolid(x, y, region.getRegionWidth());
        Body body = bodyFactory.createBody(x, y, region.getRegionWidth(), region.getRegionWidth(), true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createTextureComponent(region, region.getRegionWidth(), region.getRegionHeight()));
        entity.add(componentFactory.createTypeComponent(EntityType.DECORATION));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createRoundDecoration(float x, float y, float w, float h, String file) {
        TextureRegion region = createTextureRegion(file);
        Entity entity = createSolid(x, y, w / 4);
        Body body = bodyFactory.createBody(x, y, w / 4, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createTextureComponent(region, w, h));
        entity.add(componentFactory.createTypeComponent(EntityType.DECORATION));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createSquareDecoration(float x, float y, float w, float h, String file) {
        TextureRegion region = createTextureRegion(file);
        Entity entity = createSolid(x, y, w, w);
        Body body = bodyFactory.createBody(x, y, w, w, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createTextureComponent(region, w, h));
        entity.add(componentFactory.createTypeComponent(EntityType.DECORATION));

        engine.addEntity(entity);
        return entity;
    }

    /*------------------------------------------------------------------*\
    |*						      PROJECTILE
    \*------------------------------------------------------------------*/

    public Entity createProjectile(float x, float y, float maxSpeed, TextureRegion region) {
        Entity entity = createSolid(x, y, region.getRegionWidth(), region.getRegionHeight());
        Body body = bodyFactory.createBody(x, y, region.getRegionWidth() / 2, true, BodyType.DynamicBody, FilterType.ENTITY);
        body.setUserData(entity);

        Filter filter = new Filter();
        filter.groupIndex = 0;
        filter.categoryBits = FilterType.ENTITY.getBits();
        filter.maskBits = FilterType.getSolidMask();
        body.getFixtureList().get(0).setFilterData(filter);

        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createVelocityComponent(maxSpeed, 40f));
        entity.add(componentFactory.createTextureComponent(region));
        entity.add(componentFactory.createTypeComponent(EntityType.PROJECTILE));

        engine.addEntity(entity);
        return entity;
    }

    /*------------------------------------------------------------------*\
    |*							  SELECTABLE
    \*------------------------------------------------------------------*/

    private Entity createAnimatedSelectable(float x, float y, AnimMap animMap, PlayerType p) {
        Entity entity = createEntity(x, y, animMap.getWidth(), animMap.getHeight());
        Body body = bodyFactory.createBody(x, y, animMap.getWidth() - (animMap.getWidth() / 3), true, BodyType.DynamicBody, FilterType.ENTITY);

        body.setUserData(entity);
        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createOwnerComponent(p));
        entity.add(componentFactory.createSelectionComponent());
        entity.add(componentFactory.createTextureComponent(animMap.getMove().get(0)));
        entity.add(componentFactory.createAnimationComponent(animMap));
        entity.add(p == PlayerType.P1 ? componentFactory.createPlayer1Component() : componentFactory.createPlayer2Component());
        return entity;
    }

    private Entity createStaticSelectable(float x, float y, TextureRegion texture, PlayerType p) {
        Entity entity = createEntity(x, y, texture.getRegionWidth(), texture.getRegionHeight());
        Body body = bodyFactory.createBody(x, y, texture.getRegionWidth(), texture.getRegionHeight(), true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);

        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createOwnerComponent(p));
        entity.add(componentFactory.createSelectionComponent());
        entity.add(componentFactory.createTextureComponent(texture));
        if (p == PlayerType.NEUTRAL) {
            entity.add(componentFactory.createNeutralComponent());
        }
        else {
            entity.add(p == PlayerType.P1 ? componentFactory.createPlayer1Component() : componentFactory.createPlayer2Component());
        }
        return entity;
    }

    private Entity createStaticSelectable(float x, float y, float r, TextureRegion texture, PlayerType p) {
        Entity entity = createEntity(x, y, texture.getRegionWidth(), texture.getRegionHeight());
        Body body = bodyFactory.createBody(x, y, r, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);

        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createOwnerComponent(p));
        entity.add(componentFactory.createSelectionComponent());
        entity.add(componentFactory.createTextureComponent(texture));
        if (p == PlayerType.NEUTRAL) {
            entity.add(componentFactory.createNeutralComponent());
        }
        else {
            entity.add(p == PlayerType.P1 ? componentFactory.createPlayer1Component() : componentFactory.createPlayer2Component());
        }
        return entity;
    }

    private Entity createStaticSelectable(float x, float y, float w, float h, TextureRegion texture, PlayerType p) {
        Entity entity = createEntity(x, y, w, h);
        Body body = bodyFactory.createBody(x, y, w, h, true, BodyType.StaticBody, FilterType.SOLID);
        body.setUserData(entity);

        entity.add(componentFactory.createBodyComponent(body));
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createOwnerComponent(p));
        entity.add(componentFactory.createSelectionComponent());
        entity.add(componentFactory.createTextureComponent(texture));
        if (p == PlayerType.NEUTRAL) {
            entity.add(componentFactory.createNeutralComponent());
        }
        else {
            entity.add(p == PlayerType.P1 ? componentFactory.createPlayer1Component() : componentFactory.createPlayer2Component());
        }
        return entity;
    }

    /*------------------------------*\
    |*			  STATIC
    \*------------------------------*/

    private Entity createStatic(float x, float y, String file, PlayerType p) {
        TextureRegion region = createTextureRegion(file);
        return createStaticSelectable(x, y, region,  p);
    }

    public Entity createBuilding(float x, float y, float health, String file, PlayerType p) {
        Entity entity = createStatic(x, y, file, p);
        entity.add(componentFactory.createHealthComponent(health));
        entity.add(componentFactory.createTypeComponent(EntityType.BUILDING));
        engine.addEntity(entity);
        return entity;
    }

    public Entity createBuilding(float x, float y, float w, float h, float health, String file, PlayerType p) {
        TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal(file)));
        Entity entity = createStaticSelectable(x, y, w, h, region, p);
        entity.add(componentFactory.createHealthComponent(health));
        entity.add(componentFactory.createTypeComponent(EntityType.BUILDING));
        entity.add(componentFactory.createTargetComponent());
        entity.add(componentFactory.createBuildingComponent());
        entity.add(componentFactory.createSteeringComponent(entity));

        engine.addEntity(entity);
        return entity;
    }

    public Entity createRessource(float x, float y, float quantity, String file) {
        TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal(file)));

        Entity entity = createStaticSelectable(x, y, region.getRegionWidth() * (2/3), region, PlayerType.NEUTRAL);
        entity.add(componentFactory.createConsomableComponent(quantity));
        entity.add(componentFactory.createTypeComponent(EntityType.RESSOURCE));
        entity.add(componentFactory.createTargetComponent());
        entity.add(componentFactory.createSteeringComponent(entity));

        engine.addEntity(entity);
        return entity;
    }

    /*------------------------------*\
    |*			  MOBILE
    \*------------------------------*/

    private Entity createMobile(float x, float y, float maxSpeed, float acceleration, float health, AnimMap animMap, PlayerType p) {

        Entity entity = createAnimatedSelectable(x, y, animMap, p);
        entity.add(componentFactory.createTransformComponent(x, y));
        entity.add(componentFactory.createVelocityComponent(maxSpeed, acceleration));
        entity.add(componentFactory.createOrientationComponent());
        entity.add(componentFactory.createHealthComponent(health));
        entity.add(componentFactory.createFogComponent());
        entity.add(componentFactory.createMultiSelectionComponent());
        entity.add(componentFactory.createSteeringComponent(entity));
        StateFactory.getInstance().setPassiveState(entity);
        return entity;
    }

    private Entity createDamageDealer(float x, float y, float health, float dps, float maxSpeed, float acceleration,
                                      float minRange, float maxRange, AnimMap animMap, PlayerType p) {

        Entity entity = createMobile(x, y, maxSpeed, acceleration, health, animMap, p);
        entity.add(componentFactory.createDamageComponent(dps));
        entity.add(componentFactory.createRangeComponent(minRange, maxRange));
        entity.add(componentFactory.createTargetComponent());
        return entity;
    }

    public Entity createSoldier(float x, float y, float health, float dps, float maxSpeed, float acceleration,
                                float minRange, float maxRange, AnimMap animMap, PlayerType p) {

        Entity entity = createDamageDealer(x, y, health, dps, maxSpeed, acceleration, minRange, maxRange, animMap, p);
        entity.add(componentFactory.createTypeComponent(EntityType.SOLDIER));
        engine.addEntity(entity);
        return entity;
    }

    public Entity createRanged(float x, float y, float health, float dps, float maxSpeed, float acceleration,
                               float minRange, float maxRange, AnimMap animMap, PlayerType p) {

        Entity entity = createDamageDealer(x, y, health, dps, maxSpeed, acceleration, minRange, maxRange, animMap, p);
        entity.add(componentFactory.createTypeComponent(EntityType.RANGED));
        engine.addEntity(entity);
        return entity;
    }

    public Entity createWorker(float x, float y, float health, float gatherSpeed, float maxSpeed, float acceleration,
                               float carryValue, AnimMap animMap, PlayerType p) {

        Entity entity = createMobile(x, y, maxSpeed, acceleration, health, animMap, p);
        entity.add(componentFactory.createCarryComponent(carryValue, gatherSpeed));
        entity.add(componentFactory.createRangeComponent(.2f, 5f));
        entity.add(componentFactory.createTargetComponent());
        entity.add(componentFactory.createTypeComponent(EntityType.WORKER));

        engine.addEntity(entity);
        return entity;
    }

    /*------------------------------------------------------------------*\
    |*							    Tools
    \*------------------------------------------------------------------*/

    private Texture createTexture(String file) {
        Texture t = new Texture(Gdx.files.internal(file), true);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return t;
    }

    private TextureRegion createTextureRegion(String file) {
        return new TextureRegion(createTexture(file));
    }
}
