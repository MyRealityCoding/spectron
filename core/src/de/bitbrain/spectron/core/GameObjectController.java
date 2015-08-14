package de.bitbrain.spectron.core;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.spectron.Colors;

public class GameObjectController {

    private static final float TIME = 0.25f;

    private static final float JUMP_HEIGHT = 75f;

    private final TweenManager tweenManager;

    private Grid grid;

    private GameObjectFactory factory;

    private List<GameObject> players;

    public static enum Move {
        LEFT(-1, 0),
        TOP(0, 1),
        RIGHT(1, 0),
        BOTTOM(0, -1);

        private Vector2 direction;

        Move(int x, int y) {
            this.direction = new Vector2(x, y).nor();
        }

        public Vector2 getDirection() {
            return direction;
        }
    }

    public GameObjectController(Grid grid, TweenManager tweenManager, GameObjectFactory factory) {
        this.grid = grid;
        this.factory = factory;
        this.tweenManager = tweenManager;
        this.players = new ArrayList<GameObject>();
    }

    public void init() {
        players.add(factory.createPlayer(1, 1, grid, Colors.ORANGE));
        players.add(factory.createPlayer(8, 2, grid, Colors.BLUE));
    }

    public void move(int playerId, Move move) {
        if (players.size() > playerId && playerId >= 0) {
            GameObject player = players.get(playerId);
            if (tweenManager.containsTarget(player, GameObjectTween.POS_X)) {
                return;
            }
            if (tweenManager.containsTarget(player, GameObjectTween.POS_Y)) {
                return;
            }
            float targetX = player.getLeft() + move.getDirection().x * grid.getCellSize();
            float targetY = player.getTop() + move.getDirection().y  * grid.getCellSize();
            if (targetX > player.getLeft()) {
                targetX += grid.getOffsetX();
            } else if (targetX < player.getLeft()) {
                targetX -= grid.getOffsetX();
            }
            if (targetY > player.getTop()) {
                targetY += grid.getOffsetY();
            }  else if (targetY < player.getTop()) {
                targetY -= grid.getOffsetY();
            }
            Tween.to(player, GameObjectTween.POS_X, TIME).target(targetX).ease(TweenEquations.easeInOutCubic).start(tweenManager);
            Tween.to(player, GameObjectTween.POS_Y, TIME).target(targetY).ease(TweenEquations.easeOutBounce).start(tweenManager);
            animateJump(player, move);
            grid.setColor(targetX, targetY, player.getColor());
        }
    }

    private void animateJump(GameObject player, Move move) {
        switch (move) {
            case LEFT: case RIGHT:
                Tween.to(player, GameObjectTween.POS_Y, TIME / 2f)
                     .repeatYoyo(1, 0f)
                     .ease(TweenEquations.easeInCubic)
                     .target(player.getTop() + JUMP_HEIGHT)
                     .start(tweenManager);
                break;
            case TOP: case BOTTOM:
                break;
        }
    }
}
