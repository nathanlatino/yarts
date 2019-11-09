package com.framework.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.framework.core.Conf;
import com.yarts.Yarts;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = Conf.RESOLUTION_W;
		cfg.height = Conf.RESOLUTION_H;
		cfg.resizable = false;
		cfg.vSyncEnabled = true;
		cfg.fullscreen = Conf.FULL_SCREEN;
		cfg.title = "Yarts";

		new LwjglApplication(new Yarts(), cfg);
	}
}
