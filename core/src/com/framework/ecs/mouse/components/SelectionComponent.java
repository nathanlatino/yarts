package com.framework.ecs.mouse.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SelectionComponent implements Component, Poolable {

    private boolean selected = false;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void reset() {
        selected = false;
    }

   	/*------------------------------*\
   	|*			  Getters
   	\*------------------------------*/

    public boolean isSelected() {
        return selected;
    }

    /*------------------------------*\
   	|*			  Setters
   	\*------------------------------*/

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
