package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;

public class PlayerData {

    private int points;

    private Color color = Color.WHITE.cpy();

    public PlayerData(Color color) {
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public Color getColor() {
        return color;
    }
}
