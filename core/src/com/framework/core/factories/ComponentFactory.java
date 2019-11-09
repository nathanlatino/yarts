package com.framework.core.factories;

import box2dLight.PointLight;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.framework.core.Conf;
import com.framework.core.ai.steering.SteeringHandler;
import com.framework.core.managers.PhysicsManager;
import com.framework.ecs.core.components.*;
import com.framework.ecs.meta.components.*;
import com.framework.ecs.mouse.components.MultiSelectionComponent;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.ecs.optional.components.LightComponent;
import com.framework.ecs.optional.components.ParticlesComponent;
import com.framework.ecs.rendering.components.AnimationComponent;
import com.framework.ecs.rendering.components.FogComponent;
import com.framework.ecs.rendering.components.OrientationComponent;
import com.framework.ecs.rendering.components.TextureComponent;
import com.framework.ecs.specialized.components.*;
import com.framework.ecs.states.components.*;
import com.framework.enums.EntityType;
import com.framework.enums.FilterType;
import com.framework.enums.PlayerType;
import com.framework.utils.AnimMap;
import com.yarts.ClassType;

import static com.framework.core.Conf.PPM;

public class ComponentFactory {
    private static ComponentFactory instance;

    private PooledEngine engine;


    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private ComponentFactory() {
    }

    public static ComponentFactory getInstance() {
        if (instance == null) {
            instance = new ComponentFactory();
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
    |*							Core
    \*------------------------------------------------------------------*/

    public BoundsComponent createBoundsComponent(float x, float y, float w, float h) {
        BoundsComponent boundsComponent = engine.createComponent(BoundsComponent.class);
        boundsComponent.setBounds(x / PPM, y / PPM, w / PPM, h / PPM);
        return boundsComponent;
    }

    public TransformComponent createTransformComponent(float x, float y) {
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.setPosition(x / PPM, y / PPM);
        transformComponent.setScale(Conf.SCALE.x, Conf.SCALE.y);

        return transformComponent;
    }

    public VelocityComponent createVelocityComponent(float maxSpeed, float acceleration) {
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        velocityComponent.setMaxSpeed(maxSpeed);
        velocityComponent.setAcceleration(acceleration);
        return velocityComponent;
    }

    public RangeComponent createRangeComponent(float minRange, float maxRange) {
        RangeComponent rangeComponent = engine.createComponent(RangeComponent.class);
        rangeComponent.setMinRange(minRange + .3f);
        rangeComponent.setMaxRange(maxRange + .3f);

        return rangeComponent;
    }

    public BodyComponent createBodyComponent(Body body) {
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.setBody(body);
        return bodyComponent;
    }

    /*------------------------------------------------------------------*\
    |*							Rendering
    \*------------------------------------------------------------------*/

    public TextureComponent createTextureComponent(TextureRegion region) {
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.setRegion(region);
        textureComponent.setWidth(region.getRegionWidth());
        textureComponent.setHeight(region.getRegionHeight());
        return textureComponent;
    }

    public TextureComponent createTextureComponent(Animation animation) {
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.setRegion((TextureRegion) animation.getKeyFrame(0));
        return textureComponent;
    }

    public TextureComponent createTextureComponent(TextureRegion region, float r) {
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.setRegion(region);
        textureComponent.setWidth(r);
        textureComponent.setHeight(r);
        return textureComponent;
    }

    public TextureComponent createTextureComponent(TextureRegion region, float w, float h) {
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.setRegion(region);
        textureComponent.setWidth(w);
        textureComponent.setHeight(h);
        return textureComponent;
    }

    public AnimationComponent createAnimationComponent(AnimMap animMap) {
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.setAnimMap(animMap);
        return animationComponent;
    }

    public OrientationComponent createOrientationComponent() {
        return engine.createComponent(OrientationComponent.class);
    }

    public FogComponent createFogComponent() {
        FogComponent fogComponent = engine.createComponent(FogComponent.class);
        return fogComponent;
    }

    /*------------------------------------------------------------------*\
    |*							Meta
    \*------------------------------------------------------------------*/

    public SelectionComponent createSelectionComponent() {
        return engine.createComponent(SelectionComponent.class);
    }

    public MultiSelectionComponent createMultiSelectionComponent() {
        return engine.createComponent(MultiSelectionComponent.class);
    }

    public TypeComponent createTypeComponent(EntityType type) {
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        typeComponent.setType(type);
        return typeComponent;
    }

    public OwnerComponent createOwnerComponent(PlayerType player) {
        OwnerComponent ownerComponent = engine.createComponent(OwnerComponent.class);
        ownerComponent.setOwner(player);
        return ownerComponent;
    }

    public Player1Component createPlayer1Component() {
        return engine.createComponent(Player1Component.class);
    }

    public Player2Component createPlayer2Component() {
        return engine.createComponent(Player2Component.class);
    }

    public NeutralComponent createNeutralComponent() {
        return engine.createComponent(NeutralComponent.class);
    }


    public BuildingComponent createBuildingComponent() {
        return engine.createComponent(BuildingComponent.class);
    }
    /*------------------------------------------------------------------*\
    |*							Specialized
    \*------------------------------------------------------------------*/

    public HealthComponent createHealthComponent(float max) {
        HealthComponent healthComponent = engine.createComponent(HealthComponent.class);
        healthComponent.setMaxHealth(max);
        healthComponent.setHealth(max);
        return healthComponent;
    }

    public DamageComponent createDamageComponent(float dps) {
        DamageComponent damageComponent = engine.createComponent(DamageComponent.class);
        damageComponent.setDPS(dps);
        return damageComponent;
    }

    public ConsomableComponent createConsomableComponent(float value) {
        ConsomableComponent consomableComponent = engine.createComponent(ConsomableComponent.class);
        consomableComponent.setMaxQuantity(value);
        consomableComponent.setQuantity(value);
        return consomableComponent;
    }

    public CarryComponent createCarryComponent(float value, float speed) {
        CarryComponent carryComponent = engine.createComponent(CarryComponent.class);
        carryComponent.setMaxQuantity(value);
        carryComponent.setQuantity(0);
        carryComponent.setSpeed(speed);
        return carryComponent;
    }

    public ProjectileComponent createProjectileComponent(Entity owner, Entity target) {
        ProjectileComponent spellComopnent = engine.createComponent(ProjectileComponent.class);
        spellComopnent.setOwner(owner);
        spellComopnent.setTarget(target);
        return spellComopnent;
    }

    /*------------------------------------------------------------------*\
    |*							    Optional
    \*------------------------------------------------------------------*/

    public ClassComponent createClassComponent(ClassType classType, int id) {
        ClassComponent classComponent = engine.createComponent(ClassComponent.class);
        classComponent.setClassType(classType);
        classComponent.setId(id);
        return classComponent;
    }

    public LightComponent createLightComponent(float offX, float offY, int rays, Color color, float distance, Body body) {
        LightComponent lightComponent = engine.createComponent(LightComponent.class);
        PointLight light = new PointLight(PhysicsManager.getInstance().getRayHandler(), rays, color, distance, offX, offY);
        Filter filter = new Filter();
        filter.groupIndex = 0;
        filter.categoryBits = FilterType.SENSOR.getBits();
        body.getFixtureList().get(0).setFilterData(filter);
        float x = body.getPosition().x + offX;
        float y = body.getPosition().y + offY;
        Body lightBody = BodyFactory.getInstance().createLightBody(x, y, .1f);
        light.attachToBody(lightBody);
        light.setContactFilter(FilterType.LIGHTS.getBits(), (short) 0, FilterType.getLightsMask());
        lightComponent.setLight(light);
        lightComponent.distance = distance;
        lightComponent.setBody(lightBody);
        lightComponent.setHandler(body);
        lightComponent.setOffsetX(offX);
        lightComponent.setOffsetY(offY);
        return lightComponent;
    }

    public ParticlesComponent createParticlesComponent(float offX, float offY, float scale, String pfx) {
        ParticlesComponent particlesComponent = engine.createComponent(ParticlesComponent.class);
        particlesComponent.setEffect(new ParticleEffect());
        particlesComponent.getEffect().load(Gdx.files.internal(pfx), Gdx.files.internal(""));
        particlesComponent.setOffsetX(offX);
        particlesComponent.setOffsetY(offY);
        particlesComponent.setScale(scale);
        particlesComponent.setInfinite();
        return particlesComponent;
    }


    /*------------------------------------------------------------------*\
    |*							    Dynamic
    \*------------------------------------------------------------------*/

    public SteeringComponent createSteeringComponent(Entity entity) {
        SteeringComponent steeringComponent = engine.createComponent(SteeringComponent.class);
        steeringComponent.setSteeringHandler(new SteeringHandler(entity));
        return steeringComponent;
    }

    /*------------------------------*\
    |*			  States
    \*------------------------------*/

    public TargetComponent createTargetComponent() {
        TargetComponent targetComponent = engine.createComponent(TargetComponent.class);
        return targetComponent;
    }

    public PassiveComponent createPassiveStateComponent() {
        return engine.createComponent(PassiveComponent.class);
    }

    public MovingComponent createMovingStateComponent() {
        return engine.createComponent(MovingComponent.class);
    }

    public EngagingComponent createEngagingStateComponent() {
        return engine.createComponent(EngagingComponent.class);
    }

    public EngagedComponent createEngagedStateComponent() {
        return engine.createComponent(EngagedComponent.class);
    }

    public DisengageComponent createDisengageStateComponent() {
        return engine.createComponent(DisengageComponent.class);
    }

    public DeadComponent createDeadStateComponent() {
        return engine.createComponent(DeadComponent.class);
    }

}
