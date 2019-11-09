package com.framework.core.notifications.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.framework.core.Conf;
import com.framework.core.notifications.commandes.ContactEvent;
import com.framework.core.notifications.interfaces.ContactEventSubscriber;
import com.framework.core.notifications.interfaces.Publisher;

/**
 * Too expensive for an RTS type game.
 */
public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener, Publisher<ContactEventSubscriber> {

    private static ContactListener instance;

    private ContactEvent contactEvent;

    private Array<ContactEventSubscriber> subscribers;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private ContactListener() {
        subscribers = new Array();
    }

    public static ContactListener getInstance(World world) {
        if (instance == null) {
            instance = new ContactListener();
            world.setContactListener(instance);
            instance.contactEvent = ContactEvent.getInstance(false);
        }
        return instance;
    }

    public static ContactListener getInstance() {
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Contact listener
    \*------------------------------------------------------------------*/

    @Override
    public void beginContact(Contact contact) {
        resolve(contact, true);
    }

    @Override
    public void endContact(Contact contact) {
        resolve(contact, false);
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void resolve(Contact contact, boolean start) {
        this.contactEvent.clear();
        this.contactEvent.setA(contact.getFixtureB());
        this.contactEvent.setB(contact.getFixtureA());
        this.contactEvent.setStart(start);

        if ((contactEvent.aIsLOS() && contactEvent.bIsBody() && filterContact())) {
            notifySubscribers();
            notifyContact();
        }
        if ((contactEvent.aIsCac() && contactEvent.bIsBody() && filterContact())) {
            notifySubscribers();
            notifyContact();
        }
        if ((contactEvent.aIsBody() && contactEvent.bIsLOS() && filterContact())) {
            notifySubscribers();
            notifyContact();
        }
        if ((contactEvent.bIsBody() && contactEvent.aIsCac() && filterContact())) {
            notifySubscribers();
            notifyContact();
        }
    }

    private boolean filterContact() {
        if (contactEvent.classA() != null && contactEvent.classB() != null && contactEvent.ownerA() != contactEvent.ownerB()) {
            return true;
        }
        return false;
    }

    /*------------------------------------------------------------------*\
    |*						Publisher interface
    \*------------------------------------------------------------------*/

    @Override
    public void subscribe(ContactEventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unSubscribe(ContactEventSubscriber subscriber) {
        subscribers.removeValue(subscriber, false);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.updateEvent(contactEvent));
    }

    /*------------------------------------------------------------------*\
    |*							    Debug
    \*------------------------------------------------------------------*/

    public void notifyContact() {
        if (!Conf.CONTACT_NOTIFICATIONS) return;
        // if (contactEvent.classA() != null && contactEvent.classB() != null && contactEvent.ownerA() != contactEvent.ownerB()) {
        StringBuilder sb = new StringBuilder("[CONTACT ");
        sb.append(contactEvent.isStart() ? "START" : "END");
        sb.append("] ");
        sb.append(contactEvent.ownerA()).append(": ");
        sb.append(contactEvent.classA().identify()).append(" A(");
        sb.append(contactEvent.colliderA()).append(")");
        sb.append("\t vs \t");
        sb.append(contactEvent.ownerB()).append(": ");
        sb.append(contactEvent.classB().identify()).append(" B(");
        sb.append(contactEvent.colliderB()).append(")");
        System.out.println(sb.toString());
        // }
    }


}


