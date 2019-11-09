package com.framework.ecs.rendering.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.utils.AnimMap;

public class AnimationComponent implements Component, Poolable {

    private AnimMap animMap = null;
    private IntMap<Animation> animations = new IntMap<>();
    private float time = 0f;
    private boolean isLooping = true;

    @Override
    public void reset() {
        animMap = null;
        animations = new IntMap<>();
        time = 0f;
        isLooping = true;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void incrementTime(float dt) {
        this.time += dt;
    }

    public void resetTime() {
        this.time = 0;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public AnimMap getAnimMap() {
        return animMap;
    }

    public IntMap<Animation> getAnimations() {
        return animations;
    }

    public float getTime() {
        return time;
    }

    public boolean isLooping() {
        return isLooping;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setAnimMap(AnimMap animMap) {
        this.animMap = animMap;
    }

    public void setAnimations(IntMap<Animation> animations) {
        this.animations = animations;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }
}
