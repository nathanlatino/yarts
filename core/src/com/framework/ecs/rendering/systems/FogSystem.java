package com.framework.ecs.rendering.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.framework.core.Conf;
import com.framework.core.game.map.Map;
import com.framework.core.managers.CameraManager;
import com.framework.ecs.EcsUtils;
import com.framework.ecs.rendering.components.FogComponent;

import static com.framework.core.Conf.*;

public class FogSystem extends IteratingSystem {

    final String FRAGMENT =
            //GL ES specific stuff
            "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" + //
                    "varying LOWP vec4 vColor;\n" + "varying vec2 vTexCoord;\n" + "uniform sampler2D u_texture;\n" + "uniform sampler2D u_texture1;\n" + "uniform sampler2D u_mask;\n" + "void main(void) {\n" + "	//sample the colour from the first texture\n"
                    + "	vec4 texColor0 = texture2D(u_texture, vTexCoord);\n" + "\n" + "	//sample the colour from the second texture\n" + "	vec4 texColor1 = texture2D(u_texture1, vTexCoord);\n" + "\n" + "	//get the mask; we will only use the alpha channel\n"
                    + "	float mask = texture2D(u_mask, vTexCoord).a;\n" + "\n" + "	//interpolate the colours based on the mask\n" + "	gl_FragColor = vColor * mix(texColor0, texColor1, mask);\n" + "}";


    final String VERTEX = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
            "uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n" + "varying vec2 vTexCoord;\n" +
            "void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" + "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";


    private final int width;
    private final int height;

    Pixmap fogMask;
    Texture foreground;

    public Texture fboTexture;

    SpriteBatch batch;
    ShaderProgram shaderFog;

    Map map;

    private FrameBuffer fbo;


    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    public FogSystem(Map map, SpriteBatch batch) {
        super(Family.all(FogComponent.class).get());
        this.map = map;
        this.batch = batch;

        this.width = (int) Conf.WORLD_WIDTH;
        this.height = (int) Conf.WORLD_HEIGHT;
        this.fogMask = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        this.fogMask.setColor(new Color(0x00000000));
        this.fogMask.fillRectangle(0, 0, width, height);

        Pixmap forg = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        forg.setColor(new Color(0x000000ff));
        forg.fillRectangle(0, 0, width, height);
        this.foreground = new Texture(forg);


        int w = (int) (WORLD_WIDTH * PPM);
        int h = (int) (WORLD_HEIGHT * PPM);
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);

        loadShader();
        initializeShader();

    }

    private void loadShader() {
        shaderFog = new ShaderProgram(VERTEX, FRAGMENT);
        if (!shaderFog.isCompiled())
            throw new GdxRuntimeException("Couldn't compile shader: " + shaderFog.getLog());
    }

    private void initializeShader() {
        ShaderProgram.pedantic = false;
        shaderFog.begin();
        shaderFog.setUniformi("u_texture1", 1);
        shaderFog.setUniformi("u_mask", 2);
        shaderFog.end();
    }

    /*------------------------------------------------------------------*\
    |*							Update Methods
    \*------------------------------------------------------------------*/

    @Override
    public void update(float dt) {
        // if (!Conf.RENDER_FOG_OF_WAR) return;

        fbo.begin();
        map.renderMap();
        // Viewport vp = CameraManager.GetInstance().getViewport();
        // fbo.end(vp.getScreenX(), vp.getScreenY(), vp.getScreenWidth(), vp.getScreenHeight());
        fbo.end();


        fboTexture = fbo.getColorBufferTexture();


        Texture mask = new Texture(fogMask);
        getEntities().forEach(entity -> processEntity(entity, dt));

        mask.bind(2);
        fboTexture.bind(1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        batch.setShader(shaderFog);

        CameraManager.GetInstance().getViewport().apply();
        batch.begin();
        map.renderMap();

        batch.draw(
                foreground,
                0,
                Conf.WORLD_HEIGHT,
                Conf.WORLD_WIDTH,
                -Conf.WORLD_HEIGHT
        );
        batch.end();

        batch.setShader(SpriteBatch.createDefaultShader());
    }


    @Override
    protected void processEntity(Entity entity, float dt) {
        Vector2 pos = EcsUtils.getPosition(entity);
        fogMask.setColor(new Color(0x00000044));
        fogMask.fillCircle((int) pos.x, (int) pos.y, (int) EcsUtils.getMaxRange(entity));
    }
}
