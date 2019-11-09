package com.framework.ecs.gui.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.framework.core.Conf;
import com.framework.core.managers.CameraManager;
import com.framework.core.managers.GuiManager;
import com.framework.core.notifications.commandes.BuildingEvent;
import com.framework.core.notifications.commandes.MouseEvent;
import com.framework.core.notifications.handlers.mouse.LeftClickHandler;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.enums.PlayerType;
import com.framework.gui.utils.ActionEntity;
import com.framework.gui.utils.Vignette;
import com.yarts.entities.UnitsFactory;


public class ButtonTableSystem extends IteratingSystem {

    private final float BUTTON_WIDTH = 40;
    private final float BUTTON_HEIGHT = 40;

    private final float VIGNETTE_WIDTH = 120;
    private final float VIGNETTE_HEIGHT = 150;

    private Table table;

    private LeftClickHandler leftClickHandler;

    private TextureRegionDrawable mineral_tex;
    private TextureRegionDrawable undefined_tex;

    private TextureRegionDrawable warrior_tex;
    private TextureRegionDrawable wizard_tex;
    private TextureRegionDrawable archer_tex;
    private TextureRegionDrawable spawn_tex;
    private TextureRegionDrawable worker_tex;
    private TextureRegionDrawable barack_tex;


    private TextureRegionDrawable action_warrior_tex;
    private TextureRegionDrawable action_wizard_tex;
    private TextureRegionDrawable action_archer_tex;
    private TextureRegionDrawable action_worker_tex;
    private TextureRegionDrawable action_spawn_tex;
    private TextureRegionDrawable action_barack_tex;

    private Rectangle tableBounds;

    private float timer;

    /*------------------------------------------------------------------*\
   	|*							Constructors						  *|
   	\*------------------------------------------------------------------*/

    public ButtonTableSystem(GuiManager guiManager, LeftClickHandler leftClickHandler) {
        super(Family.all(SelectionComponent.class).get());
        this.leftClickHandler = leftClickHandler;

        mineral_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigMineral.png"))));
        undefined_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigUndefined.png"))));

        warrior_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigWarrior.png"))));
        wizard_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigWizard.png"))));
        archer_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigArcher.png"))));
        worker_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigWorker.png"))));
        spawn_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigSpawn.png"))));
        barack_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/portrait/bigBarrack.png"))));

        action_spawn_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/button/spawn.png"))));
        action_barack_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/button/barrack.png"))));
        action_warrior_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/warrior.png"))));
        action_wizard_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/wizard.png"))));
        action_archer_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/archer.png"))));
        action_worker_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/icon/worker.png"))));


        table = guiManager.getButtonTable().getTable();
        tableBounds = guiManager.getButtonTable().getBounds();
        table.top().left().pad(7);
        timer = 0;
    }

    /*------------------------------------------------------------------*\
  	|*							Update Methods 						  *|
  	\*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        table.clear();
        timer += dt;
        Entity firstSelect = firstSelected();
        processEntity(firstSelect, dt);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if (entity != null) {
            Vignette vignette = createBigVignette(entity);

            Array<Vignette> vignettes = getButtonAction(entity);
            if (isClickedVignette(vignette)) {
                timer = 0;
                processingPortrait(vignette);
            }

            for (Vignette action : vignettes) {
                if (isClickedVignette(action)) {
                    timer = 0;
                    processingAction(action);
                }
            }
            showHealth(entity);
        }
    }

    /*------------------------------------------------------------------*\
   	|*							Traitements        				      *|
   	\*------------------------------------------------------------------*/

    private void processingPortrait(Vignette vignette) {
        Entity entity = vignette.getEntity();
        focusCameraOnSelected(entity);
    }

    private void processingAction(Vignette vignette) {
        Entity entity = vignette.getEntity();
        Vector2 unprojected = new Vector2(EcsUtils.getPosition(entity).x * Conf.PPM, EcsUtils.getPosition(entity).y * Conf.PPM);

        switch (vignette.getAction()) {
            case BARRACK:
                BuildingEvent.getInstance().setActive(true);
                BuildingEvent.getInstance().setEntity(entity);
                break;
            case SPAWN:
                System.out.println("create spawn");
                break;
            case WORKER:
                UnitsFactory.getInstance().createWorker(unprojected.x, unprojected.y, PlayerType.P1);
                System.out.println("create worker");
                break;
            case WIZARD:
                UnitsFactory.getInstance().createWizard(unprojected.x, unprojected.y, PlayerType.P1);

                System.out.println("create wizard");
                break;
            case SOLDIER:
                UnitsFactory.getInstance().createWarrior(unprojected.x, unprojected.y, PlayerType.P1);
                System.out.println("create Soldier");
                break;
            case ARCHER:
                UnitsFactory.getInstance().createArcher(unprojected.x, unprojected.y, PlayerType.P1);

                System.out.println("create archer");
                break;
        }

    }
    /*------------------------------------------------------------------*\
   	|*							Private Methods 				      *|
   	\*------------------------------------------------------------------*/

    private boolean isClickedVignette(Vignette vignette) {
        MouseEvent e = leftClickHandler.getEvent();
        return e != null && vignette.getBounds().contains(e.getPosition()) && timer > .5f;
    }


    private Entity firstSelected() {
        for (Entity entity : getEntities()) {
            if (Mapper.selection_m.get(entity).isSelected()) return entity;
        }
        return null;
    }

    private Vignette createBigVignette(Entity entity) {
        Vignette vignette;
        float x = tableBounds.x;
        float y = tableBounds.y;

        switch (EcsUtils.getClass(entity)) {
            case WARRIOR:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, warrior_tex, entity);
                break;
            case WIZARD:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, wizard_tex, entity);
                break;
            case ARCHER:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, archer_tex, entity);
                break;
            case MINERAL:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, mineral_tex, entity);
                break;
            case WORKER:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, worker_tex, entity);
                break;
            case SPAWN:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, spawn_tex, entity);
                break;
            case BARRACK:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, barack_tex, entity);
                break;
            default:
                vignette = new Vignette(x, y, VIGNETTE_WIDTH, VIGNETTE_HEIGHT, undefined_tex, entity);
        }

        vignette.createCell(table);
        return vignette;
    }

    private Array<Vignette> getButtonAction(Entity entity) {
        Array<Vignette> vignettes = new Array();
        float x = tableBounds.x + VIGNETTE_WIDTH;
        float y = tableBounds.y;

        switch (EcsUtils.getClass(entity)) {
            case WORKER:
                vignettes.add(new Vignette(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, action_spawn_tex, entity, ActionEntity.SPAWN));
                vignettes.add(new Vignette(x + BUTTON_WIDTH, y, BUTTON_WIDTH, BUTTON_HEIGHT, action_barack_tex, entity, ActionEntity.BARRACK));
                break;
            case SPAWN:
                vignettes.add(new Vignette(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, action_worker_tex, entity, ActionEntity.WORKER));
                break;
            case BARRACK:
                vignettes.add(new Vignette(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, action_warrior_tex, entity, ActionEntity.SOLDIER));
                vignettes.add(new Vignette(x + BUTTON_WIDTH, y, BUTTON_WIDTH, BUTTON_HEIGHT, action_archer_tex, entity, ActionEntity.ARCHER));
                vignettes.add(new Vignette(x + BUTTON_WIDTH * 2, y, BUTTON_WIDTH, BUTTON_HEIGHT, action_wizard_tex, entity, ActionEntity.WIZARD));
                break;
        }

        for (Vignette vignette : vignettes) {
            vignette.createCell(table);
        }
        return vignettes;
    }

    private void focusCameraOnSelected(Entity entity) {
        Vector2 position = EcsUtils.getPosition(entity);
        CameraManager.GetInstance().getCamera().position.set(position.x, position.y, 0);
    }

    private void showHealth(Entity entity) {
        if (Mapper.health_m.has(entity)) {
            float health = Mapper.health_m.get(entity).getHealth();
            float maxHealth = Mapper.health_m.get(entity).getMaxHealth();

            Label healthBar = new Label("HP: " + (int) health + " / " + (int) maxHealth, new Skin(Gdx.files.internal("gui/uiskin.json")));
            table.row();
            table.add(healthBar);
        }
        if (Mapper.consomable_m.has(entity)) {
            float quantity = Mapper.consomable_m.get(entity).getQuantity();
            float maxQuantity = Mapper.consomable_m.get(entity).getMaxQuantity();

            Label healthBar = new Label((int) quantity + " / " + (int) maxQuantity, new Skin(Gdx.files.internal("gui/uiskin.json")));
            table.row();
            table.add(healthBar);
        }
    }
}

