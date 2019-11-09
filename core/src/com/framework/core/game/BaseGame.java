package com.framework.core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.framework.core.game.screen.BaseScreen;

public abstract class BaseGame extends Game {

    // stores reference to main; used when calling setActiveScreen method.
    private static BaseGame game;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    /**
     * Called when main isSetTo initialized; stores global reference to main object.
     */
    public BaseGame() {
        game = this;
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 							*|
   	\*------------------------------------------------------------------*/

    @Override
    public void create() {

        // prepare for multiple classes/stages/actors to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);

    }

    /**
     * Used to switch screens while main isSetTo running.
     * Method isSetTo static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }
}
