package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;

public class Grid {

    public static final float PADDING = 10f;

    public static final float SCALE = 64f;

    public static class Cell extends Sprite {

        private int state;

        private int depth = 4;

        public Cell() {
            super(SharedAssetManager.get(Assets.Textures.BLOCK, Texture.class));
            setColor(Colors.EMERALD);
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

    private Cell[][] cells;

    private float x, y;

    private int xCells, yCells;

    public Grid(float x, float y, int xCells, int yCells) {
        cells = prepare(xCells, yCells);
        this.x = x;
        this.y = y;
        this.xCells = xCells;
        this.yCells = yCells;
    }

    public void render(Batch batch, float delta) {
        for (int x = 0; x < cells.length; ++x) {
            for (int y = 0; y < cells[x].length; ++y) {
                Cell cell = cells[x][y];
                cell.setSize(SCALE, SCALE);
                float cellX = x * cell.getWidth() + getX();
                float cellY = y * cell.getHeight() + getY();
                float width = cell.getWidth();
                float height = cell.getHeight();
                Color color = cell.getColor();
                cell.setColor(Colors.lighten(color, 0.6f));
                cell.setBounds(cellX + PADDING * x, cellY + PADDING * y - cell.getDepth(), width, height);
                cell.draw(batch);
                cell.setColor(color);
                cell.setBounds(cellX + PADDING * x, cellY + PADDING * y, width, height);
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

    private Cell[][] prepare(int width, int height) {
        Cell[][] grid = new Cell[width][height];
        for (int x = 0; x < grid.length; ++x) {
            for (int y = 0; y < grid[x].length; ++y) {
                grid[x][y] = new Cell();
            }
        }
        return grid;
    }
}
