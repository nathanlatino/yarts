package com.framework.core.notifications.handlers.keyboard;

import com.badlogic.gdx.Input;
import com.framework.core.Conf;
import com.framework.core.notifications.commandes.KeyboardEvent;
import com.framework.core.notifications.interfaces.KeyboardEventSubscriber;
import com.framework.core.notifications.listeners.InputListener;


public class DebugKeysHandler implements KeyboardEventSubscriber {

    private KeyboardEvent keyboardEvent;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public DebugKeysHandler() {
        InputListener.getInstance().getKeyboardListener().subscribe(this);
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void debugKeys() {
        switch (keyboardEvent.getKeycode()) {
            case Input.Keys.F1:
                Conf.toggleStateNotifications();
                break;
            case Input.Keys.F2:
                Conf.toggleMouseEventDebug();
                Conf.toggleKeyboardEventDebug();
                break;
            case Input.Keys.F3:
                // free
                break;
            case Input.Keys.F4:
                Conf.toggleGuiRendering();
                break;
            case Input.Keys.F5:
                // free
                break;
            case Input.Keys.F7:
                Conf.toggleHealthBar();
                break;
            case Input.Keys.F8:
                Conf.strategyPathfinding();
                break;
            case Input.Keys.F9:
                Conf.togglePathfinderDebugRendering();
                break;
            case Input.Keys.F10:
                Conf.cycleRangesDebug();
                break;
            case Input.Keys.F11:
                Conf.toggleLightsRendering();
                break;
            case Input.Keys.F12:
                Conf.toggleB2dDebugRendering();
        }
    }

    /*------------------------------------------------------------------*\
    |*							KeyboardEvent
    \*------------------------------------------------------------------*/

    @Override
    public void updateEvent(KeyboardEvent keyboardEvent) {
        if (!keyboardEvent.isUp()) {
            this.keyboardEvent = keyboardEvent;
            debugKeys();
        }
    }

    /*------------------------------------------------------------------*\
    |*							    Debug
    \*------------------------------------------------------------------*/

    public static String getinfo() {
        StringBuilder sb = new StringBuilder("Debug info:");
        sb.append("\n---------");
        sb.append("\nF1: toggle state notifications");
        sb.append("\nF2: toggle inputs events");
        sb.append("\nF4: toggle gui");
        sb.append("\nF7: toggle health");
        sb.append("\nF8: pathfinding strategy");
        sb.append("\nF9: toggle pathfinding marks");
        sb.append("\nF10: Cycle range");
        sb.append("\nF11: toggle lights");
        sb.append("\nF12: toggle physics debug");
        return sb.toString();
    }


}
