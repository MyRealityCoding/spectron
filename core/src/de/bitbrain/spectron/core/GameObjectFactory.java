package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;

import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.GameWorld;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.graphics.CellRenderer;
import de.bitbrain.spectron.graphics.ObstacleRenderer;

public class GameObjectFactory {

    private GameWorld world;

    public GameObjectFactory(GameWorld world) {
        this.world = world;
        world.registerRenderer(GameObjectType.PLAYER, new ObstacleRenderer(Assets.Textures.BLOCK));
        world.registerRenderer(GameObjectType.CELL, new CellRenderer());
    }

    public GameObject createPlayer(String id, int cellX, int cellY, Grid grid, Color color) {
        GameObject object = world.addObject();
        object.setId(id);
        final int SIZE = 48;
        final float OFFSET_X = cellX * grid.getCellSize() + grid.getOffsetX() * cellX;
        final float OFFSET_Y = cellY * grid.getCellSize() + grid.getOffsetY() * cellY;
        final float x = grid.getX() + grid.getCellSize() / 2 - SIZE / 2 + OFFSET_X;
        final float y = grid.getY() + grid.getCellSize() / 2f - SIZE / 4f + OFFSET_Y;
        object.setType(GameObjectType.PLAYER);
        object.setDimensions(SIZE, SIZE);
        object.setColor(color);
        object.setPosition(x, y);
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
