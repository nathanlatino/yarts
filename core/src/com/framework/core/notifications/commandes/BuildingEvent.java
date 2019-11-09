package com.framework.core.notifications.commandes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.framework.core.Conf;
import com.framework.core.factories.StateFactory;
import com.framework.enums.PlayerType;
import com.yarts.entities.BuildingFactory;


public class BuildingEvent {

    private final float width = 48 / Conf.PPM;
    private final float height = 48 / Conf.PPM;

    private static BuildingEvent instance;
    private TextureRegion shape;
    private Entity entity;
    private float timer;
    private boolean active;
    private boolean confirmed;

    private Vector2 oldPos;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private BuildingEvent() {
        shape = new TextureRegion(new Texture(Gdx.files.internal("textures/buildings/small_building.png")));
        // shape = RectangleShape.getInstance().getRectangle(0, 0, (int) width, (int) height, new Color(0xff341caa));
        active = false;
        timer = 0;
        confirmed = false;
        oldPos = new Vector2(0f, 0f);
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public static BuildingEvent getInstance() {
        if (instance == null) {
            instance = new BuildingEvent();
        }
        return instance;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void update(SpriteBatch batch, float dt) {
        timer += dt;

        if (Gdx.input.isButtonPressed(0) && timer > .5) {
            active = false;
        }


        if (!active) {
            StateFactory.getInstance().setMovingState(entity, oldPos);
            confirmed = true;
        }

        else {
            Vector2 newPosition = MouseEvent.unprojectedMousePosition();
            float deltaX = Math.abs(oldPos.x - newPosition.x);
            float deltaY = Math.abs(oldPos.y - newPosition.y);

            if (deltaX > .5 || deltaY > .5) {
                oldPos = newPosition;
            }
            batch.begin();
            batch.draw(shape, oldPos.x - width / 3, oldPos.y - height / 3, shape.getRegionWidth() /Conf.PPM, shape.getRegionHeight()/Conf.PPM);
            batch.end();
        }
    }

    public void build() {
        BuildingFactory.getInstance().createBarrack(oldPos.x * Conf.PPM, oldPos.y * Conf.PPM, PlayerType.P1);
        reset();
    }

    public void reset() {
        StateFactory.getInstance().setPassiveState(entity);
        active = false;
        timer = 0;
        confirmed = false;
        oldPos = new Vector2(0f, 0f);
        entity = null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.timer = 0;
    }

    public void setConfirmed(boolean value) {
        this.confirmed = value;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Vector2 getOldPos() {
        return oldPos;
    }
}
