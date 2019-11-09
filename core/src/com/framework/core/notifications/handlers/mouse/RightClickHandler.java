package com.framework.core.notifications.handlers.mouse;

import com.badlogic.gdx.math.Rectangle;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;
import com.framework.core.notifications.listeners.InputListener;

public class RightClickHandler implements MouseEventSubscriber {

    private MouseEvent mouseEvent;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public RightClickHandler() {
        mouseEvent = null;
        InputListener.getInstance().getClickListener().subscribe(this);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public boolean isContained(Rectangle bounds) {
        return bounds.contains(this.mouseEvent.getUnprojected());
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public MouseEvent getEvent() {
        return mouseEvent;
    }

    public void setMouseEvent(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    /*------------------------------------------------------------------*\
    |*							MouseEvent
    \*------------------------------------------------------------------*/

    @Override
    public void updateEvent(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent.isRightClick() && !mouseEvent.isDragged() ? mouseEvent : null;
    }
}
