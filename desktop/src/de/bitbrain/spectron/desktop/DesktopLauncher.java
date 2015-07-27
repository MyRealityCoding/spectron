package de.bitbrain.spectron.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.SpectronGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Config.APP_NAME + " " + Config.APP_VERSION;
		config.width = Config.APP_WIDTH;
		config.height = Config.APP_HEIGHT;
		config.forceExit = true;
		config.vSyncEnabled = true;
		config.samples = 8;
		new LwjglApplication(new SpectronGame(), config);
	}
}
