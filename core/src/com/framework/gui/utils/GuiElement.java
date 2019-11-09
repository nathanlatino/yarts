package com.framework.gui.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GuiElement {

    private Table table;
    private String name;
    private Rectangle bounds;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public GuiElement(float x, float y, float width, float height, String name, String fName) {
        this.name = name;
        table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/atlasUi/" + fName + ".png")))));
        bounds = new Rectangle(x, y, width, height);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public float getX() {
        return table.getX();
    }

    public float getY() {
        return table.getY();
    }

    public float getWidth() {
        return table.getWidth();
    }

    public float getHeight() {
        return table.getHeight();
    }

    public Table getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
