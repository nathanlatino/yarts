package com.framework.core.notifications.tests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;

public class MouseDragTestSystem extends IteratingSystem implements MouseEventSubscriber {

    private MouseEvent mouseEvent;
    private Vector2 startPoint;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public MouseDragTestSystem() {
        super(Family.all().get());
        mouseEvent = null;
        startPoint = null;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        if (startPoint == null) return;

        getEntities().forEach(entity -> processEntity(entity, dt));
        // System.out.println();
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        // System.out.println("DRAGGED start: " + startPoint + "\t current: " + mouseEvent.getUnprojected());
    }

    @Override
    public void updateEvent(MouseEvent mouseEvent) {
        if (mouseEvent.isDragged() && !mouseEvent.isRightClick()) {
            if (this.startPoint == null) {
                this.startPoint = mouseEvent.getUnprojected();
            }
            this.mouseEvent = mouseEvent;
        } else {
            this.startPoint = null;
            this.mouseEvent = null;
        }
    }
}