package de.bitbrain.spectron.core;

import com.badlogic.gdx.math.Vector2;

import net.engio.mbassy.listener.Handler;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.event.Events;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.event.EventType;

public class GameObjectController {

    private static final float TIME = 0.25f;

    private static final float JUMP_HEIGHT = 75f;

    private final TweenManager tweenManager;

    private Grid grid;

    private GameObjectFactory factory;

    private List<GameObject> players;

    private Events events = Events.getInstance();

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
        events.register(this);
    }

    public void init() {
        players.add(factory.createPlayer(1, 1, grid, Colors.ORANGE));
        players.add(factory.createPlayer(8, 2, grid, Colors.BLUE));
        updateColor(players.get(0));
        updateColor(players.get(1));
    }

    public void move(int playerId, final Move move) {
        if (players.size() > playerId && playerId >= 0) {
            final GameObject player = players.get(playerId);
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
            Tween.to(player, GameObjectTween.POS_X, TIME).target(targetX).ease(TweenEquations.easeInOutCubic).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {

                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    if (move.equals(Move.TOP) || move.equals(Move.BOTTOM)) {
                        events.fire(EventType.PLAYER_JUMPED, player);
                    }
                }
            }).start(tweenManager);
            Tween.to(player, GameObjectTween.POS_Y, TIME).target(targetY).ease(TweenEquations.easeOutBounce).start(tweenManager);
            animateJump(player, move);
            grid.setColor(targetX, targetY, player.getColor());
        }
    }

    private void animateJump(final GameObject player, Move move) {
        switch (move) {
            case LEFT: case RIGHT:
                Tween.to(player, GameObjectTween.POS_Y, TIME / 2f)
                        .repeatYoyo(1, 0f)
                        .ease(TweenEquations.easeInCubic)
                        .target(player.getTop() + JUMP_HEIGHT)
                        .setCallbackTriggers(TweenCallback.COMPLETE)
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                events.fire(EventType.PLAYER_JUMPED, player);
                            }
                        })
                     .start(tweenManager);
                break;
            case TOP: case BOTTOM:
                break;
        }
    }

    private void updateColor(GameObject player) {
        grid.setColor(player.getLeft(), player.getTop(), player.getColor());
    }

    @Handler
    public void onPlayerJump(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.PLAYER_JUMPED)) {
            GameObject player = (GameObject) event.getPrimaryParam();
            GameObject cell = grid.getCell(player.getLeft(), player.getTop());
            final float cellTarget = cell.getTop() - 10f;
            final float playerTarget = player.getTop() - 10f;
            tweenManager.killTarget(player, GameObjectTween.POS_Y);
            tweenManager.killTarget(cell, GameObjectTween.POS_Y);
            Tween.to(cell, GameObjectTween.POS_Y, 0.08f)
                 .target(cellTarget).repeatYoyo(1, 0f).ease(TweenEquations.easeOutCubic)
                    .start(tweenManager);
            Tween.to(player, GameObjectTween.POS_Y, 0.08f)
                    .target(playerTarget).repeatYoyo(1, 0f).ease(TweenEquations.easeOutCubic)
                    .start(tweenManager);
        }
    }
}
