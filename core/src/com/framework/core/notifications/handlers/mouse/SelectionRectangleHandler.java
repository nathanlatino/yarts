package com.framework.core.notifications.handlers.mouse;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.interfaces.MouseEventSubscriber;
import com.framework.core.notifications.listeners.InputListener;
import com.framework.utils.RectangleShape;

public class SelectionRectangleHandler implements MouseEventSubscriber {

    private static final  float MINIMUM_AREA = .1f;
    private final Texture texture;
    private final LeftClickHandler leftClickHandler;

    private Rectangle selection;

    private Vector2 startPoint;
    private Vector2 startPointUnprojected;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public SelectionRectangleHandler(LeftClickHandler leftClickHandler) {
        this.leftClickHandler = leftClickHandler;
        this.selection = new Rectangle();
        this.texture = RectangleShape.getInstance().getRectangle(0, 0, 1, 1);
        InputListener.getInstance().getClickListener().subscribe(this);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public boolean contains(Vector2 position) {
        return normalizedSelection().contains(position.x, position.y);
    }

    public boolean haveMinimumArea() {
        return Math.abs(selection.area()) > MINIMUM_AREA;
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
        batch.end();
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Rectangle getSelection() {
        return selection;
    }

    public Vector2 getStartPointUnprojected() {
        return startPointUnprojected;
    }

    public Vector2 getStartPoint() {
        return startPoint;
    }

    public LeftClickHandler getLeftClickHandler() {
        return leftClickHandler;
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void updateRectangleData(Vector2 position) {
        float width = position.x - startPoint.x;
        float height = -(startPoint.y - position.y);
        selection.set(startPoint.x, startPoint.y, width, height);
    }

    /**
     * Convert coords to keep x and y in the bottom left corner and
     * make sure that the length and the with are always > 0.
     * @return Rectangle
     */
    private Rectangle normalizedSelection() {
        float w = selection.getWidth();
        float h = selection.getHeight();
        float x = selection.getX();
        float y = selection.getY();

        float nX = w < 0 ? x + w : x;
        float nY = h < 0 ? y + h : y;

        return new Rectangle(nX, nY, Math.abs(w), Math.abs(h));
    }

    private void resetSelection() {
        startPoint = null;
        selection.setX(0);
        selection.setY(0);
        selection.setWidth(0);
        selection.setHeight(0);
    }

    /*------------------------------------------------------------------*\
    |*							MouseEvent
    \*------------------------------------------------------------------*/

    @Override
    public void updateEvent(MouseEvent mouseEvent) {
        if (leftClickHandler.inGuiBounds() && leftClickHandler.inGuiBounds(startPointUnprojected)) return;
        if (mouseEvent.isDragged() && !mouseEvent.isRightClick()) {
            if (this.startPoint == null) {
                this.startPoint = mouseEvent.getUnprojected();
                this.startPointUnprojected = mouseEvent.getPosition();
            }
            updateRectangleData(mouseEvent.getUnprojected());
        } else {
            resetSelection();
        }
    }

}
