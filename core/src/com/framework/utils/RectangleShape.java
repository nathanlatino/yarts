package com.framework.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class RectangleShape {

    private static RectangleShape instance;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private RectangleShape() {
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public static RectangleShape getInstance() {
        if (instance == null) {
            instance = new RectangleShape();
        }
        return instance;
    }

    public Texture getRectangle(int x, int y, int w, int h) {
        Pixmap rectanglePixmap = createProceduralPixmap(x, y, w, h);
        return makeTextureFromPixmap(rectanglePixmap);
    }

    public Texture getRectangle(int x, int y, int w, int h, Color color) {
        Pixmap rectanglePixmap = createProceduralPixmap(x, y, w, h, color);
        return makeTextureFromPixmap(rectanglePixmap);
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    public Sprite makeSpriteFromTexture(Texture texture, int width, int height) {
        Sprite sprite = new Sprite(texture);
        sprite.setOrigin(width, height);
        return sprite;
    }

    public Texture makeTextureFromPixmap(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return texture;
    }

    public Pixmap createProceduralPixmap(int x, int y, int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // pixmap.setColor(new Color(0, 0, 0.5f, .5f));
        pixmap.setColor(color);
        pixmap.fillRectangle(x, y, width, height);
        return pixmap;
    }

    public Pixmap createProceduralPixmap(int x, int y, int width, int height) {
        return createProceduralPixmap(x, y, width, height,  new Color(0x708090aa));
    }

}
