package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;

import de.bitbrain.braingdx.event.Events;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.event.EventType;

public enum Player {

    PLAYER1(1, 1, Colors.ORANGE),
    PLAYER2(8, 2, Colors.BLUE);

    private Events events = Events.getInstance();

    private int startX, startY;

    private Color color;

    private int points;

    Player(int startX, int startY, Color color) {
        this.startX = startX;
        this.startY = startY;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void addPoints(int points) {
        this.points += Math.abs(points);
        events.fire(EventType.PLAYER_POINTS, this.points, this);
    }

    public void clear() {
        this.points = 0;
    }
}
