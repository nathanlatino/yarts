package com.yarts.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.framework.core.Conf;
import com.framework.core.factories.ComponentFactory;
import com.framework.core.factories.EntityFactory;
import com.framework.core.managers.PhysicsManager;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.SteeringComponent;
import com.framework.ecs.core.components.TransformComponent;
import com.framework.ecs.optional.components.LightComponent;
import com.framework.ecs.optional.components.ParticlesComponent;
import com.framework.ecs.specialized.components.ProjectileComponent;
import com.framework.enums.FilterType;
import com.yarts.ClassType;


public class ProjectilesFactory {

    private static int arrowCount = 0;
    private static int spellCount = 0;

    private static ProjectilesFactory instance;

    private EntityFactory entityFactory;
    private ComponentFactory componentFactory;
    private TextureRegion fireBall;
    private TextureRegion arrow;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private ProjectilesFactory() {
        entityFactory = EntityFactory.getInstance();
        componentFactory = ComponentFactory.getInstance();
        fireBall = new TextureRegion(new Texture(Gdx.files.internal("textures/fx/fire_ball.png")));
        arrow = new TextureRegion(new Texture(Gdx.files.internal("textures/items/arrow.png")));
    }

    public static ProjectilesFactory getInstance() {
        if (instance == null) {
            instance = new ProjectilesFactory();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void createProjectile(Entity owner, Entity target) {
        Mapper.target_m.get(owner).resetProjectileCooldown();
        ClassType classType = EcsUtils.getClass(owner);

        switch (classType) {
            case WIZARD:
                createSpell(owner, target);
                break;
            case ARCHER:
                createArrow(owner, target);
                componentFactory.createClassComponent(ClassType.ARROW, arrowCount);
                break;
        }
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Creation
    \*------------------------------*/

    private Entity createSpell(Entity owner, Entity target) {
        Entity entity = initializeProjectile(owner, target, fireBall);
        entity.add(componentFactory.createClassComponent(ClassType.SPELL, spellCount));

        Color color = Color.RED;
        Body body = EcsUtils.getBody(entity);
        setWeight(body, 1f);

        Mapper.transform_m.get(entity).setScale(1f, 1f);
        LightComponent lc = componentFactory.createLightComponent(0, 0, 64, color, 5, body);
        ParticlesComponent pc = componentFactory.createParticlesComponent(0, 0, .2f, "particles/explosion.pfx");
        ProjectileComponent ppc = componentFactory.createProjectileComponent(owner, target);
        SteeringComponent sc = componentFactory.createSteeringComponent(entity);

        Filter filter = createFilters(body);
        lc.getLight().getBody().getFixtureList().get(0).setFilterData(filter);
        sc.getSteeringHandler().setPoursue(target);
        pc.start();
        entity.add(lc);
        entity.add(pc);
        entity.add(ppc);
        entity.add(sc);
        return entity;
    }

    private Entity createArrow(Entity owner, Entity target) {
        Entity entity = initializeProjectile(owner, target, arrow);
        entity.add(componentFactory.createClassComponent(ClassType.ARROW, arrowCount));

        Body body = EcsUtils.getBody(entity);
        setWeight(body, .1f);
        createFilters(body);

        Mapper.transform_m.get(entity).setScale(.5f, .5f);
        ProjectileComponent pc = componentFactory.createProjectileComponent(owner, target);
        SteeringComponent sc = componentFactory.createSteeringComponent(entity);
        sc.getSteeringHandler().setMaxLinearSpeed(10f);
        sc.getSteeringHandler().setMaxLinearAcceleration(2f);

        sc.getSteeringHandler().setPoursue(target);
        entity.add(sc);
        entity.add(pc);
        return entity;
    }

    /*------------------------------*\
    |*			  Destruction
    \*------------------------------*/

    public void destroyProjectile(Entity projectile, Engine engine, float dt) {
        ClassType classType = EcsUtils.getClass(projectile);
        if (Mapper.projectile_m.get(projectile).getFadeTimer() < .4f) {
            if (classType == ClassType.SPELL) spellExplosionEffect(projectile, dt);
            if (classType == ClassType.ARROW) projectileFadeEffect(projectile, dt);
        }
        else {
            EcsUtils.decrementHealth(projectile);
            clearProjectile(projectile, engine);
        }
    }

    /*------------------------------*\
    |*			  Tools
    \*------------------------------*/

    private void setWeight(Body body, float value) {
        body.getFixtureList().get(0).setDensity(value);
        body.resetMassData();
    }

    private Entity initializeProjectile(Entity owner, Entity target, TextureRegion region) {
        Vector2 position = EcsUtils.getPosition(owner);
        Vector2 vTarget = EcsUtils.vectorToTarget(owner, target);
        vTarget.setLength(30f);
        float x = (position.x * Conf.PPM) + vTarget.x;
        float y = (position.y * Conf.PPM) + vTarget.y;
        return entityFactory.createProjectile(x, y, 4f, region);
    }

    private Filter createFilters(Body body) {
        Filter filter = new Filter();
        filter.groupIndex = 0;
        filter.categoryBits = FilterType.ENTITY.getBits();
        filter.maskBits = FilterType.getSolidMask();
        body.getFixtureList().get(0).setFilterData(filter);
        return filter;
    }

    private void clearProjectile(Entity projectile, Engine engine) {

        ClassType classType = EcsUtils.getClass(projectile);
        PhysicsManager.getWorld().destroyBody(Mapper.body_m.get(projectile).getBody());

        switch (classType) {
            case SPELL:
                PhysicsManager.getWorld().destroyBody(Mapper.light_m.get(projectile).getBody());
                Mapper.light_m.get(projectile).getLight().remove();
                Mapper.light_m.get(projectile).reset();
                Mapper.particles_m.get(projectile).reset();

                break;
            case ARROW:
                break;
        }
        Mapper.steering_m.get(projectile).reset();
        Mapper.body_m.get(projectile).reset();
        engine.removeEntity(projectile);
    }

    private void projectileFadeEffect(Entity projectile, float dt) {
        TransformComponent tc = Mapper.transform_m.get(projectile);
        Color opacity = tc.getTint();
        opacity.set(opacity.r, opacity.g, opacity.b, opacity.a - .2f);
        Mapper.projectile_m.get(projectile).incrementFadeTimer(dt);
    }


    private void spellExplosionEffect(Entity spell, float dt) {
        TransformComponent tc = Mapper.transform_m.get(spell);
        LightComponent lc = Mapper.light_m.get(spell);
        Vector2 scale = tc.getScale();
        float distance = lc.getLight().getDistance();
        tc.setScale(scale.x + .05f, scale.y + .05f);
        tc.setOpacity(.2f);
        lc.getLight().setDistance(distance + .4f);
        Mapper.projectile_m.get(spell).incrementFadeTimer(dt);
    }


}
