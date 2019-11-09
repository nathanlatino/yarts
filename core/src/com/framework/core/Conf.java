package com.framework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;


public abstract class Conf {

    // Resolution

    // public static final int RESOLUTION_W = 3840;
    // public static final int RESOLUTION_H = 2160;

    public static final int RESOLUTION_W = 1920;
    public static final int RESOLUTION_H = 1080;

    // public final static int RESOLUTION_W = 1280;
    // public final static int RESOLUTION_H = 720;

    // Pixel per meter & Meter per Pixel
    public final static float PPM = 32.0f;
    public final static float PTM = 1.0f / PPM;
    public final static Vector2 SCALE = new Vector2(.5f, .5f);

    public final static float WORLD_WIDTH = Gdx.graphics.getWidth() / PPM;
    public final static float WORLD_HEIGHT = Gdx.graphics.getHeight() / PPM;

    // Physics
    public final static float GRAVITY_X = 0;
    public final static float GRAVITY_Y = -9.8f;


    // Camera
    public final static boolean FULL_SCREEN = true;

    public final static float SCROLL_SPEED = .005f;
    public final static float SCROLL_BOUND = 40f;

    public final static float ZOOM_SPEED = .1f;
    public final static float MAX_ZOOM_IN = .2f;
    public final static float MAX_ZOOM_OUT = 2f;

    public final static boolean ENABLE_KEYBOARD_MOVE_CAMERA = true;
    public final static boolean ENABLE_MOUSE_WHEEL_ZOOM = true;
    public final static boolean ENABLE_MOUSE_MOVE_CAMERA = true;


    // Path
    public static boolean PATHFINDING = false;

    // Light
    public final static Color AMBIENT_COLOR = new Color(0x33333300);

    // Rendering
    public static boolean RENDER_ENTITIES = true;
    public static boolean RENDER_SELECTION_SHADER = true;
    public static boolean RENDER_LIGHTS = true;
    // public static boolean RENDER_FOG_OF_WAR = false;
    public static boolean RENDER_GUI = true;

    // Debug
    public static boolean DEBUG = false;
    public static boolean FPS = true;
    public static boolean MOUSE_EVENT_DEBUG = false;
    public static boolean KEYBOARD_EVENT_DEBUG = false;

    public static boolean RENDER_PATHFINDER_DEBUG = false;
    public static boolean RENDER_B2D_DEBUG = false;
    public static boolean RENDER_MAP_DEBUG = true;
    public static boolean RENDER_HEALTH = true;
    public static int RENDER_RANGE_DEBUG = 0;

    public static boolean STATE_CHANGE_NOTIFICATIONS = false;
    public static boolean CONTACT_NOTIFICATIONS = false;
    public static boolean TARGETS_NOTIFICATIONS = true;

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public static float pixelsToMeters(float pixelValue) {
        return pixelValue * PTM;
    }

    public static void initialize() {
        // empty
    }

    public static void toggleDebug() {
        DEBUG = !DEBUG;
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private static void feedBack(String system, boolean value) {
        System.out.println(system + " debug: " + (value ? "on" : "off"));
    }
    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public static String getInfo() {
        StringBuilder sb = new StringBuilder("Config info:");
        sb.append("\n---------");
        sb.append("\nResolution ").append(RESOLUTION_W).append(" x ").append(RESOLUTION_H);
        sb.append("\nWorld width: ").append(WORLD_WIDTH).append(" x ").append(WORLD_HEIGHT);
        sb.append("\nGravity: (").append(GRAVITY_X).append(", ").append(GRAVITY_Y).append(")");
        sb.append("\nPPM: ").append(PPM);
        return sb.toString();
    }

    /*------------------------------*\
    |*			  Debugging
    \*------------------------------*/

    public static void toggleEntitiesRendering() {
        feedBack("Entities rendering", !RENDER_ENTITIES);
        RENDER_ENTITIES = !RENDER_ENTITIES;
    }

    public static void toggleSelectionShaderRendering() {
        feedBack("Selection shader", !RENDER_SELECTION_SHADER);
        RENDER_SELECTION_SHADER = !RENDER_SELECTION_SHADER;
    }

    public static void toggleMapDebugRendering() {
        feedBack("Map", !RENDER_MAP_DEBUG);
        RENDER_MAP_DEBUG = !RENDER_MAP_DEBUG;
    }

    public static void togglePathfinderDebugRendering() {
        feedBack("Pathfinder marks", !RENDER_B2D_DEBUG);
        RENDER_PATHFINDER_DEBUG = !RENDER_PATHFINDER_DEBUG;
    }

    public static void toggleB2dDebugRendering() {
        feedBack("b2dr", !RENDER_B2D_DEBUG);
        RENDER_B2D_DEBUG = !RENDER_B2D_DEBUG;
    }

    public static void toggleLightsRendering() {
        feedBack("Lights", !RENDER_LIGHTS);
        RENDER_LIGHTS = !RENDER_LIGHTS;
    }

    public static void toggleGuiRendering() {
        feedBack("ui", !RENDER_GUI);
        RENDER_GUI = !RENDER_GUI;
    }

    // public static void toggleFogRendering() {
    //     feedBack("Lights", !RENDER_FOG_OF_WAR);
    //     RENDER_FOG_OF_WAR = !RENDER_FOG_OF_WAR;
    // }

    public static void toggleHealthBar() {
        feedBack("HealtBar", !RENDER_HEALTH);
        RENDER_HEALTH = !RENDER_HEALTH;
    }

    public static void strategyPathfinding() {
        feedBack("A*", !PATHFINDING);
        PATHFINDING = !PATHFINDING;
    }

    public static void toggleMouseEventDebug() {
        feedBack("Mouse event", !MOUSE_EVENT_DEBUG);
        MOUSE_EVENT_DEBUG = !MOUSE_EVENT_DEBUG;
    }

    public static void toggleKeyboardEventDebug() {
        feedBack("Keyboard event", !KEYBOARD_EVENT_DEBUG);
        KEYBOARD_EVENT_DEBUG = !KEYBOARD_EVENT_DEBUG;
    }

    public static void toggleStateNotifications() {
        feedBack("State notifications: ", !STATE_CHANGE_NOTIFICATIONS);
        STATE_CHANGE_NOTIFICATIONS = !STATE_CHANGE_NOTIFICATIONS;
    }

    public static void toggleContactNotifications() {
        feedBack("Contact notifications: ", !CONTACT_NOTIFICATIONS);
        CONTACT_NOTIFICATIONS = !CONTACT_NOTIFICATIONS;
    }

    public static void toggleTargetsNotifications() {
        feedBack("Targets notifications: ", !TARGETS_NOTIFICATIONS);
        TARGETS_NOTIFICATIONS = !TARGETS_NOTIFICATIONS;
    }

    public static void cycleRangesDebug() {
        if (RENDER_RANGE_DEBUG >= 3) RENDER_RANGE_DEBUG = 0;
        else RENDER_RANGE_DEBUG++;
        System.out.println("Range: " + RENDER_RANGE_DEBUG);
    }
}
