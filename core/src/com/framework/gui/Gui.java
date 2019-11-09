package com.framework.gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.framework.core.Conf;
import com.framework.core.game.map.Map;
import com.framework.core.notifications.handlers.mouse.SelectionRectangleHandler;
import com.framework.gui.interaction.DetailRessource;
import com.framework.gui.interaction.DetailSelectUnit;
import com.framework.gui.interaction.PanelButton;
import com.framework.gui.minimap.MiniMap;

public class Gui extends BaseUi {

    private MiniMap miniMap;
    private DetailRessource detailRessource;
    private PanelButton panelButton;
    DetailSelectUnit detailSelectUnity;

    public Stage stage;
    public Table root;

    protected OrthographicCamera guiCamera;
    protected ScreenViewport guiViewport;

    protected Rectangle rectGameCamera;

    /*------------------------------------------------------------------*\
    |*							Constructors						  *|
    \*------------------------------------------------------------------*/

    public Gui(Map mapManager, Engine engine, SelectionRectangleHandler selectionRectangleHandler) {
        rectGameCamera = new Rectangle(0, 0,
                cameras.getViewport().getWorldWidth() * Conf.PPM,
                cameras.getViewport().getWorldHeight() * Conf.PPM
        );

        guiCamera = new OrthographicCamera();
        guiViewport = new ScreenViewport(guiCamera);
        guiViewport.setScreenBounds(
                (int) rectGameCamera.x, (int) rectGameCamera.y,
                (int) rectGameCamera.width, (int) rectGameCamera.height);
        guiViewport.apply();

        stage = new Stage(guiViewport, batch);
        // Gdx.input.setInputProcessor(stage);



        root = new Table();
        root.setFillParent(true);
        root.top().left();
        root.setSize(rectGameCamera.width, rectGameCamera.height);
        stage.addActor(root);

        miniMap = new MiniMap(mapManager, new Vector2(0, 0), engine, rectGameCamera, selectionRectangleHandler);
        // detailSelectUnity = new DetailSelectUnit(root, rectGameCamera, engine);
        detailRessource = new DetailRessource(root, rectGameCamera);

        // root.row();
        // panelButton = new PanelButton(root, rectGameCamera);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 						  *|
	\*------------------------------------------------------------------*/

    @Override
    public void resize(int width, int height) {
        guiViewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Conf.RENDER_GUI) {
            detailRessource.render(delta);
            miniMap.render(delta);
            // panelButton.render(delta);
        }
    }
    /*------------------------------*\
	|*				Getters		    *|
	\*------------------------------*/

    public PanelButton getPanelButton() {
        return panelButton;
    }

    /*------------------------------*\
	|*				Setters		   *|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Private Methods 				      *|
	\*------------------------------------------------------------------*/
}
