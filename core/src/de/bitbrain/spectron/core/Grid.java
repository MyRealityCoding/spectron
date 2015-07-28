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
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;

public class Grid {

    public static final float PADDING = 12f;

    public static final float SCALE = 80f;

    public static class Cell extends Sprite {

        private int state;

        private int depth = 6;

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

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    private static class CellTween implements TweenAccessor<Cell> {

        public static final int OFFSET = 1;
        public static final int ALPHA = 2;


        @Override
        public int getValues(Cell target, int tweenType, float[] returnValues) {
            switch (tweenType) {
                case OFFSET:
                    returnValues[0] = target.getOffset();
                    return 1;
                case ALPHA:
                    returnValues[0] = target.getColor().a;
                    return 1;
            }
            return 0;
        }

        @Override
        public void setValues(Cell target, int tweenType, float[] newValues) {
            switch (tweenType) {
                case OFFSET:
                    target.setOffset(newValues[0]);
                    break;
                case ALPHA:
                    target.setAlpha(newValues[0]);
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

    public Grid(float x, float y, int xCells, int yCells, TweenManager tweenManager) {
        cells = prepare(xCells, yCells, tweenManager);
        this.x = x;
        this.y = y;
        this.xCells = xCells;
        this.yCells = yCells;
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
                final float PADDING_Y = PADDING * 1.2f;
                cell.setBounds(cellX + PADDING * x, cellY + PADDING_Y * y - cell.getDepth(), width, height);
                cell.draw(batch);
                cell.setColor(color);
                cell.setBounds(cellX + PADDING * x, cellY + PADDING_Y * y, width, height);
                cell.draw(batch);
            }
        }
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private Cell[][] prepare(int width, int height, TweenManager tweenManager) {
        Cell[][] grid = new Cell[width][height];
        int iteration = 0;
        for (int x = 0; x < grid.length; ++x) {
            for (int y = 0; y < grid[x].length; ++y) {
                Cell cell = new Cell();
                grid[x][y] = cell;
                cell.setOffset(-250f);
                cell.setAlpha(0f);
                Tween.to(cell, CellTween.OFFSET, 1.5f).target(0f).delay(iteration * 0.02f).ease(TweenEquations.easeOutElastic).start(tweenManager);
                Tween.to(cell, CellTween.ALPHA, 2.5f).target(1f).delay(iteration * 0.02f).ease(TweenEquations.easeOutExpo).start(tweenManager);
                iteration++;
            }
        }
        return grid;
    }
}
