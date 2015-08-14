package de.bitbrain.spectron.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.spectron.Assets;
import de.bitbrain.spectron.Colors;

public class ObstacleRenderer extends SpriteRenderer {

    public ObstacleRenderer(String assetId) {
        super(assetId);
    }

    @Override
    public void render(GameObject gameObject, Batch batch, float v) {
        sprite.setPosition(gameObject.getLeft(), gameObject.getTop() - 5f);
        sprite.setColor(0f, 0f, 0f, 0.2f);
        sprite.setSize(gameObject.getWidth(), gameObject.getHeight() / 3f);
        sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
        float scale = 1.1f + gameObject.getOffset().y / 500f;
        sprite.setScale(scale);
        sprite.draw(batch);
        sprite.setScale(1f);
        super.render(gameObject, batch, v);

    }
}
