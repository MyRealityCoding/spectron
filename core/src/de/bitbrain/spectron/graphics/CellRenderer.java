package de.bitbrain.spectron.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;

public class CellRenderer extends SpriteRenderer {

    public CellRenderer() {
        super(Assets.Textures.BLOCK);
    }

    @Override
    public void render(GameObject gameObject, Batch batch, float v) {
        Color color = gameObject.getColor().cpy();
        float y = gameObject.getTop();
        gameObject.setColor(Colors.lighten(color, 0.6f));
        gameObject.setPosition(gameObject.getLeft(), y - gameObject.getOffset().y);
        super.render(gameObject, batch, v);
        gameObject.setColor(color);
        gameObject.setPosition(gameObject.getLeft(), y);
        super.render(gameObject, batch, v);

    }
}
