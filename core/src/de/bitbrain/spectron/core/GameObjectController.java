package de.bitbrain.spectron.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import net.engio.mbassy.listener.Handler;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.event.Events;
import de.bitbrain.braingdx.fx.FX;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.Config;
import de.bitbrain.spectron.event.EventType;

public class GameObjectController {

    private static final float TIME = 0.25f;

    private static final float JUMP_HEIGHT = 55f;

    private final TweenManager tweenManager = SharedTweenManager.getInstance();

    private Grid grid;

    private GameObjectFactory factory;

    private List<GameObject> players;

    private Events events = Events.getInstance();

    private FX fx = FX.getInstance();

    private boolean initialized = false;

    public static enum Move {
        LEFT(-1, 0),
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1);

        private Vector2 direction;

        Move(int x, int y) {
            this.direction = new Vector2(x, y).nor();
        }

        public Vector2 getDirection() {
            return direction;
        }
    }

    private class PlayerData {

        public int indexX, indexY;
        public Color color;

        public PlayerData(int indexX, int indexY, Color color) {
            this.indexX = indexX;
            this.indexY = indexY;
            this.color = color;
        }
    }

    private PlayerData[] data = new PlayerData[]{
        new PlayerData(1, 1, Colors.ORANGE),
        new PlayerData(8, 2, Colors.BLUE)
    };

    public GameObjectController(Grid grid, GameObjectFactory factory) {
        this.grid = grid;
        this.factory = factory;
        this.players = new ArrayList<GameObject>();
        events.register(this);
    }

    public void init() {
        for (PlayerData playerData : data) {
            registerNewPlayer(playerData.indexX, playerData.indexY, playerData.color);
        }
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
            final float time = grid.getCell(targetX, targetY) != null && !grid.getCell(targetX, targetY).getId().isEmpty() && !grid.getCell(targetX, targetY).getId().equals(player.getId()) ? TIME * 2 : TIME;
            if (targetX != player.getLeft()) {
                Tween.to(player, GameObjectTween.POS_X, time).target(targetX).ease(TweenEquations.easeOutCubic).start(tweenManager);
            }
            if (targetY != player.getTop()) {
                Tween.to(player, GameObjectTween.POS_Y, time).target(targetY).ease(TweenEquations.easeOutCubic).start(tweenManager);
            }
            animateJump(player, time);
            if (grid.isInRange(targetX, targetY)) {
                grid.getCell(targetX, targetY).setId(player.getId());
                grid.setColor(targetX, targetY, player.getColor());
                updateCellData(targetX, targetY, player);
            }
        }
    }

    private void animateJump(final GameObject player, float time) {
        tweenManager.killTarget(player, GameObjectTween.OFFSET_Y);
        Tween.to(player, GameObjectTween.OFFSET_Y, time / 3)
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
                        if (initialized) {
                            GameObject winner = null;
                            for (GameObject candidate : players) {
                                if (!candidate.getId().equals(player.getId())) {
                                    winner = candidate;
                                    break;
                                }
                            }
                            events.fire(EventType.GAME_OVER, winner, player);
                        }
                    }
                }).start(tweenManager);
            }
        }
    }

    @Handler
    public void onGameOver(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.GAME_OVER)) {
            GameObject winner = (GameObject) event.getPrimaryParam();
            this.initialized = false;
            grid.destroy(winner.getColor());
            for (GameObject player : players) {
                Tween.to(player, GameObjectTween.SCALE, 0.2f).target(0f).ease(TweenEquations.easeOutQuad).start(tweenManager);
            }
            tweenManager.killTarget(winner, GameObjectTween.POS_X);
            Tween.to(winner, GameObjectTween.POS_X, 1f).target(0f).delay(1f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {

                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    events.fire(EventType.RESTART_GAME, null);
                }
            }).start(tweenManager);
        }
    }

    private void registerNewPlayer(int indexX, int indexY, Color color) {
        GameObject player = factory.createPlayer("player" + players.size(), indexX, indexY, grid, color);
        players.add(player);
        grid.setCellId(player);
        updateColor(player);
        player.setOffset(0f, Config.APP_HEIGHT);
        Tween tween = Tween.to(player, GameObjectTween.OFFSET_Y, 0.85f)
             .delay((players.size() / 1.5f) + 0.7f)
             .target(0)
             .ease(TweenEquations.easeOutBounce)
             .start(tweenManager);
        final boolean initializeValid = players.size() == data.length;
        tween.setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        initialized = initializeValid;
                    }
                });
        updateCellData(player.getLeft(), player.getTop(), player);
    }

    private void updateCellData(float x, float y, GameObject object) {
        GameObject oldData = grid.getData(x, y);
        if (oldData != null) {
            if (initialized && oldData.getType() == GameObjectType.PLAYER && !oldData.getId().equals(object.getId())) {
                events.fire(EventType.GAME_OVER, object, oldData);
            }
        }
        grid.setData(object.getLeft(), object.getTop(), null);
        grid.setData(x, y, object);
    }
}
