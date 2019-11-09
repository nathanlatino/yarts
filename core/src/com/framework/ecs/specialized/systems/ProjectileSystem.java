package com.framework.ecs.specialized.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.specialized.components.ProjectileComponent;
import com.yarts.entities.ProjectilesFactory;

public class ProjectileSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public ProjectileSystem() {
        super(Family.all(ProjectileComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        super.update(dt);
        GdxAI.getTimepiece().update(dt);

    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        EcsUtils.updateSteering(entity, dt);

        Mapper.projectile_m.get(entity).incrementTimer(dt);

        if (Mapper.body_m.has(Mapper.projectile_m.get(entity).getTarget())) {

            if (EcsUtils.distanceToDestination(entity) < 2f) {

                Mapper.projectile_m.get(entity).setContact(true);
            }

            if (EcsUtils.projectileHit(entity)) {
                ProjectilesFactory.getInstance().destroyProjectile(entity, getEngine(), dt);

            }

            else if (Mapper.projectile_m.get(entity).getTimer() > 1f) {
                ProjectilesFactory.getInstance().destroyProjectile(entity, getEngine(), dt);
            }

        }
        else {
            ProjectilesFactory.getInstance().destroyProjectile(entity, getEngine(), dt);

        }
    }

}


