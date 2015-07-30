package de.bitbrain.spectron.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.SpriteTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.core.Grid;

public class IngameScreen extends AbstractScreen {

    private Grid grid;

    private Sprite sprite;

    public IngameScreen(BrainGdxGame game) {
        super(game);
        setBackgroundColor(Colors.DARK_EMERALD);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
        grid = new Grid(0, 0, 10, 4, tweenManager);
        grid.setPosition(width / 2f - grid.getWidth() / 2f, 40f);
        sprite = new Sprite(SharedAssetManager.get(Assets.Textures.BACKGROUND, Texture.class));

    }

    @Override
    protected void beforeWorldRender(Batch batch, float delta) {
        sprite.setBounds(0, 0, Config.APP_WIDTH, Config.APP_HEIGHT);
        sprite.draw(batch);
        grid.render(batch, delta);
    }

    @Override
    protected void afterWorldRender(Batch batch, float delta) {
        Texture overlay = SharedAssetManager.get(Assets.Textures.OVERLAY, Texture.class);
        batch.draw(overlay, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    protected Viewport getViewport(int width, int height) {
        return new FitViewport(Config.APP_WIDTH, Config.APP_HEIGHT);
    }
}
