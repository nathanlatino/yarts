package com.framework.core.notifications.commandes;

import com.badlogic.gdx.Input;
import com.framework.core.Conf;

public class KeyboardEvent {

    private static KeyboardEvent instance;
    private int keycode;
    private boolean up;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private KeyboardEvent() {

    }

    public static KeyboardEvent getInstance(int keycode, boolean up) {
        if (instance == null) {
            instance = new KeyboardEvent();
        }
        instance.keycode = keycode;
        instance.up = up;
        instance.debug();
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public int getKeycode() {
        return keycode;
    }

    public boolean isUp() {
        return up;
    }

    /*------------------------------------------------------------------*\
    |*							    Debug
    \*------------------------------------------------------------------*/

    private void debug() {
        if (!Conf.KEYBOARD_EVENT_DEBUG) return;
        StringBuilder sb = new StringBuilder("Key: ");
        sb.append(Input.Keys.toString(keycode));
        sb.append(" \t(").append(keycode).append(") \t");
        sb.append(isUp() ? "released" : "pressed");
        System.out.println(sb.toString());
    }

}
