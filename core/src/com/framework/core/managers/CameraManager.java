package com.framework.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.framework.core.Conf.*;

public class CameraManager {

    private static CameraManager instance;

    // Camera
    private OrthographicCamera camera;
    private FitViewport viewport;

    // Zoom
    private float zoomLevel;

    /*------------------------------------------------------------------*\
   	|*							Initialization
   	\*------------------------------------------------------------------*/

    private CameraManager() {
        zoomLevel = .40f;
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        // camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.position.set(81, 10, 0);
        viewport.apply();
        // camera.position.add(800, 800 , 0);
    }


    public static CameraManager GetInstance() {
        if (instance == null) {
            instance = new CameraManager();
        }
        return instance;
    }

    // public static OrthographicCamera getCamera() {
    //     if (instance == null) {
    //         instance = new CameraManager();
    //     }
    //     return instance.camera;
    // }

    /*------------------------------------------------------------------*\
   	|*							Update Methods
   	\*------------------------------------------------------------------*/

    // public void setZoomLevel(int amount) {
    //     float x = MathUtils.clamp(zoomLevel + amount * ZOOM_SPEED, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    //     float width = camera.viewportWidth * x;
    //     float height = camera.viewportHeight * x;
    //     if (width <= 150 && width >= 5) {
    //         camera.viewportWidth = width;
    //         camera.viewportHeight = height;
    //     }
    // }

    public void updateCameras() {
        if (ENABLE_KEYBOARD_MOVE_CAMERA) keyboardMoveCamera();
        if (ENABLE_MOUSE_MOVE_CAMERA) alignCameraWithMouse();
        if (ENABLE_MOUSE_WHEEL_ZOOM) updateZoom();
        camera.update();
    }

    public void setZoomLevel(int amount) {
        zoomLevel = MathUtils.clamp(zoomLevel + amount * ZOOM_SPEED, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    /*------------------------------------------------------------------*\
	|*							Public Methods
	\*------------------------------------------------------------------*/

    public void updateZoom() {
        camera.zoom = zoomLevel;
    }

    public void alignCameraWithMouse() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        float offsetX = 0;
        float offsetY = 0;

        if (mouseX < SCROLL_BOUND)
            offsetX = -SCROLL_SPEED * (SCROLL_BOUND - mouseX);

        if (mouseX > RESOLUTION_W - SCROLL_BOUND)
            offsetX = SCROLL_SPEED * (SCROLL_BOUND - (RESOLUTION_W - mouseX));

        if (mouseY < SCROLL_BOUND)
            offsetY = SCROLL_SPEED * (SCROLL_BOUND - mouseY);

        if (mouseY > RESOLUTION_H - SCROLL_BOUND)
            offsetY = -SCROLL_SPEED * (SCROLL_BOUND - (RESOLUTION_H - mouseY));

        camera.position.add(offsetX, offsetY, 0);
    }

    public void keyboardMoveCamera() {
        int coeff = 10;
        int offsetX = 0;
        int offsetY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) coeff = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) offsetY += coeff;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) offsetY -= coeff;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) offsetX -= coeff;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) offsetX += coeff;

        camera.position.add(offsetX / PPM, offsetY / PPM, 0);
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Viewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return instance.camera;
    }


}
