package de.bitbrain.spectron.screens;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.spectron.Colors;

public class IngameScreen extends AbstractScreen {

    public IngameScreen(BrainGdxGame game) {
        super(game);
        setBackgroundColor(Colors.DARK_EMERALD);
    }
}
