package com.framework.ecs.gui.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.framework.core.managers.CameraManager;
import com.framework.core.managers.GuiManager;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.handlers.mouse.LeftClickHandler;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.ecs.states.components.TargetComponent;
import com.framework.gui.utils.Vignette;


public class SelectionTableSystem extends IteratingSystem {

    private final int ROWS = 4;
    private final int COLUMNS = 4;

    private final float VIGNETTE_WIDTH = 36;
    private final float VIGNETTE_HEIGHT = 36;

    private Table table;

    private TextureRegionDrawable spawn_tex;
    private TextureRegionDrawable mineral_tex;
    private TextureRegionDrawable warrior_tex;
    private TextureRegionDrawable worker_tex;
    private TextureRegionDrawable wizard_tex;
    private TextureRegionDrawable archer_tex;
    private TextureRegionDrawable barrack_tex;
    private TextureRegionDrawable undefined_tex;
    private TextureRegionDrawable leftButton_tex;
    private TextureRegionDrawable rightButton_tex;

    private LeftClickHandler leftClickHandler;
    private Rectangle tableBounds;

    private int colCounter;
    private int rowCounter;
    private int nbPage;
    private int nbEntity;

    private float timer;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public SelectionTableSystem(GuiManager guiManager, LeftClickHandler leftClickHandler) {

        super(Family.all(SelectionComponent.class).get());
        this.leftClickHandler = leftClickHandler;

        mineral_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/mineral.png"))));
        warrior_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/warrior.png"))));
        worker_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/worker.png"))));
        wizard_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/wizard.png"))));
        archer_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/archer.png"))));
        undefined_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/undefined.png"))));
        spawn_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/spawn.png"))));
        barrack_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/barrack.png"))));
        leftButton_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/leftButton.png"))));
        rightButton_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/rightButton.png"))));

        table = guiManager.getSelectionTable().getTable();
        tableBounds = guiManager.getSelectionTable().getBounds();
        table.top().left();
        table.pad(8);

        colCounter = 1;
        rowCounter = 1;
        timer = 0;
        nbPage = 0;
        nbEntity = 0;
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        clear();
        timer += dt;
        nbEntity = getEntities().size();
        getEntities().forEach(entity -> processEntity(entity, dt));
        createNavigateButton();
    }

    @Override
    protected void processEntity(Entity entity, float dt) {

        if (Mapper.selection_m.get(entity).isSelected()) {
            Vignette vignette = createVignette(entity);
            if (isClickedVignette(vignette)) {
                timer = 0;
                focusCameraOnSelected(entity);
                unselectAllExcept(entity);
            }
        }
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void createNavigateButton() {
        ImageButton left = new ImageButton(leftButton_tex);
        ImageButton right = new ImageButton(rightButton_tex);
        table.row();
        table.add(left).expandY().bottom();
        table.add(right).expandY().bottom().expandX().right().colspan(COLUMNS - 1);
    }

    private void focusCameraOnSelected(Entity entity) {
        Vector2 position = EcsUtils.getPosition(entity);
        CameraManager.GetInstance().getCamera().position.set(position.x, position.y, 0);
    }

    private void unselectAllExcept(Entity entity) {
        for (Entity e : getEngine().getEntitiesFor(Family.all(SelectionComponent.class, TargetComponent.class).get())) {
            if (e != entity) {
                e.getComponent(SelectionComponent.class).setSelected(false);
            }
        }
    }

    private boolean isClickedVignette(Vignette vignette) {
        MouseEvent e = leftClickHandler.getEvent();
        return e != null && vignette.getBounds().contains(e.getPosition()) && timer > .5f;
    }

    private Vignette createVignette(Entity entity) {
        Vignette vignette;
        float x = tableBounds.x + VIGNETTE_WIDTH * (colCounter - 1);
        float y = tableBounds.y + VIGNETTE_HEIGHT * (rowCounter - 1);


        switch (EcsUtils.getClass(entity)) {
            case WARRIOR:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, warrior_tex, entity);
                break;
            case ARCHER:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, archer_tex, entity);
                break;
            case WIZARD:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, wizard_tex, entity);
                break;
            case WORKER:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, worker_tex, entity);
                break;
            case MINERAL:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, mineral_tex, entity);
                break;
            case SPAWN:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, spawn_tex, entity);
                break;
            case BARRACK:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, barrack_tex, entity);
                break;
            default:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, undefined_tex, entity);
        }

        vignette.createCell(table);
        increment();
        return vignette;
    }

    private void increment() {
        if (colCounter % COLUMNS == 0) {
            table.row();
            rowCounter++;
            if (rowCounter % ROWS == 0) {
                nbPage++;
                rowCounter = 0;
            }
            colCounter = 0;
        }
        colCounter++;
    }

    private void clear() {
        colCounter = 1;
        rowCounter = 1;
        table.clear();
    }
}
