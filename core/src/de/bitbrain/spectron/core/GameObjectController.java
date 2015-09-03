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
import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.event.EventType;

public class GameObjectController {

    private static final float TIME = 0.25f;

    private static final float JUMP_HEIGHT = 55f;

    private final TweenManager tweenManager;

    private Grid grid;

    private GameObjectFactory factory;

    private List<GameObject> players;

    private Events events = Events.getInstance();

    private boolean initialized = false;

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

        players.get(0).setOffset(0f, Config.APP_HEIGHT);
        Tween.to(players.get(0), GameObjectTween.OFFSET_Y, 0.85f).delay(0.7f).target(0).ease(TweenEquations.easeOutBounce).start(tweenManager);
        players.get(1).setOffset(0f, Config.APP_HEIGHT);
        Tween.to(players.get(1), GameObjectTween.OFFSET_Y, 0.85f).delay(1.65f).target(0).ease(TweenEquations.easeOutBounce).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {

            @Override
            public void onEvent(int type, BaseTween<?> source) {
                initialized = true;
            }
        }).start(tweenManager);
    }

    public void move(int playerId, final Move move) {
        if (!initialized) {
            return;
        }
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
            if (!grid.isInRange(player.getLeft(), player.getTop())) {
                return;
            }
            if (targetX != player.getLeft()) {
                Tween.to(player, GameObjectTween.POS_X, TIME).target(targetX).ease(TweenEquations.easeOutCubic).start(tweenManager);
            }
            if (targetY != player.getTop()) {
                Tween.to(player, GameObjectTween.POS_Y, TIME).target(targetY).ease(TweenEquations.easeOutCubic).start(tweenManager);
            }
            animateJump(player);
            if (grid.isInRange(targetX, targetY)) {
                grid.setColor(targetX, targetY, player.getColor());
            }
        }
    }

    private void animateJump(final GameObject player) {
        tweenManager.killTarget(player, GameObjectTween.OFFSET_Y);
        Tween.to(player, GameObjectTween.OFFSET_Y, TIME / 3)
                .repeatYoyo(1, 0f)
                .ease(TweenEquations.easeOutCubic)
                .target(JUMP_HEIGHT)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        events.fire(EventType.PLAYER_JUMPED, player);
                    }
                })
                .start(tweenManager);
    }

    private void updateColor(GameObject player) {
        grid.setColor(player.getLeft(), player.getTop(), player.getColor());
    }

    @Handler
    public void onPlayerJump(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.PLAYER_JUMPED)) {
            final GameObject player = (GameObject) event.getPrimaryParam();
            if (grid.isInRange(player.getLeft(), player.getTop())) {
                GameObject cell = grid.getCell(player.getLeft(), player.getTop());
                if (cell != null) {
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
            } else {
                float targetY = player.getTop() - Config.APP_HEIGHT / 1.5f;
                player.setZIndex(-100);
                Tween.to(player, GameObjectTween.POS_Y, 0.3f).target(targetY).ease(TweenEquations.easeNone).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {

                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        events.fire(EventType.GAME_OVER, player);
                    }
                }).start(tweenManager);
            }
        }
    }

    @Handler
    public void onGameOver(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.GAME_OVER)) {
            this.initialized = false;
            grid.cleanUp();
            for (GameObject player : players) {
                Tween.to(player, GameObjectTween.SCALE, 0.2f).target(0f).ease(TweenEquations.easeOutQuad).start(tweenManager);
            }
        }
    }
}
