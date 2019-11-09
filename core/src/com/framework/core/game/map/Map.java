package com.framework.core.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.framework.core.Conf;
import com.framework.core.ai.pathfinding.Node;
import com.framework.core.factories.EntityFactory;
import com.framework.core.managers.PhysicsManager;
import com.framework.enums.PlayerType;
import com.yarts.entities.BuildingFactory;
import com.yarts.entities.DecorationsFactory;
import com.yarts.entities.NeutralFactory;
import com.yarts.entities.UnitsFactory;

import java.util.Iterator;

import static com.framework.core.Conf.PPM;
import static com.framework.core.Conf.PTM;

public class Map implements BaseTileMap {

    protected final float tileWidth;
    protected final float tileHeight;

    // Map dimensions (obstacles)
    protected final int columns;
    protected final int rows;
    // private final OrthographicCamera camera;

    // tiledmap utils
    protected TiledMap tiledMap;
    protected MultiPolyOrthognalTiled tiledMapRenderer;
    // Used to build Solid entities
    protected EntityFactory entityFactory;
    protected DecorationsFactory decorationFactory;
    protected NeutralFactory neutralFactory;
    protected BuildingFactory buildingFactory;
    protected UnitsFactory unitsFactory;

    // Graph reprensenation
    protected Node[][] obstacles;

    // Solid coord list
    public Array<Node> solidList;

    /*------------------------------------------------------------------*\
   	|*							Initialization
   	\*------------------------------------------------------------------*/

    /**
     * Initialize Tilemap created with the Tiled CameraActor Editor.
     */
    public Map(String filename) {

        this.tiledMap = new TmxMapLoader().load(filename);
        this.entityFactory = EntityFactory.getInstance();
        this.decorationFactory = DecorationsFactory.getInstance();
        this.neutralFactory = NeutralFactory.getInstance();
        this.buildingFactory = BuildingFactory.getInstance();
        this.unitsFactory = UnitsFactory.getInstance();

        this.tileWidth = (int) tiledMap.getProperties().get("tilewidth");
        this.tileHeight = (int) tiledMap.getProperties().get("tileheight");

        this.columns = (int) tiledMap.getProperties().get("width");
        this.rows = (int) tiledMap.getProperties().get("height");

        this.tiledMapRenderer = new MultiPolyOrthognalTiled(tiledMap, PTM);

        this.solidList = new Array();

        initTopDownWorld();

        buildObstaclesList();
        extractSun();

        extractItem("Solid");
        extractItem("Torch");
        extractItem("Pillard");
        extractItem("Ressource");
        extractItem("SpawnP1");
        extractItem("SpawnP2");
        extractItem("WorkerP1");
        extractItem("WorkerP2");
        extractItem("ArcherP1");
        extractItem("ArcherP2");
        extractItem("WarriorP1");
        extractItem("WarriorP2");
        extractItem("WizardP1");
        extractItem("WizardP2");
    }


    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void buildObstaclesList() {
        obstacles = new Node[columns][rows];
        int index = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++, index++) {
                obstacles[x][y] = new Node(index, x * tileWidth, y * tileHeight, false);
            }
        }
    }

    private void extractItem(String name) {
        for (MapObject obj : getRectangleList(name)) {
            MapProperties props = obj.getProperties();

            float x = (float) props.get("x");
            float y = (float) props.get("y");
            float w = (float) props.get("width");
            float h = (float) props.get("height");

            int obstacleX = (int) (x / tileWidth);
            int obstacleY = (int) (y / tileHeight);

            int nBlocsX = (int) (w / tileWidth);
            int nBlocsY = (int) (h / tileHeight);

            x = x + w / 2;
            y = y + h / 2;

            boolean isSolid = true;

            switch (name) {
                case "Solid":
                    entityFactory.createEmptyRectangle(x, y, w, h);
                    break;
                case "Torch":
                    decorationFactory.createTorch(x, y);
                    break;
                case "Pillard":
                    decorationFactory.createPillard(x, y);
                    break;
                case "Ressource":
                    neutralFactory.createMineral(x, y, 10000);
                    break;

                case "SpawnP1":
                    buildingFactory.createSpawn(x, y, PlayerType.P1);
                    break;
                case "SpawnP2":
                    buildingFactory.createSpawn(x, y, PlayerType.P2);
                    break;

                case "WorkerP1":
                    unitsFactory.createWorker(x, y, PlayerType.P1);
                    isSolid = false;
                    break;
                case "WorkerP2":
                    unitsFactory.createWorker(x, y, PlayerType.P2);
                    isSolid = false;
                    break;

                case "ArcherP1":
                    unitsFactory.createArcher(x, y, PlayerType.P1);
                    isSolid = false;
                    break;
                case "ArcherP2":
                    unitsFactory.createArcher(x, y, PlayerType.P2);
                    isSolid = false;
                    break;

                case "WarriorP1":
                    unitsFactory.createWarrior(x, y, PlayerType.P1);
                    isSolid = false;
                    break;
                case "WarriorP2":
                    unitsFactory.createWarrior(x, y, PlayerType.P2);
                    isSolid = false;
                    break;

                case "WizardP1":
                    unitsFactory.createWizard(x, y, PlayerType.P1);
                    isSolid = false;
                    break;
                case "WizardP2":
                    unitsFactory.createWizard(x, y, PlayerType.P2);
                    isSolid = false;
                    break;

                case "Sun":
                    decorationFactory.createSun(x, y);
                    isSolid = false;
                    break;

            }

            if (isSolid) {
                for (int i = 0; i < nBlocsX; i++) {
                    for (int j = 0; j < nBlocsY; j++) {

                        obstacles[obstacleX + i][obstacleY + j].setObstacle(true);
                    }
                }
            }
        }
    }

    private void extractSolid() {
        for (MapObject obj : getRectangleList("Solid")) {
            MapProperties props = obj.getProperties();

            float x = (float) props.get("x");
            float y = (float) props.get("y");
            float w = (float) props.get("width");
            float h = (float) props.get("height");

            int obstacleX = (int) (x / tileWidth);
            int obstacleY = (int) (y / tileHeight);

            int nBlocsX = (int) (w / tileWidth);
            int nBlocsY = (int) (h / tileHeight);

            entityFactory.createEmptyRectangle(x + w / 2, y + h / 2, w, h);
            for (int i = 0; i < nBlocsX; i++) {
                for (int j = 0; j < nBlocsY; j++) {

                    obstacles[obstacleX + i][obstacleY + j].setObstacle(true);
                }
            }

            // IntStream.range(0, nBlocsX).forEach(i -> obstacles[obstacleX + i][obstacleY].setObstacle(true));
            // IntStream.range(0, nBlocsY).forEach(i -> obstacles[obstacleX][obstacleY + i].setObstacle(true));
        }
    }

    private void extractTorches() {
        for (MapObject obj : getRectangleList("Torch")) {
            MapProperties props = obj.getProperties();

            float x = (float) props.get("x");
            float y = (float) props.get("y");
            float w = (float) props.get("width");
            float h = (float) props.get("height");

            int obstacleX = (int) (x / tileWidth);
            int obstacleY = (int) (y / tileHeight);

            int nBlocsX = (int) (w / tileWidth);
            int nBlocsY = (int) (h / tileHeight);

            // entityFactory.makeSolidFromTiled(x + w / 2, y + h / 2, w, h);
            DecorationsFactory.getInstance().createTorch(x + w / 2, y + h / 2);

            entityFactory.createEmptyRectangle(x + w / 2, y + h / 2, w, h);
            for (int i = 0; i < nBlocsX; i++) {
                for (int j = 0; j < nBlocsY; j++) {

                    obstacles[obstacleX + i][obstacleY + j].setObstacle(true);
                }
            }

        }
    }

    private void extractPillards() {
        for (MapObject obj : getRectangleList("Torch")) {
            MapProperties props = obj.getProperties();

            float x = (float) props.get("x");
            float y = (float) props.get("y");
            float w = (float) props.get("width");
            float h = (float) props.get("height");

            int obstacleX = (int) (x / tileWidth);
            int obstacleY = (int) (y / tileHeight);

            int nBlocsX = (int) (w / tileWidth);
            int nBlocsY = (int) (h / tileHeight);

            // entityFactory.makeSolidFromTiled(x + w / 2, y + h / 2, w, h);
            DecorationsFactory.getInstance().createTorch(x + w / 2, y + h / 2);


            entityFactory.createEmptyRectangle(x + w / 2, y + h / 2, w, h);
            for (int i = 0; i < nBlocsX; i++) {
                for (int j = 0; j < nBlocsY; j++) {

                    obstacles[obstacleX + i][obstacleY + j].setObstacle(true);
                }
            }
        }
    }

    private void extractSun() {
        for (MapObject obj : getRectangleList("Sun")) {
            MapProperties props = obj.getProperties();
            float x = (float) props.get("x");
            float y = (float) props.get("y");
            DecorationsFactory.getInstance().createSun(x, y);
        }
    }

    private void initTopDownWorld() {
        float mapCenterX = getMapWidth() / (Conf.PPM * 2);
        float mapCenterY = getMapHeight() / (Conf.PPM * 2);
        PhysicsManager.getInstance().setTopdownWorld(mapCenterX, mapCenterY, 1, 1);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public void updateMap(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        // tiledMapRenderer.render();
    }

    public void renderMap() {
        tiledMapRenderer.render();
    }

    /**
     * Search the map layers for RectangleActor Objects that contain a property (key) called "name" with associated value propertyName.
     * Typically used to store non-actor information such as SpawnPoint locations or dimensions of SolidActor objects.
     * Retrieve data as object, then attack to desired type: for example, float w = (float)obj.getProperties().get("width").
     */
    public Array<MapObject> getRectangleList(String propertyName) {
        Array<MapObject> list = new Array<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof RectangleMapObject)) {
                    continue;
                }
                MapProperties props = obj.getProperties();

                if (props.containsKey("name") && props.get("name").equals(propertyName)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }

    public Array<MapObject> getEllipseList(String propertyName) {
        Array<MapObject> list = new Array<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof EllipseMapObject)) {
                    continue;
                }
                MapProperties props = obj.getProperties();

                if (props.containsKey("name") && props.get("name").equals(propertyName)) {
                    list.add(obj);
                }
            }
        }
        return list;
    }


    public Array<MapObject> getTileByPropName(String propertyName) {
        Array<MapObject> list = new Array<>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof TiledMapTileMapObject)) {
                    continue;
                }
                MapProperties props = obj.getProperties();

                // Default MapProperties are stored within associated Node object
                // Instance-specific overrides are stored in MapObject
                TiledMapTileMapObject tmtmo = (TiledMapTileMapObject) obj;
                TiledMapTile t = tmtmo.getTile();
                MapProperties defaultProps = t.getProperties();

                if (defaultProps.containsKey("name") && defaultProps.get("name").equals(propertyName)) {
                    list.add(obj);
                }

                // get list of default property keys
                Iterator<String> propertyKeys = defaultProps.getKeys();

                // iterate over keys; copy default values into props if needed
                while (propertyKeys.hasNext()) {
                    String key = propertyKeys.next();

                    // check if value already exists; if not, create property with default value
                    if (props.containsKey(key)) {
                        continue;
                    }
                    else {
                        Object value = defaultProps.get(key);
                        props.put(key, value);
                    }
                }
            }
        }
        return list;
    }

    /*------------------------------------------------------------------*\
    |*							    Debug
    \*------------------------------------------------------------------*/

    public String getInfo() {
        StringBuilder sb = new StringBuilder("Map info:");
        sb.append("\n---------");
        sb.append("\nA tile: ").append(tileWidth).append(" x ").append(tileHeight);
        sb.append("\nTiles: ").append(columns).append(" x ").append(rows);
        sb.append("\nMap (pixel): ").append(getMapWidth()).append(" x ").append(getMapHeight());
        sb.append("\nMap (meters): ").append(getMapWidth() / PPM).append(" x ").append(getMapHeight() / PPM);
        return sb.toString();
    }

    /*------------------------------*\
   	|*				Getters
   	\*------------------------------*/

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    @Override
    public float getTileWidth() {
        return tileWidth;
    }

    @Override
    public float getTileHeight() {
        return tileHeight;
    }

    @Override
    public float getMapWidth() {
        return tileWidth * getColumns();
    }

    @Override
    public float getMapHeight() {
        return tileWidth * getRows();
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public Node[][] getObstacles() {
        return obstacles;
    }

    public MultiPolyOrthognalTiled getTiledMapRenderer() {
        return tiledMapRenderer;
    }
}
