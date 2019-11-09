package com.framework.ecs.rendering.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.framework.core.ai.pathfinding.Graph;
import com.framework.core.ai.pathfinding.Node;
import com.framework.core.ai.pathfinding.PathFinderManager;
import com.framework.core.game.map.Map;
import com.framework.core.managers.CameraManager;
import com.framework.core.managers.PhysicsManager;
import com.framework.core.notifications.commandes.BuildingEvent;
import com.framework.core.notifications.handlers.mouse.SelectionRectangleHandler;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.Mapper;
import com.framework.ecs.core.components.TransformComponent;
import com.framework.ecs.rendering.components.TextureComponent;
import com.framework.enums.EntityType;
import com.framework.utils.FrameRate;
import com.framework.utils.ZComparator;

import java.util.Comparator;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
import static com.framework.core.Conf.*;


public class RenderingSystem extends SortedIteratingSystem {

    private final float tileWidth;
    private final float tileHeight;

    private final int mapRows;
    private final int mapColumns;

    private final Map map;
    private final SpriteBatch batch;
    private final PhysicsManager physicsManager;
    private final PathFinderManager pathFinderManager;
    private final Box2DDebugRenderer b2dr;

    private final ShapeRenderer shapeRenderer;
    private final SelectionRectangleHandler selectionRectangleHandler;

    private final FrameRate frameRate;

    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;

    public ShaderProgram shaderOutline;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public RenderingSystem(SpriteBatch batch, Map map, PathFinderManager pathFinderManager,
                           SelectionRectangleHandler selectionRectangle) {

        super(Family.all(TextureComponent.class).get(), new ZComparator());

        this.renderQueue = new Array();
        this.comparator = new ZComparator();

        this.batch = batch;
        this.physicsManager = PhysicsManager.getInstance();

        this.map = map;
        this.pathFinderManager = pathFinderManager;

        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);

        this.selectionRectangleHandler = selectionRectangle;

        this.b2dr = new Box2DDebugRenderer();
        this.b2dr.setDrawVelocities(true);
        this.b2dr.setDrawJoints(false);

        this.tileWidth = map.getTileWidth() / PPM;
        this.tileHeight = map.getTileHeight() / PPM;

        this.mapRows = map.getRows();
        this.mapColumns = map.getColumns();

        this.frameRate = new FrameRate();

        loadShader();
    }

    /*------------------------------------------------------------------*\
    |*							Update methods
    \*------------------------------------------------------------------*/

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        renderQueue.sort(comparator);
        initializeRendering();
        updateAndRender(dt);
        renderQueue.clear();
    }

    private void updateAndRender(float dt) {

        map.renderMap();
        renderMapDebug();
        renderingLoop();

        updateLights();
        renderRanges();
        udpateBuildingShape(dt);

        renderB2dDebug();
        renderSelectionRectangle();
        renderSelectionShader();

        frameRate.update();
        frameRate.render();

        CameraManager.GetInstance().updateCameras();
        map.updateMap(CameraManager.GetInstance().getCamera());
    }

    private void udpateBuildingShape(float dt) {
        if (BuildingEvent.getInstance().isActive()) {
            BuildingEvent.getInstance().update(batch, dt);
        }
        if (BuildingEvent.getInstance().isConfirmed()) {
            BuildingEvent.getInstance().setActive(false);
        }
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void initializeRendering() {
        // Batch
        batch.setProjectionMatrix(CameraManager.GetInstance().getCamera().combined);
        batch.enableBlending();

        // ShapeRenderer
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        // Shader
        shaderOutline.begin();
        shaderOutline.setUniformf("u_viewportInverse", new Vector2(1f / 124, 1f / 64));
        shaderOutline.setUniformf("u_offset", 0.29f);
        shaderOutline.setUniformf("u_step", Math.min(1f, 40 / 90f));
        shaderOutline.setUniformf("u_color", new Vector3(0f, .5f, 1f));
        shaderOutline.end();
    }

    /*------------------------------*\
    |*			  Rendering
    \*------------------------------*/

    private void renderingLoop() {
        batch.begin();
        for (Entity entity : renderQueue) {
            if (RENDER_ENTITIES) {
                draw(entity);
            }
            if (Mapper.particles_m.has(entity)) {
                Mapper.particles_m.get(entity).getEffect().draw(batch);
            }
        }
        batch.end();
    }

    private void pickColor(Entity entity) {
        if (Mapper.engagingState.has(entity))
            shapeRenderer.setColor(Color.YELLOW);

        else if (Mapper.passiveState.has(entity) || Mapper.movingState.has(entity))
            shapeRenderer.setColor(Color.LIGHT_GRAY);

        else if (Mapper.engagedState.has(entity))
            shapeRenderer.setColor(Color.RED);

    }

    private void renderRanges() {
        if (RENDER_RANGE_DEBUG == 0) return;
        shapeRenderer.begin();
        Gdx.gl20.glLineWidth(1.5f);
        for (Entity entity : renderQueue) {

            if (Mapper.body_m.has(entity)) {
                EntityType type = Mapper.type_m.get(entity).getType();
                if (type == EntityType.SOLDIER || type == EntityType.RANGED || type == EntityType.WORKER) {
                    Vector2 position = EcsUtils.getPosition(entity);
                    shapeRenderer.set(Line);

                    shapeRenderer.setColor(Color.LIGHT_GRAY);
                    if (RENDER_RANGE_DEBUG >= 1 && Mapper.engagedState.has(entity)) {
                        if (EcsUtils.hasTargets(entity)) {
                            Entity target = EcsUtils.getFirstTarget(entity);
                            if (target != null) {
                                Vector2 tPosition = EcsUtils.getPosition(target);
                                if (tPosition != null) {
                                    shapeRenderer.setColor(Color.RED);
                                    shapeRenderer.line(position.x, position.y, tPosition.x, tPosition.y);
                                }
                            }
                        }
                    }
                    if (RENDER_RANGE_DEBUG >= 2) {
                        pickColor(entity);
                        shapeRenderer.circle(position.x, position.y, EcsUtils.getMaxRange(entity), 20);
                    }
                    if (RENDER_RANGE_DEBUG >= 3) {
                        pickColor(entity);
                        shapeRenderer.circle(position.x, position.y, EcsUtils.getMinRange(entity), 20);
                    }
                }
            }
        }
        shapeRenderer.end();
    }

    private void renderSelectionShader() {
        if (!RENDER_SELECTION_SHADER) return;
        batch.begin();
        batch.setShader(shaderOutline);

        for (Entity entity : renderQueue) {
            if (EcsUtils.isSelected(entity)) {
                draw(entity);
            }
        }
        batch.setShader(SpriteBatch.createDefaultShader());
        batch.end();
    }

    private void renderSelectionRectangle() {
        selectionRectangleHandler.render(batch);
    }

    private void renderMapDebug() {
        shapeRenderer.begin();

        // if (RENDER_MAP_DEBUG && DEBUG) {
        //     shapeRenderer.setColor(0.4f, 0.2f, 0.2f, 0.5f);
        //     for (int i = 0; i < mapRows; i++) {
        //         for (int j = 0; j < mapColumns; j++) {
        //             shapeRenderer.set(map.getObstacles()[j][i].isObstacle() ? Filled : Line);
        //             shapeRenderer.rect(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
        //         }
        //     }
        // }

        if (RENDER_PATHFINDER_DEBUG) {
            shapeRenderer.set(Filled);
            shapeRenderer.setColor(Color.ORANGE);
            for (Graph path : pathFinderManager.getDebugPaths()) {
                for (Node node : path) {
                    float x = node.getX() / PPM + tileWidth / 3;
                    float y = node.getY() / PPM + tileHeight / 3;
                    shapeRenderer.rect(x, y, tileWidth / 4, tileHeight / 4);
                }
            }
        }
        shapeRenderer.end();
    }


    private void renderB2dDebug() {
        if (!RENDER_B2D_DEBUG) return;
        b2dr.render(physicsManager.getWorld(), CameraManager.GetInstance().getCamera().combined);

    }

    /*------------------------------*\
    |*			  Updating
    \*------------------------------*/

    private void updateLights() {
        if (!RENDER_LIGHTS) return;
        physicsManager.updateLights();
    }

    /*------------------------------*\
    |*	      Drawing helpers
    \*------------------------------*/

    private void draw(Entity entity) {
        TextureComponent texture = Mapper.texture_m.get(entity);
        TransformComponent position = Mapper.transform_m.get(entity);

        float width = texture.getWidth();
        float height = texture.getHeight();

        float originX = width / 2f;
        float originY = height / 2f;

        float x = position.getX() - originX;
        float y = position.getY() - originY;

        float scaledX = pixelsToMeters(position.getScale().x);
        float scaledY = pixelsToMeters(position.getScale().y);

        float angle = Mapper.animation_m.has(entity) ? 0 : Mapper.body_m.get(entity).getAngle();
        batch.draw(texture.getRegion(), x, y, originX, originY, width, height, scaledX, scaledY, angle);
    }

    /*------------------------------------------------------------------*\
    |*							Shader methods
    \*------------------------------------------------------------------*/

    private void loadShader() {
        String vertexShader;
        String fragmentShader;
        vertexShader = Gdx.files.internal("shader/highlight/df_vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("shader/highlight/outline_border_fragment.glsl").readString();
        shaderOutline = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderOutline.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader: " + shaderOutline.getLog());
    }
}
