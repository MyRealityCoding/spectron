package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SpriteTween;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;

public class Grid {

    public static final float PADDING = 12f;

    public static final float SCALE = 80f;

    public static class Cell extends Sprite {

        private int state;

        private float depth = 6;

        private float offset = 0;

        public Cell() {
            super(SharedAssetManager.get(Assets.Textures.BLOCK, Texture.class));
            setColor(Colors.EMERALD);
        }

        public float getOffset() {
            return offset;
        }

        public void setOffset(float offset) {
            this.offset = offset;
        }

        public float getDepth() {
            return depth;
        }

        public void setDepth(float depth) {
            this.depth = depth;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    private static class CellTween extends SpriteTween {

        public static final int OFFSET = 10;

        public static final int DEPTH = 11;


        @Override
        public int getValues(Sprite target, int tweenType, float[] returnValues) {
            if (super.getValues(target, tweenType, returnValues) > 0) {
                return 1;
            }
            switch (tweenType) {
                case OFFSET:
                    if (target instanceof Cell) {
                        returnValues[0] = ((Cell)target).getOffset();
                    }
                    return 1;
                case DEPTH:
                    if (target instanceof Cell) {
                        returnValues[0] = ((Cell)target).getDepth();
                    }
                    return 1;

            }
            return 0;
        }

        @Override
        public void setValues(Sprite target, int tweenType, float[] newValues) {
            super.setValues(target, tweenType, newValues);
            switch (tweenType) {
                case OFFSET:
                    if (target instanceof Cell) {
                        ((Cell)target).setOffset(newValues[0]);
                    }
                    break;
                case DEPTH:
                    if (target instanceof Cell) {
                        ((Cell)target).setDepth(newValues[0]);
                    }
                    break;
            }
        }
    }

    static {
        Tween.registerAccessor(Cell.class, new CellTween());
    }

    private Cell[][] cells;

    private float x, y;

    private int xCells, yCells;

    private final TweenManager tweenManager;

    public Grid(float x, float y, int xCells, int yCells, GameObjectFactory factory, TweenManager tweenManager) {
        cells = prepare(xCells, yCells, tweenManager);
        this.x = x;
        this.y = y;
        this.xCells = xCells;
        this.yCells = yCells;
        this.tweenManager = tweenManager;
    }

    public void render(Batch batch, float delta) {
        for (int x = cells.length - 1; x >=0; --x) {
            for (int y = cells[x].length - 1; y >= 0; --y) {
                Cell cell = cells[x][y];
                cell.setSize(SCALE, SCALE);
                float cellX = x * cell.getWidth() + getX();
                float cellY = y * cell.getHeight() + getY() + cell.getOffset();
                float width = cell.getWidth();
                float height = cell.getHeight();
                Color color = cell.getColor();
                cell.setColor(Colors.lighten(color, 0.5f));
                final float PADDING_Y = getOffsetY();
                cell.setBounds(cellX + PADDING * x, cellY + PADDING_Y * y, width, height);
                cell.draw(batch);
                cell.setColor(color);
                cell.setBounds(cellX + PADDING * x, cellY + PADDING_Y * y + cell.getDepth(), width, height);
                cell.draw(batch);
            }
        }
    }

    public void setActive(int cellX, int cellY, boolean active) {
        if (isInRange(cellX, cellY)) {
            Cell cell = cells[cellX][cellY];
            tweenManager.killTarget(cell, CellTween.DEPTH);
            float targetDepth = active ? 3 : 6;
            Tween.to(cell, CellTween.DEPTH, 0.5f).target(targetDepth).start(tweenManager);
        }
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

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setCellColor(float x, float y, Color color) {
        int cellX = getLocalIndexX(x);
        int cellY = getLocalIndexY(y);
        if (isInRange(cellX, cellY)) {
            Cell cell = cells[cellX][cellY];
            tweenManager.killTarget(cell, CellTween.COLOR_R);
            tweenManager.killTarget(cell, CellTween.COLOR_G);
            tweenManager.killTarget(cell, CellTween.COLOR_B);
            Tween.to(cell, CellTween.COLOR_R, 0.25f).target(color.r).delay(0.175f).ease(TweenEquations.easeOutCubic).start(tweenManager);
            Tween.to(cell, CellTween.COLOR_G, 0.25f).target(color.g).delay(0.175f).ease(TweenEquations.easeOutCubic).start(tweenManager);
            Tween.to(cell, CellTween.COLOR_B, 0.25f).target(color.b).delay(0.175f).ease(TweenEquations.easeOutCubic).start(tweenManager);
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

    private Cell[][] prepare(int width, int height, TweenManager tweenManager) {
        Cell[][] grid = new Cell[width][height];
        int iteration = 0;
        for (int x = 0; x < grid.length; ++x) {
            for (int y = 0; y < grid[x].length; ++y) {
                Cell cell = new Cell();
                grid[x][y] = cell;
                cell.setOffset(-500f);
                cell.setAlpha(0f);
                Tween.to(cell, CellTween.OFFSET, 0.7f).target(0f).delay(iteration * 0.05f).ease(TweenEquations.easeOutElastic).start(tweenManager);
                Tween.to(cell, CellTween.ALPHA, 0.7f).target(1f).delay(iteration * 0.05f).ease(TweenEquations.easeInOutCubic).start(tweenManager);
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
