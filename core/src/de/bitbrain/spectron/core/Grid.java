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
import de.bitbrain.braingdx.event.Events;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.SpriteTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.event.EventType;

public class Grid {

    public static final float PADDING = 8f;

    public static final float SCALE = 80f;

    private class CellData {
        public GameObject cell;
        public GameObject data;

        public CellData(GameObject cell) {
            this.cell = cell;
        }
    }

    private final GameObjectFactory factory;

    private CellData[][] cells;

    private float x, y;

    private int xCells, yCells;

    private final TweenManager tweenManager = SharedTweenManager.getInstance();

    private Events events = Events.getInstance();

    public Grid(float x, float y, int xCells, int yCells, GameObjectFactory factory) {
        this.x = x;
        this.y = y;
        this.xCells = xCells;
        this.yCells = yCells;
        this.factory = factory;
        prepare(xCells, yCells, tweenManager);
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
        int iteration = 0;
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                prepareCell(x, y, iteration);
                iteration++;
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

    public boolean hasData(float x, float y) {
        CellData data = getDataInternally(x, y);
        return data != null && data.data != null;
    }

    public void setData(float x, float y, GameObject object) {
        CellData data = getDataInternally(x, y);
        if (data != null) {
            data.data = object;
        }
    }

    public GameObject getData(float x, float y) {
        CellData data = getDataInternally(x, y);
        return data != null ? data.data : null;
    }

    public void setColor(float x, float y, Color color) {
        if (isInRange(x, y)) {
            GameObject cell = getCell(x, y);
            if (!cell.getColor().equals(color)) {
                events.fire(EventType.CELL_COLORED, color, cell.getColor());
            }
            tweenManager.killTarget(cell, GameObjectTween.R);
            tweenManager.killTarget(cell, GameObjectTween.G);
            tweenManager.killTarget(cell, GameObjectTween.B);
            Tween.to(cell, GameObjectTween.R, 0.3f).target(color.r).ease(TweenEquations.easeInCubic).start(tweenManager);
            Tween.to(cell, GameObjectTween.G, 0.3f).target(color.g).ease(TweenEquations.easeInCubic).start(tweenManager);
            Tween.to(cell, GameObjectTween.B, 0.3f).target(color.b).ease(TweenEquations.easeInCubic).start(tweenManager);
        }
    }

    public GameObject getCell(float x, float y) {
        CellData data = getDataInternally(x, y);
        return data != null ? data.cell : null;
    }

    private CellData getDataInternally(float x, float y) {
        if (isInRange(x, y)) {
            return cells[getLocalIndexX(x)][getLocalIndexY(y)];
        } else {
            return null;
        }
    }

    private void prepare(int width, int height, TweenManager tweenManager) {
        cells = new CellData[width][height];
        int iteration = 0;
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                GameObject cell = factory.createCell();
                cells[x][y] = new CellData(cell);
                cell.setZIndex(-iteration);
                prepareCell(x, y, iteration);
                iteration++;
            }
        }
    }

    private boolean isInRange(int cellX, int cellY) {
        return cellX >= 0 && cellY >= 0 && cellX < cells.length && cellY < cells[cellX].length;
    }

    private int getLocalIndexX(float x) {
        float localX = x - getX();
        int index = (int)Math.round(Math.floor(localX / (SCALE + PADDING)));
        return index;
    }

    private int getLocalIndexY(float y) {
        float localY = y - getY();
        int index = (int)Math.round(Math.floor(localY / (SCALE + PADDING)));
        return index;
    }

    private void prepareCell(int x, int y, int iteration) {
        CellData data = cells[x][y];
        GameObject cell = data.cell;
        tweenManager.killTarget(cell, GameObjectTween.POS_Y);
        tweenManager.killTarget(cell, GameObjectTween.ALPHA);
        float targetY = y * SCALE + this.y + PADDING * y - cell.getOffset().y;
        cell.setPosition(x * SCALE + this.x + PADDING * x, -60);
        cell.getColor().a = 0.0f;
        Tween.to(cell, GameObjectTween.POS_Y, 0.7f).target(targetY).delay(iteration * 0.05f).ease(TweenEquations.easeOutElastic).start(tweenManager);
        Tween.to(cell, GameObjectTween.ALPHA, 0.7f).target(1f).delay(iteration * 0.05f).ease(TweenEquations.easeInOutCubic).start(tweenManager);
    }

    public void setCellId(GameObject object) {
        GameObject cell = getCell(object.getLeft(), object.getTop());
        if (cell != null) {
            cell.setId(object.getId());
        }
    }

    public void destroy(Color color) {
        int iteration = 0;
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                CellData data = cells[x][y];
                setColor(data.cell.getLeft(), data.cell.getTop() + data.cell.getHeight(), color);
                Tween.to(data.cell, GameObjectTween.SCALE, 0.6f).target(0).delay(0.5f + 0.05f * iteration).start(tweenManager);
                Tween.to(data.cell, GameObjectTween.ALPHA, 0.4f).target(0).delay(0.3f + 0.05f * iteration).start(tweenManager);
                Tween.to(data.cell, GameObjectTween.POS_Y, 0.4f).target(-100).delay(0.3f + 0.05f * iteration).start(tweenManager);
                iteration++;
            }
        }
    }
}
