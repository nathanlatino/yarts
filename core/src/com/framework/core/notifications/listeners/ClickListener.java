package com.framework.core.notifications.listeners;

import com.badlogic.gdx.utils.Array;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;
import com.framework.core.notifications.interfaces.Publisher;

public class ClickListener implements Publisher<MouseEventSubscriber> {

    private MouseEvent mouseEvent;
    private Array<MouseEventSubscriber> subscribers;
    private int button;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public ClickListener() {
        this.mouseEvent = null;
        this.subscribers = new Array();
        this.button = 0;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void touchDown(int screenX, int screenY, int button) {
        this.button = button;
        this.mouseEvent = new MouseEvent(screenX, screenY, button, false, false);
        notifySubscribers();
    }

    public void touchUp(int screenX, int screenY, int button) {
        this.button = button;
        mouseEvent = new MouseEvent(screenX, screenY, button, true, false);
        notifySubscribers();
    }

    public void touchDragged(int screenX, int screenY) {
        mouseEvent = new MouseEvent(screenX, screenY, button, false, true);
        notifySubscribers();
    }

    /*------------------------------------------------------------------*\
    |*					      Publisher Interface
    \*------------------------------------------------------------------*/

    @Override
    public void subscribe(MouseEventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unSubscribe(MouseEventSubscriber subscriber) {
        subscribers.removeValue(subscriber, false);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.updateEvent(mouseEvent));
    }
}
