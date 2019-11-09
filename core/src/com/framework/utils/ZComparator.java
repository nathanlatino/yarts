package com.framework.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.framework.ecs.core.components.TransformComponent;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {

    private ComponentMapper<TransformComponent> tc;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public ZComparator() {
        tc = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        float az = tc.get(entityA).getZ();
        float bz = tc.get(entityB).getZ();

        int res = 0;

        if (az > bz) res = 1;
        else if (az < bz) res = -1;

        return res;
    }

}
