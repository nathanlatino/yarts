package com.framework.gui.interaction;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.framework.gui.BaseUi;

import java.util.ArrayList;

public class DetailSelectUnit extends BaseUi {
    Table root;
    Rectangle rectUi;
    Vector2 size;
    Engine engine;

    /*------------------------------------------------------------------*\
    |*							Constructors						  *|
    \*------------------------------------------------------------------*/
    public DetailSelectUnit(Table root, Rectangle rectUi, Engine engine) {
        super();
        this.engine = engine;
        this.root = root;
        this.rectUi = rectUi;
        size = new Vector2(150, 150);
        create();
    }

    private void create() {

        Table table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/atlasUi/panelButton.png")))));


        root.add(table).maxSize(size.x, size.y).expand().bottom();
    }
}
