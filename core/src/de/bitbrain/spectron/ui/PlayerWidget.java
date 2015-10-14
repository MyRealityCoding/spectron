package de.bitbrain.spectron.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import net.engio.mbassy.listener.Handler;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.event.Events;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.core.Player;
import de.bitbrain.spectron.event.EventType;

public class PlayerWidget extends VerticalGroup {

    private static final float ANIMATION_TIME = 0.75f;

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
        playerName.setColor(Colors.lighten(data.getColor(), 1.7f));
        playerName.setFontScale(0.5f);
        addActor(playerName);
        points = new Label("0", Styles.mediumLabelStyle());
        points.setColor(data.getColor().cpy());
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
    public void onPlayerPoints(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.PLAYER_POINTS)) {
            int points = (Integer)event.getPrimaryParam();
            Player player = (Player)event.getSecondaryParam(0);
            if (player.getColor().equals(data.getColor())) {
                tweenManager.killTarget(pointValue);
                tweenManager.killTarget(scaleValue);
                scaleValue.setValue(1.6f);
                this.points.setColor(Colors.lighten(this.data.getColor(), 3.0f));
                TweenUtils.toColor(this.points.getColor(), this.data.getColor(), ANIMATION_TIME / 1.2f, TweenEquations.easeInCubic);
                Tween.to(pointValue, ValueTween.VALUE, ANIMATION_TIME).target(points).ease(TweenEquations.easeInOutCubic).start(tweenManager);
                Tween.to(scaleValue, ValueTween.VALUE, ANIMATION_TIME).target(1f).ease(TweenEquations.easeOutCubic).start(tweenManager);
            }
        }
    }
}
