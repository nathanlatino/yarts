package com.framework.gui.minimap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.framework.core.Conf;
import com.framework.core.game.map.Map;
import com.framework.core.managers.GuiManager;
import com.framework.core.notifications.handlers.mouse.SelectionRectangleHandler;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.states.components.TargetComponent;
import com.framework.enums.PlayerType;
import com.framework.gui.BaseUi;

public class MiniMap extends BaseUi {

    public final static float SCALE = 6f;
    public final static float INV_SCALE = 1.f / SCALE;
    private final float ratioScreen = (float) Conf.RESOLUTION_H / (float) Conf.RESOLUTION_W;

    private OrthogonalTiledMapRenderer miniRenderer;
    private Map mapManager;
    private OrthographicCamera miniCamera;
    private ScreenViewport miniViewport;
    private Engine engine;
    private SelectionRectangleHandler selectionRectangle;

    private float width, height;
    private float x, y;
    private float cx, cy;
    private Vector2 unit;

    private Vector2 sizeMiniMap;
    private Vector2 positionMiniMap;
    private Rectangle rectUi;


    public MiniMap(Map mapManager, Vector2 position, Engine engine, Rectangle rectUi, SelectionRectangleHandler selectionRectangleHandler) {
        super();
        this.rectUi = rectUi;
        this.mapManager = mapManager;
        this.engine = engine;

        positionMiniMap = new Vector2(position);
        sizeMiniMap = new Vector2(mapManager.getMapWidth() * INV_SCALE * ratioScreen + 20, mapManager.getMapHeight() * INV_SCALE * ratioScreen + 20);
        this.selectionRectangle = selectionRectangleHandler;

        GuiManager.getInstance().minimapInitialization(position.x, position.y, sizeMiniMap.x, sizeMiniMap.y);


        unit = new Vector2(sizeMiniMap.x / mapManager.getMapWidth(), sizeMiniMap.y / mapManager.getMapHeight());

        miniCamera = new OrthographicCamera();
        miniViewport = new ScreenViewport(miniCamera);
        miniRenderer = new OrthogonalTiledMapRenderer(mapManager.getTiledMap(), INV_SCALE, batch);

        setView();
    }

    private void setView() {
        miniViewport.setWorldSize(mapManager.getMapWidth(), mapManager.getMapHeight() * ratioScreen); // taille
        miniCamera.position.set(miniViewport.getWorldWidth() / 2 - positionMiniMap.x, miniViewport.getWorldHeight() / 2 - positionMiniMap.y, 0); // position
        miniViewport.setScreenBounds((int) positionMiniMap.x, (int) positionMiniMap.y, (int) (sizeMiniMap.x), (int) (sizeMiniMap.y));
        miniViewport.apply();
        miniRenderer.setView(miniCamera);
    }

    @Override
    public void render(float delta) {
        renderClick();

        refreshCord();
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        HdpiUtils.glScissor(miniViewport.getScreenX(), miniViewport.getScreenY(), miniViewport.getScreenWidth(), miniViewport.getScreenHeight());
        miniRenderer.render();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        //DEBUG
        // renderer.setColor(Color.RED);
        // renderer.rect(miniViewport.getScreenX(), miniViewport.getScreenY(), miniViewport.getScreenWidth()-1, miniViewport.getScreenHeight()-1);
        // renderer.setColor(Color.BLUE);
        // renderer.rect(0, 0, sizeMiniMap.x, sizeMiniMap.y);
        //DEBUG

        renderer.setColor(Color.MAGENTA);
        renderer.rect((positionMiniMap.x * unit.x) + (cx * unit.x), (positionMiniMap.y * unit.y) + (cy * unit.y), 2, 2);
        renderer.setColor(Color.CYAN);
        renderer.rect(x * unit.x, y * unit.y, width * unit.x, height * unit.y);
        rendererUnit(renderer);
        renderer.end();

        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    private void refreshCord() {
        cx = cameras.getCamera().position.x * Conf.PPM;
        cy = cameras.getCamera().position.y * Conf.PPM;
        width = (cameras.getViewport().getWorldWidth() * Conf.PPM) * cameras.getCamera().zoom;
        height = (cameras.getViewport().getWorldHeight() * Conf.PPM) * cameras.getCamera().zoom;
        x = cx - width / 2;
        y = cy - height / 2;
    }

    private void renderClick() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !selectionRectangle.haveMinimumArea()) {

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();
            if (mouseX < miniViewport.getScreenWidth() && mouseY > (rectUi.height - miniViewport.getScreenHeight())) {
                cameras.getCamera().position.set(new Vector2(mouseX  * Conf.PTM * (1/unit.x),(rectUi.height-mouseY) * Conf.PTM * (1/unit.y)), 0);
            }
        }
    }

    private void rendererUnit(ShapeRenderer renderer) {

        renderer.set(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : engine.getEntitiesFor(Family.all(TargetComponent.class).get())) {
            Vector2 position = EcsUtils.getPosition(entity);
            if (position != null) {
                if(EcsUtils.getOwner(entity) == PlayerType.P1){
                    renderer.setColor(Color.BLUE);
                } else if(EcsUtils.getOwner(entity) == PlayerType.P2){
                    renderer.setColor(Color.RED);
                } else {
                    renderer.setColor(Color.YELLOW);
                }
                renderer.rect(position.x * unit.x * Conf.PPM - 2, position.y * unit.y * Conf.PPM - 2, 4, 4);
            }
        }
    }
}
