package com.framework.ecs.mouse.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.core.notifications.handlers.mouse.LeftClickHandler;
import com.framework.core.notifications.handlers.mouse.SelectionRectangleHandler;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.mouse.components.SelectionComponent;

public class SimpleSelectionSystem extends IteratingSystem {

    private LeftClickHandler leftClickHandler;
    private SelectionRectangleHandler selectionRectangle;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public SimpleSelectionSystem(LeftClickHandler leftClickHandler, SelectionRectangleHandler selectionRectangle) {
        super(Family.all(SelectionComponent.class).get());
        this.leftClickHandler = leftClickHandler;
        this.selectionRectangle= selectionRectangle;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {

        if (leftClickHandler.getEvent() == null || leftClickHandler.inGuiBounds() || selectionRectangle.haveMinimumArea()) return;

        EcsUtils.unselectAll(getEngine());

        for (Entity entity : getEntities()) {
            if (this.leftClickHandler.isContained(Mapper.bounds_m.get(entity).getBounds())) {
                Mapper.selection_m.get(entity).setSelected(true);
                this.leftClickHandler.setMouseEvent(null);
                return;
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float dt) {

    }
}