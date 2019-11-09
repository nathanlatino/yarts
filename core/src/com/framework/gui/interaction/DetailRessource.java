package com.framework.gui.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.framework.core.Conf;
import com.framework.core.managers.PlayerManager;
import com.framework.gui.BaseUi;

public class DetailRessource extends BaseUi {

    Stage stage;
    TextureRegionDrawable exit_tex;
    Button btnExit;
    Label lbltime, lblRessource, lblUnit;
    Image imgRessource, imgUnit;
    Table root;
    Rectangle rectUi;
    float timer;
    int ressource, nbUnits;
    Time time;

    /*------------------------------------------------------------------*\
    |*							Constructors						  *|
    \*------------------------------------------------------------------*/
    public DetailRessource(Table root, Rectangle rectUi) {
        super();
        time = new Time();
        timer = 0;
        nbUnits = 0;
        ressource = 200;
        this.root = root;
        this.rectUi = rectUi;
        create();
    }

    private void create() {
        lbltime = new Label("0:00", skin);
        lblRessource = new Label("200", skin);
        imgRessource = new Image(new Texture(Gdx.files.internal("gui/icons/money.png")));
        lblUnit = new Label("0/200", skin);
        imgUnit = new Image(new Texture(Gdx.files.internal("gui/icons/unit.png")));
        Table table = new Table();

        // table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/atlasUi/atlasRessource.png")))));

        stage = new Stage();
        exit_tex = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gui/raw/button-close.png"))));
        btnExit = new Button(exit_tex);

        table.add().width(rectUi.width / 2 - lbltime.getWidth() / 2);
        table.add(lbltime);
        table.add().expandX();
        table.add(lblRessource);
        table.add().width(10);
        table.add(imgRessource);
        table.add().width(40);
        table.add(lblUnit);
        table.add().width(10);
        table.add(imgUnit);
        table.add().width(40);
        table.add(btnExit);
        table.add().width(10);

        root.add(table).width(rectUi.width);
        stage.addActor(root);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 						  *|
	\*------------------------------------------------------------------*/

    @Override
    public void render(float delta) {
        timeGestion(delta);
        lblRessource.setText("" + ((int) PlayerManager.getInstance().getGold()));
        lblUnit.setText(PlayerManager.getInstance().getPopulation() + " / " + 200);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();
            if (mouseX > 1895 && mouseY < 20) { //TODO Modify when he is a system
                Gdx.app.exit();
            }
        }
    }

    private void timeGestion(float delta) {
        timer += delta;
        if (timer >= 1) {
            time.addSecond();
            lbltime.setText(time.getTime());
            timer -= 1;
        }
    }

	/*------------------------------------------------------------------*\
	|*							Private Methods 				      *|
	\*------------------------------------------------------------------*/

    private class Time {
        private int second, minute;

        public Time() {
            second = 0;
            minute = 0;
        }

        public void addSecond() {
            if (second < 59) {
                second++;
            } else {
                second = 0;
                minute++;
            }
        }

        public String getTime() {
            StringBuilder sb = new StringBuilder();
            sb.append(minute).append(":");
            sb.append(second < 10 ? "0" : "");
            sb.append(second);
            return sb.toString();
        }
    }
}
