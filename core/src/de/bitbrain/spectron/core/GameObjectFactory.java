package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.GameWorld;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.graphics.CellRenderer;

public class GameObjectFactory {

    private TweenManager tweenManager;

    private GameWorld world;

    public GameObjectFactory(TweenManager tweenManager, GameWorld world) {
        this.world = world;
        this.tweenManager = tweenManager;
        world.registerRenderer(GameObjectType.PLAYER, new SpriteRenderer(Assets.Textures.BLOCK));
        world.registerRenderer(GameObjectType.CELL, new CellRenderer());
    }

    public GameObject createPlayer(int cellX, int cellY, Grid grid, Color color) {
        GameObject object = world.addObject();
        final int SIZE = 48;
        final float OFFSET_X = cellX * grid.getCellSize() + grid.getOffsetX() * cellX;
        final float OFFSET_Y = cellY * grid.getCellSize() + grid.getOffsetY() * cellY;
        final float x = grid.getX() + grid.getCellSize() / 2 - SIZE / 2 + OFFSET_X;
        final float y = grid.getY() + grid.getCellSize() / 2f - SIZE / 4f + OFFSET_Y;
        object.setType(GameObjectType.PLAYER);
        object.setDimensions(SIZE, SIZE);
        object.setColor(color);
        object.setPosition(x, y);
        object.setOffset(0f, Config.APP_HEIGHT);
        Tween.to(object, GameObjectTween.OFFSET_Y, 0.85f).delay(2.1f).target(0).ease(TweenEquations.easeOutBounce).start(tweenManager);
        return object;
    }

    public GameObject createCell() {
        GameObject object = world.addObject();
        object.setColor(Colors.EMERALD);
        object.setType(GameObjectType.CELL);
        object.setDimensions(Grid.SCALE, Grid.SCALE);
        object.setOffset(0f, 5f);
        return object;
    }
}
