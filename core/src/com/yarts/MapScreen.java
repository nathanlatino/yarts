package com.yarts;

import com.badlogic.gdx.math.MathUtils;
import com.framework.core.game.screen.BaseMapScreen;
import com.framework.enums.PlayerType;
import com.yarts.entities.UnitsFactory;


public class MapScreen extends BaseMapScreen {

    public MapScreen() {
        super();
        // stressTest(200);
    }

    private void stressTest(int count) {
        for (int i = 0; i < count; i++) {
            float x = MathUtils.random(100, 3240);
            float y = MathUtils.random(100, 1250);
            if (MathUtils.random(1) == 1) {
                UnitsFactory.getInstance().createWarrior(x, y, i % 2 == 0 ? PlayerType.P1 : PlayerType.P2);
            }
            else {
                UnitsFactory.getInstance().createWizard(x, y, i % 2 == 0 ? PlayerType.P1 : PlayerType.P2);
            }
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);
    }
}
