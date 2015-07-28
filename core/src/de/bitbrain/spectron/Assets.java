package de.bitbrain.spectron;

import com.badlogic.gdx.graphics.Texture;

import java.util.Map;

import de.bitbrain.braingdx.assets.GameAssetLoader;

public final class Assets implements GameAssetLoader {

    public final class Textures {
        public static final String BLOCK = "images/block.png";
    }

    @Override
    public void put(Map<String, Class<?>> map) {
        map.put(Textures.BLOCK, Texture.class);
    }
}
