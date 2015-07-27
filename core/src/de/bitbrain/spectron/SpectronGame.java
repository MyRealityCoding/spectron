package de.bitbrain.spectron;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class SpectronGame extends BrainGdxGame {
	@Override
	protected GameAssetLoader getAssetLoader() {
		return new Assets();
	}

	@Override
	protected AbstractScreen getInitialScreen() {
		return new de.bitbrain.spectron.screens.IngameScreen(this);
	}
}
