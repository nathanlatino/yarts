package com.framework.core.ai.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Graph implements IndexedGraph<Node>, SmoothableGraphPath<Node, Vector2> {

    private Array<Node> nodes;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public Graph() {
        nodes = new Array();
    }

    public Graph(Array<Node> nodes) {
        this.nodes = nodes;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Array<Node> getNodes() {
        return nodes;
    }

    @Override
    public int getIndex(Node node) {
        return node.getIdx();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node node) {
        return node.getConnections();
    }

    public Array<Vector2> getPath() {
        Array<Vector2> vectorList = new Array();
        nodes.forEach(i -> vectorList.add(i.getLocation()));
        return vectorList;
    }

    /*------------------------------------------------------------------*\
    |*							Smoothable
    \*------------------------------------------------------------------*/

    @Override
    public Vector2 getNodePosition(int index) {
        return nodes.get(index).getLocation();
    }

    @Override
    public void swapNodes(int index1, int index2) {
        nodes.swap(index1, index2);

    }

    @Override
    public void truncatePath(int newLength) {
        nodes.setSize(newLength);
    }

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    @Override
    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    @Override
    public Iterator<Node> iterator() {
        return new Array.ArrayIterator(nodes);
    }
}
