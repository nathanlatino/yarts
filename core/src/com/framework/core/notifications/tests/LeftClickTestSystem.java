package com.framework.core.notifications.tests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;

public class LeftClickTestSystem extends IteratingSystem implements MouseEventSubscriber {

    private MouseEvent leftClickEvent;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public LeftClickTestSystem() {
        super(Family.all().get());
        leftClickEvent = null;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        if (leftClickEvent == null) return;

        getEntities().forEach(entity -> processEntity(entity, dt));

        leftClickEvent = null;
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        // if (leftClickEvent != null) System.out.println("LEFT_CLICK: " + leftClickEvent.getUnprojected());

    }

    @Override
    public void updateEvent(MouseEvent mouseEvent) {
        leftClickEvent = mouseEvent.isLeftClick() ? mouseEvent : null;
    }
}