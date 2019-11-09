package com.framework.ecs.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BoundsComponent implements Component, Poolable {

    private Rectangle bounds = new Rectangle();
    private Vector2 offset = new Vector2(0f, 0f);

    @Override
    public void reset() {
        this.bounds.set(0f, 0f, 0f, 0f);
        this.offset.set(0f, 0f);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return bounds.x;
    }

    public float getY() {
        return bounds.y;
    }

    public float getWidth() {
        return bounds.width;
    }

    public float getHeight() {
        return bounds.height;
    }

    public Vector2 getOffset() {
        return offset;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setBounds(Rectangle r){
        this.bounds = r;
    }

    public void setBounds(float x, float y, float width, float height){
        this.bounds.set(x, y, width, height);
    }

    public void setOffset(float x, float y){
        this.offset.set(x, y);
    }

}