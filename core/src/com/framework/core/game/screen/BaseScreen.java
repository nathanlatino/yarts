package com.framework.core.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.framework.core.managers.CameraManager;
import com.framework.core.managers.PhysicsManager;
import com.framework.core.notifications.listeners.InputListener;

public abstract class BaseScreen implements Screen {

    protected SpriteBatch batch;


    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public BaseScreen() {
        PhysicsManager.getInstance().setCamera(CameraManager.GetInstance().getCamera());

        batch = new SpriteBatch();
        // batch.setProjectionMatrix(CameraManager.getCamera().combined);
    }

    /*------------------------------------------------------------------*\
    |*							Update methods
    \*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /*------------------------------------------------------------------*\
	|*							Screen interface
	\*------------------------------------------------------------------*/

    @Override
    public void show() {
        Gdx.input.setInputProcessor(InputListener.getInstance());
    }

    @Override
    public void hide() {
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
