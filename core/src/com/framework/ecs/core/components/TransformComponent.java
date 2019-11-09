package com.framework.ecs.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TransformComponent implements Component, Poolable {

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2(1.0f, 1.0f);
    private Vector2 originOffset = new Vector2(0f, 0f);
    private Color tint = Color.WHITE.cpy();
    private float rotation = 0.0f;
    private float zIndex = 1;
    private boolean isHidden = false;

    @Override
    public void reset() {
        this.position.set(0f, 0f);
        this.scale.set(1f, 1f);
        this.rotation = 0f;
        this.zIndex = 1;
        this.isHidden = false;
        this.tint.set(255f, 255f, 255f, 1f);
        this.originOffset.set(0f, 0f);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Vector2 getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public Vector2 getScale() {
        return scale;
    }

    public Vector2 getOriginOffset() {
        return originOffset;
    }

    public Color getTint() {
        return tint;
    }

    public float getRotation() {
        return rotation;
    }

    public float getZ() {
        return zIndex;
    }

    public boolean isHidden() {
        return isHidden;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setPosition(float x, float y){
        this.position.set(x, y);
    }

    public void setX(float x) {
        this.position.x = x;
    }

    public void setY(float y) {
        this.position.y = y;
    }

    public void setPosition(float x, float y, int z){
        this.position.set(x, y);
        this.zIndex = z;
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
    }

    public void setOpacity(float opacity){
        this.tint.set(this.tint.r, this.tint.g, this.tint.b, opacity);
    }

    public void setScale(float x, float y){
        this.scale.set(x, y);
    }

    public void setRotation(float rot){
        this.rotation = rot;
    }

    public void setTint(Color color){
        this.tint.set(color.r, color.g, color.b, color.a);
    }

    public void setTint(float r, float g, float b, float a){
        if(this.tint != null){
            this.tint.set(r, g, b, a);
        }
    }

    public void setOriginOffset(float x, float y){
        this.originOffset.set(x, y);
    }

    public void hide(){
        this.isHidden = false;
    }

    public void show(){
        this.isHidden = true;
    }

    public void setHidden(boolean isHidin){
        this.isHidden = isHidin;
    }

}