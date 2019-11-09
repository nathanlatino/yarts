package com.framework.ecs.mouse.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.framework.core.notifications.handlers.mouse.LeftClickHandler;
import com.framework.core.notifications.handlers.mouse.SelectionRectangleHandler;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.mouse.components.MultiSelectionComponent;
import com.framework.ecs.mouse.components.SelectionComponent;

public class MultiSelectionSystem extends IteratingSystem {

    private SelectionRectangleHandler rectangle;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public MultiSelectionSystem(SelectionRectangleHandler selectionRectangleHandler) {
        super(Family.all(SelectionComponent.class, MultiSelectionComponent.class).get());
        this.rectangle = selectionRectangleHandler;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {

        if (rectangle.getStartPoint() == null) return;


        getEntities().forEach(entity -> processEntity(entity, dt));
    }

    @Override
    protected void processEntity(Entity entity, float dt) {

        LeftClickHandler leftClickHandler = rectangle.getLeftClickHandler();
        if (leftClickHandler.getEvent() == null) return;


        SelectionComponent sc = Mapper.selection_m.get(entity);

        if (rectangle.contains(Mapper.transform_m.get(entity).getPosition())) {
            sc.setSelected(true);
        } else if (rectangle.haveMinimumArea()){
            sc.setSelected(false);
        }
    }
}