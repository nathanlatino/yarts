package com.framework.ecs.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.BodyComponent;
import com.framework.ecs.core.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1 / 60f;

    private World world;
    private Array<Entity> bodiesQueue;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());

        this.world = world;
        this.bodiesQueue = new Array();
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {

        super.update(dt);
        world.step(MAX_STEP_TIME, 6, 2);

        for (Entity entity : bodiesQueue) {
            TransformComponent tc = Mapper.transform_m.get(entity);

            Vector2 position = EcsUtils.getPosition(entity);
            EcsUtils.getBody(entity).setTransform(position.x, position.y, 0);


            tc.setX(position.x + tc.getOriginOffset().x);
            tc.setY(position.y + tc.getOriginOffset().y);

            tc.setZIndex(-EcsUtils.getPosition(entity).y);

        }

        bodiesQueue.clear();
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
