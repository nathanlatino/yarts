package com.framework.core.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.framework.core.Conf;
import com.framework.gui.BaseUi;
import com.framework.gui.utils.GuiElement;

public class GuiManager extends BaseUi {

    private static GuiManager instance;
    private final Stage stage;
    private final ScreenViewport guiViewport;
    private final OrthographicCamera guiCamera;

    private Array<Rectangle> bounds;
    private Table root;

    private Rectangle uiSize;


    private GuiElement selectionTable;
    private GuiElement minimap;
    private GuiElement buttonTable;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private GuiManager() {
        this.bounds = new Array();
        this.guiCamera = new OrthographicCamera();
        this.guiViewport = new ScreenViewport(guiCamera);
        this.stage = new Stage(guiViewport, batch);

        initRoot();
        initPannels();
    }

    /*------------------------------------------------------------------*\
    |*							Initialization
    \*------------------------------------------------------------------*/

    private void initPannels() {
        buttonTableInitialization();
        selectionTableInitialization();

    }


    private void initRoot() {
        float width = cameras.getViewport().getWorldWidth() * Conf.PPM;
        float height = cameras.getViewport().getWorldHeight() * Conf.PPM;

        uiSize = new Rectangle(0, 0, width, height);
        root = new Table();
        root.setFillParent(true);
        root.top().left();
        root.setSize(uiSize.width, uiSize.height);
        stage.addActor(root);
    }

    public static GuiManager getInstance() {
        if (instance == null) {
            instance = new GuiManager();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Conf.RENDER_GUI) {
            stage.act(delta);
            stage.draw();
        }
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Array<Rectangle> getBounds() {
        return bounds;
    }

    public void setBounds(Array<Rectangle> bounds) {
        this.bounds = bounds;
    }

    public void addPannel(Rectangle pannel) {
        bounds.add(pannel);
    }

    public Table getRoot() {
        return root;
    }

    /*------------------------------------------------------------------*\
    |*							Elements
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*		   Initialization
    \*------------------------------*/

    private void selectionTableInitialization() {
        int width = 160;
        int height = 180;
        selectionTable = new GuiElement(uiSize.width - width, uiSize.height - height, width, height, "selectionTable", "panelButton");
        root.add(selectionTable.getTable()).maxSize(width, height).minSize(width, height).bottom().right().fill();
        bounds.add(selectionTable.getBounds());
    }

    private void buttonTableInitialization() {
        int width = 400;
        int height = 150;
        buttonTable = new GuiElement(uiSize.width/2 - (width/2f) - 75, uiSize.height - height, width, height, "selectionTable", "panelButton");
        root.add(buttonTable.getTable()).maxSize(width, height).minSize(width, height).expand().center().bottom();
        bounds.add(buttonTable.getBounds());
    }

    /**
     * Temporary called directly by MiniMap.java
     */
    public void minimapInitialization(float x, float y, float width, float height) {
        root.left();
        minimap = new GuiElement(x, uiSize.height - height, width, height, "minimap", "panelButton");
        // root.add(minimap.getTable()).maxSize(width+50, height+50).bottom().left().expand();
        bounds.add(minimap.getBounds());
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public GuiElement getSelectionTable() {
        return selectionTable;
    }

    public GuiElement getButtonTable() {
        return buttonTable;
    }
}
