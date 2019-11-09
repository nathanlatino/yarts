package com.framework.core.notifications.handlers.mouse;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.framework.core.managers.GuiManager;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;
import com.framework.core.notifications.listeners.InputListener;

public class LeftClickHandler implements MouseEventSubscriber {

    private final GuiManager guiManager;
    private MouseEvent mouseEvent;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public LeftClickHandler() {
        this.guiManager = GuiManager.getInstance();
        mouseEvent = null;
        InputListener.getInstance().getClickListener().subscribe(this);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public boolean isContained(Rectangle bounds) {
        return bounds.contains(this.mouseEvent.getUnprojected());
    }

    public boolean inGuiBounds() {
        if (mouseEvent == null) return false;
        for (Rectangle pannel : guiManager.getBounds()) {
            if (pannel.contains(mouseEvent.getPosition()) ) {
                return true;
            }
        }
        return false;
    }
    public boolean inGuiBounds(Vector2 position) {
        if (mouseEvent == null) return false;
        for (Rectangle pannel : guiManager.getBounds()) {
            if (position != null && pannel.contains(position) ) {
                return true;
            }
        }
        return false;
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
        this.mouseEvent = mouseEvent.isLeftClick() ? mouseEvent : null;
    }
}
