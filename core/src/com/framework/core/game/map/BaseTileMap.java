package com.framework.core.game.map;

import com.framework.core.ai.pathfinding.Node;

public interface BaseTileMap {
    float getTileWidth();

    float getTileHeight();

    float getMapWidth();

    float getMapHeight();

    int getColumns();

    int getRows();

    Node[][] getObstacles();
}
