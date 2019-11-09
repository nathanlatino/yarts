package com.framework.gui.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Vignette {

    private Rectangle bounds;
    private Cell<ImageButton> cell;
    private Entity entity;
    private TextureRegionDrawable imageButton;
    private ActionEntity action;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public Vignette(float x, float y, float w, float h, TextureRegionDrawable tex, Entity entity) {
        this.entity = entity;
        this.imageButton = tex;
        this.action = ActionEntity.NONE;

        bounds = new Rectangle(x, y, w, h);
    }

    public Vignette(float x, float y, float w, float h, TextureRegionDrawable tex, Entity entity, ActionEntity action) {
            this.entity = entity;
            this.imageButton = tex;
            this.action = action;

            bounds = new Rectangle(x, y, w, h);
        }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public Cell<ImageButton> createCell(Table table) {
        ImageButton ib = new ImageButton(imageButton);
        table.add(ib).top().left();
        this.cell = table.getCell(ib);
        return this.cell;
    }

    /*------------------------------*\
    |*			  Getters
    \*------------------------------*/

    public Rectangle getBounds() {
        return bounds;
    }

    public Cell<ImageButton> getCell() {
        return cell;
    }

    public Entity getEntity() {
        return entity;
    }

    public ActionEntity getAction() {
        return action;
    }
}
