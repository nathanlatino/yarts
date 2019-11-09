package com.framework.ecs.optional.components;

import box2dLight.PointLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.core.Conf;

public class LightComponent implements Component, Poolable {

    private Body body = null;
    private Body handler = null;

    // private PointLight light;
    private PointLight light = null;
    public float distance = 0;

    // main positioning
    private float offsetX = 0;
    private float offsetY = 0;


    // dansing light
    private int cursor = 0;
    private int direction = 1;
    private float oscillation = .5f;

    private boolean dansing = false;


    @Override
    public void reset() {
        this.light = null;
        this.body = null;
        this.offsetX = 0;
        this.offsetY = 0;
        this.oscillation = .5f;
        this.cursor = 0;
        this.direction = 1;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Body getBody() {
        return body;
    }

    public Body getHandler() {
        return handler;
    }

    public PointLight getLight() {
        return light;
    }

    public float getX() {
        return handler.getPosition().x + offsetX;
    }

    public float getY() {
        return handler.getPosition().y + offsetY;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public int getCursor() {
        return cursor;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isDansing() {
        return dansing;
    }

    public float getDistance() {
        return light.getDistance();
    }

    public float getOscillation() {
        return oscillation;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setBody(Body body) {
        this.body = body;
    }

    public void setHandler(Body handler) {
        this.handler = handler;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX * Conf.PTM;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY * Conf.PTM;
    }

    public void setDansing(boolean dansing) {
        this.dansing = dansing;
        this.distance = getDistance();
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setDistance(float distance) {
        this.light.setDistance(distance);
    }

    public void setPosition(float x, float y) {
        this.body.setTransform(x, y, 0);
    }

    public void setSoftnessLength(float value) {
        this.light.setSoftnessLength(value);
    }

    public void setLight(PointLight light) {
        this.light = light;
    }
}
