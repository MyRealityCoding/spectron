package de.bitbrain.spectron.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.fx.FX;
import de.bitbrain.braingdx.tweens.SpriteTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.core.GameObjectController;
import de.bitbrain.spectron.core.GameObjectFactory;
import de.bitbrain.spectron.core.Grid;

public class IngameScreen extends AbstractScreen {

    private Grid grid;

    private Sprite sprite;

    private FX fx = FX.getInstance();

    private GameObjectController controller;

    public IngameScreen(BrainGdxGame game) {
        super(game);
        setBackgroundColor(Colors.DARK_EMERALD);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
        grid = new Grid(0, 0, 10, 4, tweenManager);
        grid.setPosition(width / 2f - grid.getWidth() / 2f, 40f);
        sprite = new Sprite(SharedAssetManager.get(Assets.Textures.BACKGROUND, Texture.class));
        fx.setFadeColor(Color.BLACK);
        GameObjectFactory factory = new GameObjectFactory(tweenManager, grid, world);
        controller = new GameObjectController(grid, tweenManager, factory);
        fx.fadeIn(1.5f);
        controller.init();
    }

    @Override
    protected void beforeWorldRender(Batch batch, float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            controller.move(0, GameObjectController.Move.TOP);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            controller.move(0, GameObjectController.Move.LEFT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            controller.move(0, GameObjectController.Move.BOTTOM);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            controller.move(0, GameObjectController.Move.RIGHT);
        }
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
        return new FillViewport(Config.APP_WIDTH, Config.APP_HEIGHT);
    }
}
