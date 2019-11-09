package com.framework.ecs.specialized.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.framework.core.Conf;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.BoundsComponent;
import com.framework.ecs.core.components.TransformComponent;
import com.framework.ecs.specialized.components.ConsomableComponent;
import com.framework.ecs.specialized.components.HealthComponent;
import com.framework.enums.EntityType;

public class QuantitySystem extends IteratingSystem {

    private OrthographicCamera camera;
    private final ShapeRenderer filledRenderer;
    private final ShapeRenderer lineRenderer;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public QuantitySystem(OrthographicCamera camera) {
        super(Family.one(HealthComponent.class, ConsomableComponent.class).get());
        this.camera = camera;
        filledRenderer = new ShapeRenderer();
        lineRenderer = new ShapeRenderer();
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float deltaTime) {
        if (!Conf.RENDER_HEALTH) return;
        super.update(deltaTime);

        Gdx.gl20.glLineWidth(1.5f);
        filledRenderer.setProjectionMatrix(camera.combined);
        filledRenderer.begin(ShapeRenderer.ShapeType.Filled);
        filledRenderer.setColor(Color.GREEN);


        for (Entity entity : getEntities()) {
            EntityType type = EcsUtils.getType(entity);
            BoundsComponent bc = Mapper.bounds_m.get(entity);
            TransformComponent tc = Mapper.transform_m.get(entity);

            float quantityPercent;

            if (type != EntityType.RESSOURCE) {
                quantityPercent = Mapper.health_m.get(entity).getPercent();
            }
            else {
                quantityPercent = Mapper.consomable_m.get(entity).getPercent();
            }
            float quantityWidth = bc.getWidth() * quantityPercent;

            if (type != EntityType.RESSOURCE) {

                if (quantityPercent < 1.0) {
                    if (quantityPercent < 0.75f && quantityPercent > 0.20f) {
                        filledRenderer.setColor(Color.YELLOW);
                    }
                    else if (quantityPercent < 0.20f) {
                        filledRenderer.setColor(Color.RED);
                    }
                    else {
                        filledRenderer.setColor(Color.GREEN);
                    }
                    float x = tc.getX() - bc.getWidth() / 2;
                    float y = tc.getY() + bc.getHeight() / 2;
                    float offset = .2f;
                    float h = 0.1f;

                    filledRenderer.rect(x, y + offset, quantityWidth, h);
                }
            }
        }

        filledRenderer.end();
        lineRenderer.setProjectionMatrix(camera.combined);
        lineRenderer.begin(ShapeRenderer.ShapeType.Line);
        lineRenderer.setColor(Color.BLACK);


        for (Entity entity : getEntities()) {
            EntityType type = EcsUtils.getType(entity);
            BoundsComponent bc = Mapper.bounds_m.get(entity);
            float quantityPercent;

            if (type != EntityType.RESSOURCE) {
                quantityPercent = Mapper.health_m.get(entity).getPercent();
            }
            else {
                quantityPercent = Mapper.consomable_m.get(entity).getPercent();
            }
            float quantityWidth = bc.getWidth();

            if (type != EntityType.RESSOURCE) {

                if (quantityPercent < 1.0) {
                    TransformComponent pc = Mapper.transform_m.get(entity);
                    float x = pc.getX() - bc.getWidth() / 2;
                    float y = pc.getY() + bc.getHeight() / 2;
                    float offset = .2f;
                    float h = 0.1f;

                    lineRenderer.rect(x, y + offset, quantityWidth, h);
                }
            }
        }
        lineRenderer.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}