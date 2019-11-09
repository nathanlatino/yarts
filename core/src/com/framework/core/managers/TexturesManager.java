package com.framework.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.framework.enums.Orientations;
import com.framework.utils.AnimMap;
import com.yarts.ClassType;

public class TexturesManager {

    private static TexturesManager instance;

    private AnimMap warrior;
    private AnimMap wizard;
    private AnimMap archer;
    private AnimMap worker;

    /*------------------------------------------------------------------*\
    |*							Constructors
    \*------------------------------------------------------------------*/

    private TexturesManager() {
        createWarriorTextures();
        createWizardTextures();
        createArcherTextures();
        createWorkerTextures();
    }

    public static TexturesManager getInstance() {
        if (instance == null) {
            instance = new TexturesManager();
        }
        return instance;
    }

    /*------------------------------------------------------------------*\
    |*							Public Methods
    \*------------------------------------------------------------------*/

    public AnimMap getWarrior() {
        return warrior;
    }

    public AnimMap getWizard() {
        return wizard;
    }

    public AnimMap getArcher() {
        return archer;
    }

    public AnimMap getWorker() {
        return worker;
    }

    /*------------------------------------------------------------------*\
    |*							Private Methods
    \*------------------------------------------------------------------*/

    private void createWarriorTextures() {
        ClassType classType = ClassType.WARRIOR;
        Texture moveT = new Texture(Gdx.files.internal(createPath(classType.name(), "move")));
        Texture attaT = new Texture(Gdx.files.internal(createPath(classType.name(), "attack")));
        Texture deadT = new Texture(Gdx.files.internal(createPath(classType.name(), "death")));

        warrior = new AnimMap();
        warrior.setClassType(classType);
        warrior.setMove(createAnimationGroup(moveT, 8, 4, .1f));
        warrior.setAttack(createAnimationGroup(attaT, 6, 4, .1f));
        warrior.setDeath(createAnimationGroup(deadT, 6, 4, .5f));
        warrior.setHeight(20);
        warrior.setWidth(20);
    }

    private void createWizardTextures() {
        ClassType classType = ClassType.WIZARD;
        Texture moveT = new Texture(Gdx.files.internal(createPath(classType.name(), "move")));
        Texture attaT = new Texture(Gdx.files.internal(createPath(classType.name(), "attack")));
        Texture deadT = new Texture(Gdx.files.internal(createPath(classType.name(), "death")));

        wizard = new AnimMap();
        wizard.setClassType(classType);
        wizard.setMove(createAnimationGroup(moveT, 8, 4, .1f));
        wizard.setAttack(createAnimationGroup(attaT, 7, 4, .1f));
        wizard.setDeath(createAnimationGroup(deadT, 6, 4, .5f));
        wizard.setHeight(20);
        wizard.setWidth(20);
    }

    private void createArcherTextures() {
        ClassType classType = ClassType.ARCHER;
        Texture moveT = new Texture(Gdx.files.internal(createPath(classType.name(), "move")));
        Texture attaT = new Texture(Gdx.files.internal(createPath(classType.name(), "attack")));
        Texture deadT = new Texture(Gdx.files.internal(createPath(classType.name(), "death")));

        archer = new AnimMap();
        archer.setClassType(classType);
        archer.setMove(createAnimationGroup(moveT, 8, 4, .1f));
        archer.setAttack(createAnimationGroup(attaT, 13, 4, .1f));
        archer.setDeath(createAnimationGroup(deadT, 6, 4, .5f));
        archer.setHeight(20);
        archer.setWidth(20);
    }

    private void createWorkerTextures() {
        ClassType classType = ClassType.WORKER;
        Texture moveT = new Texture(Gdx.files.internal(createPath(classType.name(), "move")));
        Texture attaT = new Texture(Gdx.files.internal(createPath(classType.name(), "attack")));
        Texture deadT = new Texture(Gdx.files.internal(createPath(classType.name(), "death")));

        worker = new AnimMap();
        worker.setClassType(classType);
        worker.setMove(createAnimationGroup(moveT, 8, 4, .1f));
        worker.setAttack(createAnimationGroup(attaT, 8, 4, .1f));
        worker.setDeath(createAnimationGroup(deadT, 6, 4, .5f));
        worker.setHeight(20);
        worker.setWidth(20);
    }

    /*------------------------------------------------------------------*\
    |*							    Helpers
    \*------------------------------------------------------------------*/

    public String createPath(String className, String type) {
        StringBuilder sb = new StringBuilder("textures/heros/");
        sb.append(className.toLowerCase()).append("/");
        sb.append(type).append(className.toLowerCase()).append(".png");
        return sb.toString();
    }

    public IntMap<Animation> createAnimationGroup(Texture texture, int columns, int rows, float fDuration) {
        IntMap<Animation> animations = new IntMap<>();
        int w = texture.getWidth() / columns;
        int h = texture.getHeight() / rows;
        Array<Animation<TextureRegion>> trs = loadAnimationFromSheet(texture, w, h, rows, columns, fDuration);
        animations.put(Orientations.SOUTH.getIndex(), trs.get(Orientations.SOUTH.getRow()));
        animations.put(Orientations.WEST.getIndex(), trs.get(Orientations.WEST.getRow()));
        animations.put(Orientations.EAST.getIndex(), trs.get(Orientations.EAST.getRow()));
        animations.put(Orientations.NORTH.getIndex(), trs.get(Orientations.NORTH.getRow()));
        return animations;
    }

    public static Array<Animation<TextureRegion>> loadAnimationFromSheet(Texture texture, float w, float h, int rows, int cols, float frameDuration) {
        TextureRegion[][] temp = TextureRegion.split(texture, (int) w, (int) h);
        Array<TextureRegion> textureArray = new Array<>();
        Array<Animation<TextureRegion>> animations = new Array<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                textureArray.add(temp[row][col]);
            }
            animations.add(new Animation(frameDuration, textureArray, Animation.PlayMode.NORMAL));
            textureArray.clear();
        }
        return animations;
    }
}
