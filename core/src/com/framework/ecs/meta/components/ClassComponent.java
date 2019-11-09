package com.framework.ecs.meta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.yarts.ClassType;

public class ClassComponent implements Component, Poolable {

    private ClassType classType = null;
    private int id = 0;

    @Override
    public void reset() {
        classType = null;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public String identify() {
        return classType + "" + id;
    }

    public ClassType getClassType() {
        return classType;
    }

    public int getId() {
        return id;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }


    public void setId(int id) {
        this.id = id;
    }
}