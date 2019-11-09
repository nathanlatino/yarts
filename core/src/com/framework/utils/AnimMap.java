package com.framework.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.yarts.ClassType;

public class AnimMap {

    private ClassType classType;
    private IntMap<Animation> move;
    private IntMap<Animation> attack;
    private IntMap<Animation> death;
    private float width;
    private float height;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public AnimMap() {

    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public ClassType getClassType() {
        return classType;
    }

    public IntMap<Animation> getMove() {
        return move;
    }

    public IntMap<Animation> getAttack() {
        return attack;
    }

    public IntMap<Animation> getDeath() {
        return death;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public void setMove(IntMap<Animation> move) {
        this.move = move;
    }

    public void setAttack(IntMap<Animation> attack) {
        this.attack = attack;
    }

    public void setDeath(IntMap<Animation> death) {
        this.death = death;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
