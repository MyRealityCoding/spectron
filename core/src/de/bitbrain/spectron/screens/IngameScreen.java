package de.bitbrain.spectron.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.core.Grid;

public class IngameScreen extends AbstractScreen {

    private Grid grid;

    public IngameScreen(BrainGdxGame game) {
        super(game);
        setBackgroundColor(Colors.DARK_EMERALD);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
        grid = new Grid(0, 0, 10, 5);
        grid.setPosition(width / 2f - grid.getWidth() / 2f, 10f);

    }

    @Override
    protected void beforeWorldRender(Batch batch, float delta) {
        grid.render(batch, delta);
    }
}
