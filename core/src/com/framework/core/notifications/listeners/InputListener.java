package com.framework.core.notifications.listeners;

import com.badlogic.gdx.InputProcessor;
import com.framework.core.managers.CameraManager;

public class InputListener implements InputProcessor {

    private static InputListener inputListener;

    ClickListener clickListener;
    KeyboardListener keyboardListener;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public InputListener() {
        clickListener = new ClickListener();
        keyboardListener = new KeyboardListener();
    }

    public static InputListener getInstance() {
        if (inputListener == null) {
            inputListener = new InputListener();
        }
        return inputListener;
    }

    /*------------------------------------------------------------------*\
    |*							Listeners
    \*------------------------------------------------------------------*/

    public ClickListener getClickListener() {
        return clickListener;
    }

    public KeyboardListener getKeyboardListener() {
        return keyboardListener;
    }

    /*------------------------------------------------------------------*\
    |*						    Input Processor
    \*------------------------------------------------------------------*/

    @Override
    public boolean keyDown(int keycode) {
        keyboardListener.keyDown(keycode);
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        keyboardListener.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        clickListener.touchDown(screenX, screenY, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickListener.touchUp(screenX, screenY, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        clickListener.touchDragged(screenX, screenY);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // empty
        return false;
    }



    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // empty
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        CameraManager.GetInstance().setZoomLevel(amount);
        return false;
    }
}
