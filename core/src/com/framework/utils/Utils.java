package com.framework.utils;

import com.badlogic.gdx.math.Vector2;

public class Utils {

    public static float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    public static Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    public static Vector2 vectorBetween(Vector2 a, Vector2 b) {
        float length = a.dst(b);
        float x = b.x - a.x;
        float y = b.y - a.y;
        return new Vector2(x / length, y / length);
    }

}
