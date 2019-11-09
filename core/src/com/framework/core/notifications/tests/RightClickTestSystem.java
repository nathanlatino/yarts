package com.framework.core.notifications.tests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.core.notifications.commandes.ContactEvent;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.ContactEventSubscriber;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;

public class RightClickTestSystem extends IteratingSystem implements MouseEventSubscriber, ContactEventSubscriber {

    private MouseEvent rightClickEvent;
    private ContactEvent contactEvent;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public RightClickTestSystem() {
        super(Family.all().get());
        rightClickEvent = null;
        contactEvent = null;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        if (rightClickEvent == null && contactEvent == null) return;

        getEntities().forEach(entity -> processEntity(entity, dt));

        rightClickEvent = null;
        contactEvent = null;
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        // if (rightClickEvent != null) System.out.println("RIGHT_CLICK: " + rightClickEvent.getUnprojected());
        // if (contactEvent != null) System.out.println("RIGHT_CLICK (contact): " + contactEvent);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void updateEvent(MouseEvent mouseEvent) {
        rightClickEvent = mouseEvent.isRightClick() ? mouseEvent : null;
    }

    @Override
    public void updateEvent(ContactEvent collision) {
        contactEvent = collision;
    }
}