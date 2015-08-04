package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SpriteTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;

public class Grid {

    public static final float PADDING = 8f;

    public static final float SCALE = 80f;

    private final GameObjectFactory factory;

    private GameObject[][] cells;

    private float x, y;

    private int xCells, yCells;

    private final TweenManager tweenManager;

    public Grid(float x, float y, int xCells, int yCells, GameObjectFactory factory, TweenManager tweenManager) {
        this.x = x;
        this.y = y;
        this.xCells = xCells;
        this.yCells = yCells;
        this.tweenManager = tweenManager;
        this.factory = factory;
        cells = prepare(xCells, yCells, tweenManager);
    }

    public int getCellSize() {
        return Math.round(SCALE);
    }

    public int getXCells() {
        return xCells;
    }

    public int getYCells() {
        return yCells;
    }

    public float getOffsetX() {
        return PADDING;
    }

    public float getOffsetY() {
        return PADDING * 1.2f;
    }

    public int getWidth() {
        return Math.round(xCells * SCALE + PADDING * xCells - PADDING);
    }

    public int getHeight() {
        return Math.round(yCells * SCALE + PADDING * yCells - PADDING);
    }

    public void setPosition(float newX, float newY) {
        this.x = newX;
        this.y = newY;
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                cells[x][y].setPosition(x * SCALE + this.x + PADDING * x, y * SCALE + this.y + PADDING * y);
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isInRange(float x, float y) {
        return isInRange(getLocalIndexX(x), getLocalIndexY(y));
    }

    private GameObject[][] prepare(int width, int height, TweenManager tweenManager) {
        GameObject[][] grid = new GameObject[width][height];
        int iteration = 0;
        for (int x = 0; x < grid.length; ++x) {
            for (int y = 0; y < grid[x].length; ++y) {
                grid[x][y] = factory.createCell();
                grid[x][y].setPosition(x * SCALE + this.x, y * SCALE + this.y);
                grid[x][y].setZIndex(-iteration);
                iteration++;
            }
        }
        return grid;
    }

    private boolean isInRange(int cellX, int cellY) {
        return cellX >= 0 && cellY >= 0 && cellX < cells.length && cellY < cells[cellX].length;
    }

    private int getLocalIndexX(float x) {
        float localX = x - getX();
        return (int)Math.round(Math.floor(localX / (SCALE + PADDING)));
    }

    private int getLocalIndexY(float y) {
        float localY = y - getY();
        return (int)Math.round(Math.floor(localY / (SCALE + PADDING)));
    }
}
