package com.framework.ecs.rendering.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.rendering.components.AnimationComponent;
import com.framework.ecs.rendering.components.OrientationComponent;
import com.framework.ecs.rendering.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    @SuppressWarnings("unchecked")
    public AnimationSystem() {
        super(Family.all(TextureComponent.class, AnimationComponent.class, OrientationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float dt) {

        AnimationComponent ac = Mapper.animation_m.get(entity);
        OrientationComponent oc = Mapper.orientation_m.get(entity);


        if (Mapper.passiveState.has(entity)) {
            ac.setAnimations(ac.getAnimMap().getMove());
            if (EcsUtils.getSpeed(entity) > .1f) {
                ac.incrementTime(dt);
            }
        }

        else if (Mapper.movingState.has(entity)) {
            ac.setAnimations(ac.getAnimMap().getMove());
            ac.incrementTime(dt);
        }

        else if (Mapper.engagingState.has(entity)) {
            ac.setAnimations(ac.getAnimMap().getMove());
            ac.incrementTime(dt);
        }

        else if (Mapper.engagedState.has(entity)) {
            ac.setAnimations(ac.getAnimMap().getAttack());
            ac.incrementTime(dt);
        }

        else if (Mapper.disengageState.has(entity)) {
            ac.setAnimations(ac.getAnimMap().getMove());
            ac.incrementTime(dt);
        }

        else if (Mapper.deadState.has(entity)) {
            ac.setAnimations(ac.getAnimMap().getDeath());
            ac.incrementTime(dt);
        }


        if (ac.getAnimations().containsKey(oc.getOrientation().getIndex())) {

            TextureComponent tc = Mapper.texture_m.get(entity);
            IntMap<Animation> animations = ac.getAnimations();
            int index = oc.getOrientation().getIndex();
            tc.setRegion((TextureRegion) animations.get(index).getKeyFrame(ac.getTime(), ac.isLooping()));
        }
    }
}
