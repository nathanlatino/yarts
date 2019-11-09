package com.framework.gui.interaction;

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

public class PanelButton extends BaseUi {
    ArrayList<ImageButton> buttons;
    Table root;
    Rectangle rectUi;
    Vector2 size;

    /*------------------------------------------------------------------*\
    |*							Constructors						  *|
    \*------------------------------------------------------------------*/
    public PanelButton(Table root, Rectangle rectUi) {
        super();
        buttons = new ArrayList<>();
        this.root = root;
        this.rectUi = rectUi;
        size = new Vector2(150,150);
        create();
    }

    private void create() {

        Table table = new Table();


        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/atlasUi/panelButton.png")))));

        for (int i = 1; i < 17; i++) {

            ImageButton button = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/icons/warrior.png")))));



            button.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    System.out.println("yes");
                }
            });
            buttons.add(button);
            table.add(button);
            if (i % 4 == 0) {
                table.row();
            }
        }

        root.add(table).maxSize(size.x,size.y).expand().bottom().right().fill();
    }

    public Vector2 getSize() {
        return size;
    }
}
