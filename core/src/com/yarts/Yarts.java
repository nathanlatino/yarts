package com.yarts;

import com.framework.core.game.BaseGame;

public class Yarts extends BaseGame {

	public void create() {
		super.create();
	    setActiveScreen(new MapScreen());
    }
}
