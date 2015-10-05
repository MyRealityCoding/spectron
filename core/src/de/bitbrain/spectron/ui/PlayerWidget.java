package de.bitbrain.spectron.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import de.bitbrain.spectron.Colors;
import de.bitbrain.spectron.core.Player;

public class PlayerWidget extends VerticalGroup {

    private final Label points;
    private Player data;

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
        points.setText(String.valueOf(data.getPoints()));
        super.draw(batch, parentAlpha);
    }
}
