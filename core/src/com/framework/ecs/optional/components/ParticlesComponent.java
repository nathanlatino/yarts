package com.framework.ecs.optional.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.core.Conf;

public class ParticlesComponent implements Component, Poolable {

    private ParticleEffect effect;
    private float offsetX;
    private float offsetY;

    @Override
    public void reset() {
        effect = null;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void start() {
        effect.start();
    }

    public void stop() {
        effect.allowCompletion();
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public ParticleEffect getEffect() {
        return effect;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public boolean isRunning() {
        return !effect.isComplete();
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setInfinite() {
        effect.getEmitters().forEach(i -> i.setContinuous(true));
    }

    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX / Conf.PPM;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY / Conf.PPM;
    }

    public void setScale(float scale) {
        this.effect.scaleEffect(scale * Conf.PTM);
    }
}