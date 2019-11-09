package com.framework.core.ai.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.framework.core.Conf.PPM;

public class Node {

    private int idx;
    private float x;
    private float y;
    public Array<Neighbour> neighbours;
    private boolean obstacle;

    private int weight;

    public Array<Connection<Node>> connetions;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public Node(int idx, float x, float y, boolean obstacle) {
        this.idx = idx;
        this.x = x;
        this.y = y;
        this.obstacle = obstacle;
        this.weight = 1;
        this.connetions = new Array();
        this.neighbours = new Array<>();
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("x: ").append(x).append(", y: ").append(y);
        sb.append(", idx: ").append(idx).append(", solid: ").append(obstacle);
        return sb.append("}").toString();
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public int getIdx() {
        return idx;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public Array<Connection<Node>> getConnections() {
        return connetions;
    }

    public Vector2 getLocation() {
        return new Vector2((x + 16) / PPM, (y + 16) / PPM);
    }

    public int getWeight() {
        return weight;
    }

    /*------------------------------*\
    |*			  Setters
    \*------------------------------*/

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    public void addConnection(Connection<Node> connection) {
        connetions.add(connection);
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
