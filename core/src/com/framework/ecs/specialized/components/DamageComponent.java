package com.framework.ecs.specialized.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Created by barry on 12/13/15 @ 5:54 PM.
 */
public class DamageComponent implements Component, Poolable{

    private float dps = 0f;


    @Override
    public void reset() {
        this.dps = 0f;
    }

    public float getDps() {
        return dps;
    }

    public void setDPS(float dps){
        this.dps = dps;
    }

}
