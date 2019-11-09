package com.framework.ecs.rendering.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.BodyComponent;
import com.framework.ecs.rendering.components.OrientationComponent;
import com.framework.enums.Orientations;

public class OrientationSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public OrientationSystem() {
        super(Family.all(OrientationComponent.class, BodyComponent.class).get());
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        OrientationComponent orientation = Mapper.orientation_m.get(entity);

        // if (steering.getCurrentMode() == SteeringState.FACE || body.getSpeed() > 1f) {


        // if (steering.getCurrentMode() == SteeringState.FACE) {
        //     angle = Mapper.orientation_m.get(entity).getAngle();
        // }

        if (EcsUtils.getSpeed(entity) > 1f) {
            orientation.setAngle(EcsUtils.getVelocity(entity).angle());
        }

        if (Orientations.NORTH.contains(orientation.getAngle())) {
            orientation.setOrientation(Orientations.NORTH);
        }

        else if (Orientations.WEST.contains(orientation.getAngle())) {
            orientation.setOrientation(Orientations.WEST);
        }

        else if (Orientations.SOUTH.contains(orientation.getAngle())) {
            orientation.setOrientation(Orientations.SOUTH);
        }

        else {
            orientation.setOrientation(Orientations.EAST);
        }
    }
}