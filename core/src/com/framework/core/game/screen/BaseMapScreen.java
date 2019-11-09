package com.framework.core.game.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.framework.core.Conf;
import com.framework.core.ai.pathfinding.PathFinderManager;
import com.framework.core.factories.BodyFactory;
import com.framework.core.factories.ComponentFactory;
import com.framework.core.factories.EntityFactory;
import com.framework.core.game.map.Map;
import com.framework.core.managers.CameraManager;
import com.framework.core.managers.GuiManager;
import com.framework.core.managers.PhysicsManager;
import com.framework.core.notifications.handlers.keyboard.DebugKeysHandler;
import com.framework.core.notifications.handlers.mouse.LeftClickHandler;
import com.framework.core.notifications.handlers.mouse.SelectionRectangleHandler;
import com.framework.core.notifications.listeners.InputListener;
import com.framework.ecs.core.systems.BoundsSystem;
import com.framework.ecs.core.systems.PhysicsSystem;
import com.framework.ecs.gui.systems.ButtonTableSystem;
import com.framework.ecs.gui.systems.CountingSystem;
import com.framework.ecs.gui.systems.SelectionTableSystem;
import com.framework.ecs.meta.systems.BuildingSystem;
import com.framework.ecs.mouse.systems.MultiSelectionSystem;
import com.framework.ecs.mouse.systems.SimpleSelectionSystem;
import com.framework.ecs.optional.systems.LightSystem;
import com.framework.ecs.optional.systems.ParticlesSystem;
import com.framework.ecs.optional.systems.SunSystem;
import com.framework.ecs.rendering.systems.AnimationSystem;
import com.framework.ecs.rendering.systems.OrientationSystem;
import com.framework.ecs.rendering.systems.RenderingSystem;
import com.framework.ecs.specialized.systems.ProjectileSystem;
import com.framework.ecs.specialized.systems.QuantitySystem;
import com.framework.ecs.states.systems.*;
import com.framework.gui.Gui;

public abstract class BaseMapScreen extends BaseScreen {

    protected Map map;
    protected PooledEngine engine;

    protected PathFinderManager pathFinderManager;
    protected SelectionRectangleHandler selectionRectangleHandler;
    protected LeftClickHandler leftClickHandler;

    protected Gui gui;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public BaseMapScreen() {
        engine = new PooledEngine();

        initFactories();
        initMap();
        initListeners();

        leftClickHandler = new LeftClickHandler();
        selectionRectangleHandler = new SelectionRectangleHandler(leftClickHandler);
        new DebugKeysHandler();

        initEngine();
        gui = new Gui(map, engine, selectionRectangleHandler);

        debugInfo();
        map.updateMap(CameraManager.GetInstance().getCamera());
    }

    /*------------------------------------------------------------------*\
    |*							Initialization
    \*------------------------------------------------------------------*/

    private void initFactories() {
        EntityFactory.getInstance().setEngine(engine);
        ComponentFactory.getInstance().setEngine(engine);
        BodyFactory.getInstance().setWorld(PhysicsManager.getWorld());
    }

    private void initMap() {
        map = new Map("map/felicity/felicity-1.0/castle.tmx");
        // map = new Map("map/felicity/felicity-1.0/castle2.tmx");
        // map = new Map("map/felicity/felicity-1.0/castle3.tmx");


        pathFinderManager = PathFinderManager.getInstance();
        PathFinderManager.setMap(map);
    }

    private void initTopDownWorld() {
        float mapCenterX = map.getMapWidth() / (Conf.PPM * 2);
        float mapCenterY = map.getMapHeight() / (Conf.PPM * 2);
        PhysicsManager.getInstance().setTopdownWorld(mapCenterX, mapCenterY, 1, 1);
    }

    private void initListeners() {
        InputListener.getInstance().getClickListener();
        InputListener.getInstance().getKeyboardListener();
    }

    private void initEngine() {

        engine.addSystem(new RenderingSystem(batch, map, pathFinderManager, selectionRectangleHandler));
        engine.addSystem(new QuantitySystem(CameraManager.GetInstance().getCamera()));

        // engine.addSystem(new FogSystem(map, batch));
        engine.addSystem(new PhysicsSystem(PhysicsManager.getWorld()));
        engine.addSystem(new OrientationSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new AnimationSystem());

        engine.addSystem(new PassiveSystem());
        engine.addSystem(new MovingSystem());
        engine.addSystem(new EngagingSystem());
        engine.addSystem(new EngagedSystem());
        engine.addSystem(new DisengagingSystem());
        engine.addSystem(new DeadSystem());

        engine.addSystem(new TargetSystem());
        engine.addSystem(new ProjectileSystem());

        engine.addSystem(new LightSystem());
        engine.addSystem(new ParticlesSystem());
        engine.addSystem(new SunSystem(map));

        engine.addSystem(new BuildingSystem());
        engine.addSystem(new CountingSystem());

        engine.addSystem(new SelectionTableSystem(GuiManager.getInstance(), leftClickHandler));
        engine.addSystem(new ButtonTableSystem(GuiManager.getInstance(), leftClickHandler));
        engine.addSystem(new SimpleSelectionSystem(leftClickHandler, selectionRectangleHandler));
        engine.addSystem(new MultiSelectionSystem(selectionRectangleHandler));
    }

    /*------------------------------------------------------------------*\
    |*							Update methods
    \*------------------------------------------------------------------*/

    @Override
    public void render(float dt) {
        super.render(dt);
        engine.update(dt);


        gui.render(dt);
        GuiManager.getInstance().render(dt);
    }

    /*------------------------------------------------------------------*\
    |*							    Debug
    \*------------------------------------------------------------------*/

    private void debugInfo() {
        System.out.println(Conf.getInfo());
        System.out.println();
        System.out.println(map.getInfo());
        System.out.println();
        System.out.println(DebugKeysHandler.getinfo());
        System.out.println();
    }

    @Override
    public void resize(int width, int height) {
        // CameraManager.GetInstance().getViewport().update(width, height);
        // float scaledWidth = CameraManager.GetInstance().getCamera().viewportWidth / 2;
        // float scaledHeight = CameraManager.GetInstance().getCamera().viewportHeight / 2;
        // CameraManager.GetInstance().getCamera().position.set(scaledWidth, scaledHeight, 0);

    }
}
