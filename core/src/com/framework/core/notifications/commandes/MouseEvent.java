package com.framework.core.notifications.commandes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.framework.core.Conf;
import com.framework.core.managers.CameraManager;

public class MouseEvent {

    private static final OrthographicCamera CAMERA = CameraManager.GetInstance().getCamera();

    private Vector2 position;
    private Vector2 unprojected;
    private int button;

    private boolean dragged;
    private boolean up;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public MouseEvent(int screenX, int screenY, int button, boolean up, boolean dragged) {
        this.unprojected = unprojectedPosition(screenX, screenY);
        this.dragged = dragged;
        this.button = button;
        this.position = new Vector2(screenX,  screenY);
        this.up = up;
        this.debug();
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Vector2 getUnprojected() {
        return unprojected;
    }

    public int getX() {
        return (int) unprojected.x;
    }

    public int getY() {
        return (int) unprojected.y;
    }

    public boolean isRightClick() {
        return button == Input.Buttons.RIGHT && !isMouseUp();
    }

    public boolean isLeftClick() {
        return button == Input.Buttons.LEFT && !isMouseUp();
    }

    public boolean isMiddleClick() {
        return button == Input.Buttons.MIDDLE && !isMouseUp();
    }

    public boolean isDragged() {
        return dragged;
    }

    public boolean isMouseUp() {
        return up;
    }

    public Vector2 getPosition() {
        return position;
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    public static Vector2 unprojectedPosition(int x, int y) {
        Vector3 unprojected = CAMERA.unproject(new Vector3(x, y, 0));
        return new Vector2(unprojected.x, unprojected.y);
    }

    public static Vector2 unprojectedMousePosition() {
        return unprojectedPosition(Gdx.input.getX(), Gdx.input.getY());
    }

    public void debug() {
        if (!Conf.MOUSE_EVENT_DEBUG) return;
        StringBuilder sb = new StringBuilder("Button: ");
        sb.append(button).append("\t").append(up ? "released" : "clicked");
        sb.append(" \tdragged: ").append(dragged);
        sb.append(" \t").append(unprojected);
        System.out.println(sb.toString());
    }
}
