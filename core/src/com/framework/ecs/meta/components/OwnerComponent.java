package com.framework.ecs.meta.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.framework.enums.PlayerType;


public class OwnerComponent implements Component, Poolable {

    private PlayerType owner;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
   	public void reset() {
        owner = null;
   	}

   	/*------------------------------*\
   	|*			  Getters
   	\*------------------------------*/

    public PlayerType getOwner() {
        return owner;
    }

   	/*------------------------------*\
   	|*			  Setters
   	\*------------------------------*/

    public void setOwner(PlayerType owner) {
        this.owner = owner;
    }
}
