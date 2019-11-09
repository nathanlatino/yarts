package com.framework.core.notifications.tests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.core.notifications.commandes.ContactEvent;
import com.framework.core.notifications.interfaces.ContactEventSubscriber;
import com.framework.ecs.core.components.BodyComponent;

public class EndContactTestSystem extends IteratingSystem implements ContactEventSubscriber {

    private ContactEvent contactEvent;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public EndContactTestSystem() {
        super(Family.all(BodyComponent.class).get());
        contactEvent = null;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/
    @Override
    public void update(float dt) {
        if (contactEvent == null) return;
        getEntities().forEach(entity -> processEntity(entity, dt));
        contactEvent = null;
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        // System.out.println("END contact between " + contactEvent.getTypeB() + " and " + contactEvent.getTypeA());
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public void updateEvent(ContactEvent contactEvent) {
        if (!contactEvent.isStart()) {
            this.contactEvent = contactEvent;
        }
    }
}