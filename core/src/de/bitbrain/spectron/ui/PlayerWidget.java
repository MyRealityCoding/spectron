package de.bitbrain.spectron.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.bitbrain.spectron.core.Player;

public class PlayerWidget extends VerticalGroup {

    private Player data;

    public PlayerWidget(Player data) {
        this.data = data;
        Label label = new Label("19283", Styles.mediumLabelStyle());
        label.setColor(data.getColor());
        addActor(label);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
