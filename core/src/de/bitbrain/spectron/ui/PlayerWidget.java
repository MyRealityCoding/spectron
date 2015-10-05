package de.bitbrain.spectron.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import net.engio.mbassy.listener.Handler;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.event.Events;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.core.Player;
import de.bitbrain.spectron.event.EventType;

public class PlayerWidget extends VerticalGroup {

    private static final float ANIMATION_TIME = 0.75f;
    private static final float ALPHA = 0.45f;

    private final Label points;
    private Player data;
    private ValueProvider pointValue = new ValueProvider();
    private ValueProvider scaleValue = new ValueProvider();

    private TweenManager tweenManager = SharedTweenManager.getInstance();

    private Events events = Events.getInstance();

    static {
        Tween.registerAccessor(ValueProvider.class, new ValueTween());
    }

    public PlayerWidget(Player data) {
        this.data = data;
        scaleValue.setValue(1f);
        Label playerName = new Label(data.name().toLowerCase(), Styles.mediumLabelStyle());
        playerName.setColor(Colors.lighten(data.getColor(), 0.7f));
        playerName.setFontScale(0.5f);
        addActor(playerName);
        points = new Label("0", Styles.mediumLabelStyle());
        points.setColor(data.getColor().cpy());
        points.getColor().a = ALPHA;
        addActor(points);
        events.register(this);
    }

    @Override
    public void clear() {
        super.clear();
        events.unregister(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        points.setText(String.valueOf(Math.round(pointValue.getValue())));
        points.setFontScale(scaleValue.getValue());
        super.draw(batch, parentAlpha);
    }

    @Handler
    public void onGameOver(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.PLAYER_POINTS)) {
            int points = (Integer)event.getPrimaryParam();
            Player player = (Player)event.getSecondaryParam(0);
            if (player.getColor().equals(data.getColor())) {
                tweenManager.killTarget(pointValue);
                tweenManager.killTarget(scaleValue);
                tweenManager.killTarget(this.points.getColor());
                this.points.getColor().a = 1f;
                scaleValue.setValue(1.2f);
                Tween.to(pointValue, ValueTween.VALUE, ANIMATION_TIME).target(points).ease(TweenEquations.easeInOutCubic).start(tweenManager);
                Tween.to(scaleValue, ValueTween.VALUE, ANIMATION_TIME).target(1f).ease(TweenEquations.easeInOutCubic).start(tweenManager);
                Tween.to(this.points.getColor(), ColorTween.A, ANIMATION_TIME).target(ALPHA).ease(TweenEquations.easeInOutCubic).start(tweenManager);
            }
        }
    }
}
