
package com.framework.core.ai.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class GridHeuristic implements Heuristic<Node> {

    public static float D = 1;
    public static String function = "diagonal";

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

    public GridHeuristic() {

    }

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

    @Override
    public float estimate(Node start, Node end) {
        if (function.equals("manhattan")) {
            return manhattanDistance(start, end);
        } else if (function.equals("diagonal")) {
            return diagonalDistance(start, end);
        } else if (function.equals("euclidean")) {
            return euclideanDistance(start, end);
        } else {
            return manhattanDistance(start, end);
        }
    }


    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/


    private float manhattanDistance(Node start, Node end) {
        float dx = Math.abs(start.getX() - end.getX());
        float dy = Math.abs(start.getY() - end.getY());
        float f = (dx + dy);
        return D * f;
    }

    private float diagonalDistance(Node start, Node end) {
        // float D2 = (float) Math.sqrt(D);
        float D2 = (float) Math.sqrt(2);
        float dx = Math.abs(start.getX() - end.getX());
        float dy = Math.abs(start.getY() - end.getY());
        return D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);
    }

    private float euclideanDistance(Node start, Node end) {
        float dx = start.getX() - end.getX();
        float dy = start.getY() - end.getY();
        float f = (float) Math.sqrt(dy * dy + dx * dx);
        return D * f;
    }

}
