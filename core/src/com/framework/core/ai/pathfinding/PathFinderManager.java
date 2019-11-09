package com.framework.core.ai.pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.framework.core.game.map.BaseTileMap;

import static com.framework.core.Conf.PPM;

public class PathFinderManager {

    private static PathFinderManager instance;

    private BaseTileMap map;
    private Node[][] obstacles;
    private Graph graph;
    private Graph out;

    private Array<Graph> debugPaths;


    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private PathFinderManager() {
        this.debugPaths = new Array();
    }

    public static PathFinderManager getInstance() {

        if (instance == null) {
            instance = new PathFinderManager();
        }
        return instance;
    }

    public static PathFinderManager setMap(BaseTileMap map) {
        instance.map = map;
        instance.obstacles = map.getObstacles();
        instance.graph = instance.buildGraph();
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Graph findPath(Vector2 start, Vector2 end) {
        IndexedAStarPathFinder<Node> pathFinder = new IndexedAStarPathFinder<>(graph);

        out = new Graph();

        Vector2 scaledStart = new Vector2((int) (start.x * PPM), (int) (start.y * PPM));
        Vector2 scaledend = new Vector2((int) (end.x * PPM), (int) (end.y * PPM));

        Node startNode = graph.getNodes().get(getIndex(scaledStart.x, scaledStart.y));
        Node endNode = graph.getNodes().get(getIndex(scaledend.x, scaledend.y));

        pathFinder.searchNodePath(startNode, endNode, new GridHeuristic(), out);

        return out;
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods                         *|
    \*------------------------------------------------------------------*/

    private Graph buildGraph() {
        obstacles = map.getObstacles();

        Array<Node> indexedNodes = new Array(map.getColumns() * map.getRows());

        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getColumns(); x++) {
                int weight = getWeight(x, y, obstacles[x][y].neighbours);
                obstacles[x][y].setWeight(weight);

                if (weight > 7) {
                    addAllConnections(x, y);
                } else if (weight > 0) {
                    addOneConnections(x, y, obstacles[x][y].neighbours, weight);
                }


                indexedNodes.add(obstacles[x][y]);
            }
        }
        return new Graph(indexedNodes);
    }

    private void addAllConnections(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                addConnection(x, y, x + i, y + j); //Connection sur lui même autorisé
            }
        }
    }

    private boolean addOneConnections(int x, int y, Array<Neighbour> neighbours, int weight) {
        Neighbour nei = findNeighbour(neighbours, weight);
        return addConnection(x, y, (int) nei.position.x, (int) nei.position.y);
    }

    private Neighbour findNeighbour(Array<Neighbour> neighbours, int weight) {
        int start = 0;
        int end = 7;
        int index = 0;
        boolean startValid = false;

        for (Neighbour nei : neighbours) {
            if (!startValid) {
                if (nei.isValide) {
                    start = index;
                    end = 7;
                    startValid = true;
                }
            } else {
                if (!nei.isValide) {
                    end = index - 1;
                    startValid = false;
                }
            }
            index++;
        }
        int decal = end - start;
        return neighbours.get((decal / 2) + start + (decal % 2));
    }

    private boolean addConnection(int x, int y, int cx, int cy) {
        if (isInMap(cx, cy) && !obstacles[cx][cy].isObstacle()) {
            obstacles[x][y].addConnection(new DefaultConnection(obstacles[x][y], obstacles[cx][cy]));
            return true;
        }
        return false;
    }

    private boolean isInMap(int x, int y) {
        if (x >= 0 && x < map.getColumns()) {
            if (y >= 0 && y < map.getRows()) {
                return true;
            }
        }
        return false;
    }

    private int getIndex(float x, float y) {
        float index = 0;
        float yIndex = 0;
        float xIndex = 0;

        while (yIndex + map.getTileHeight() < y) {
            index += (map.getColumns());
            yIndex += map.getTileHeight();
        }

        while (xIndex + map.getTileWidth() < x) {
            index++;
            xIndex += map.getTileWidth();
        }
        return (int) index;
    }

    private int getWeight(int x, int y, Array<Neighbour> out) {
        int weight = 0;
        int[] values = new int[8];
        values[0] = getWeightOneLink(x - 1, y + 1);
        out.add(new Neighbour(values[0] == 1, new Vector2(x - 1, y + 1)));
        values[1] = getWeightOneLink(x, y + 1);
        out.add(new Neighbour(values[1] == 1, new Vector2(x, y + 1)));
        values[2] = getWeightOneLink(x + 1, y + 1);
        out.add(new Neighbour(values[2] == 1, new Vector2(x + 1, y + 1)));
        values[3] = getWeightOneLink(x + 1, y);
        out.add(new Neighbour(values[3] == 1, new Vector2(x + 1, y)));
        values[4] = getWeightOneLink(x + 1, y - 1);
        out.add(new Neighbour(values[4] == 1, new Vector2(x + 1, y - 1)));
        values[5] = getWeightOneLink(x, y - 1);
        out.add(new Neighbour(values[5] == 1, new Vector2(x, y - 1)));
        values[6] = getWeightOneLink(x - 1, y - 1);
        out.add(new Neighbour(values[6] == 1, new Vector2(x - 1, y - 1)));
        values[7] = getWeightOneLink(x - 1, y);
        out.add(new Neighbour(values[7] == 1, new Vector2(x - 1, y)));

        for (int i = 0; i < 8; i++) {
            weight += values[i];
        }
        return weight;
    }

    private int getWeightOneLink(int cx, int cy) {
        if (isInMap(cx, cy) && !obstacles[cx][cy].isObstacle()) {
            return 1;
        }
        return 0;
    }
    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Array<Graph> getDebugPaths() {
        return debugPaths;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void addDebugPath(Graph debugPath) {
        this.debugPaths.add(debugPath);
    }
}
