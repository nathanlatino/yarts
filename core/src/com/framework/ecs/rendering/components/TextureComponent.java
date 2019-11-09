package com.framework.ecs.rendering.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;


public class TextureComponent implements Component, Poolable{

    private TextureRegion region = null;
    private float width = -1;
    private float height = -1;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void reset() {
        region = null;
        width = -1;
        height = -1;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public TextureRegion getRegion() {
        return region;
    }

    public float getWidth() {
        return width == -1 ? region.getRegionWidth() : width;
    }

    public float getHeight() {
        return height == -1 ? region.getRegionHeight() : height;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
