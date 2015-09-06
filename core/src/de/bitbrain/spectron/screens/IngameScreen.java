package de.bitbrain.spectron.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.engio.mbassy.listener.Handler;

import java.util.Map;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.event.Events;
import de.bitbrain.braingdx.fx.FX;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.core.GameObjectController;
import de.bitbrain.spectron.core.GameObjectFactory;
import de.bitbrain.spectron.core.Grid;
import de.bitbrain.spectron.event.EventType;
import de.bitbrain.spectron.util.ColorDistributionUtil;

public class IngameScreen extends AbstractScreen {

    private Grid grid;

    private Sprite sprite;

    private FX fx = FX.getInstance();

    private GameObjectController controller;

    private Color backgroundColor;

    private Events events = Events.getInstance();

    private ColorDistributionUtil colorDistributionUtil = ColorDistributionUtil.getInstance();

    public IngameScreen(BrainGdxGame game) {
        super(game);
        setBackgroundColor(Colors.DARK_EMERALD);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
        sprite = new Sprite(SharedAssetManager.get(Assets.Textures.BACKGROUND, Texture.class));
        fx.setFadeColor(Color.BLACK);
        GameObjectFactory factory = new GameObjectFactory(tweenManager, world);
        grid = new Grid(0, 0, 10, 4, factory, tweenManager);
        grid.setPosition(width / 2f - grid.getWidth() / 2f, 40f);
        controller = new GameObjectController(grid, tweenManager, factory);
        fx.fadeIn(1.5f);
        backgroundColor = Color.WHITE.cpy();
        controller.init();
        events.register(this);
    }



    @Override
    protected void beforeWorldRender(Batch batch, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            controller.move(0, GameObjectController.Move.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            controller.move(0, GameObjectController.Move.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            controller.move(0, GameObjectController.Move.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            controller.move(0, GameObjectController.Move.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            controller.move(1, GameObjectController.Move.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            controller.move(1, GameObjectController.Move.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            controller.move(1, GameObjectController.Move.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            controller.move(1, GameObjectController.Move.RIGHT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            fx.shake(40, 2f);
        }
        sprite.setBounds(camera.position.x - Config.APP_WIDTH / 2f, camera.position.y - Config.APP_HEIGHT / 2f, Config.APP_WIDTH, Config.APP_HEIGHT);
        sprite.setColor(getColorByDistribution());
        sprite.draw(batch);
    }

    @Override
    protected void afterWorldRender(Batch batch, float delta) {
        Texture overlay = SharedAssetManager.get(Assets.Textures.OVERLAY, Texture.class);
        batch.setColor(getColorByDistribution());
        batch.draw(overlay, camera.position.x - Config.APP_WIDTH / 2f, camera.position.y - Config.APP_HEIGHT / 2f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    protected Viewport getViewport(int width, int height) {
        return new FillViewport(Config.APP_WIDTH, Config.APP_HEIGHT);
    }

    @Handler
    public void onEvent(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.CELL_COLORED)) {
            Color color = Color.WHITE.cpy();
            for (Map.Entry<Color, Integer> entry : colorDistributionUtil.getDistribution().entrySet()) {
                float totalCells = grid.getXCells() * grid.getYCells();
                color.lerp(entry.getKey(), (float) entry.getValue() / totalCells);
            }
            Tween.to(backgroundColor, ColorTween.R, 0.75f).target(color.r).start(tweenManager);
            Tween.to(backgroundColor, ColorTween.G, 0.75f).target(color.g).start(tweenManager);
            Tween.to(backgroundColor, ColorTween.B, 0.75f).target(color.b).start(tweenManager);
        } else  if (event.isTypeOf(EventType.RESTART_GAME)) {
            colorDistributionUtil.clear();
            game.setScreen(new IngameScreen(game));
        }
    }

    private Color getColorByDistribution() {
        return backgroundColor;
    }
}
