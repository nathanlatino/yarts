package com.framework.core.notifications.listeners;

import com.badlogic.gdx.utils.Array;
import com.framework.core.notifications.commandes.KeyboardEvent;
import com.framework.core.notifications.interfaces.KeyboardEventSubscriber;
import com.framework.core.notifications.interfaces.Publisher;


public class KeyboardListener implements Publisher<KeyboardEventSubscriber> {

    private KeyboardEvent keyboardEvent;
    private Array<KeyboardEventSubscriber> subscribers;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public KeyboardListener() {
        this.keyboardEvent = null;
        this.subscribers = new Array();
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void keyDown(int keycode) {
        keyboardEvent = KeyboardEvent.getInstance(keycode, false);
        notifySubscribers();
    }


    public void keyUp(int keycode) {
        keyboardEvent = KeyboardEvent.getInstance(keycode, true);
        notifySubscribers();
    }

    /*------------------------------------------------------------------*\
    |*					      Publisher Interface
    \*------------------------------------------------------------------*/

    @Override
    public void subscribe(KeyboardEventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unSubscribe(KeyboardEventSubscriber subscriber) {
        subscribers.removeValue(subscriber, false);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.updateEvent(keyboardEvent));
    }
}
