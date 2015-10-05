package de.bitbrain.spectron.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.core.Player;

public class PlayerWidget extends VerticalGroup {

    private final Label points;
    private Player data;
    private ValueProvider pointValue = new ValueProvider();
    private int lastPoints = 0;

    private TweenManager tweenManager = SharedTweenManager.getInstance();

    static {
        Tween.registerAccessor(ValueProvider.class, new ValueTween());
    }

    public PlayerWidget(Player data) {
        this.data = data;
        Label playerName = new Label(data.name().toLowerCase(), Styles.mediumLabelStyle());
        playerName.setColor(Colors.lighten(data.getColor(), 0.7f));
        playerName.setFontScale(0.5f);
        addActor(playerName);
        points = new Label("0", Styles.mediumLabelStyle());
        points.setColor(data.getColor());
        addActor(points);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (lastPoints != data.getPoints()) {
            lastPoints = data.getPoints();
            tweenManager.killTarget(pointValue);
            Tween.to(pointValue, ValueTween.VALUE, 0.4f).target(lastPoints).ease(TweenEquations.easeInOutCubic).start(tweenManager);
        }
        points.setText(String.valueOf(Math.round(pointValue.getValue())));
        super.draw(batch, parentAlpha);
    }
}
