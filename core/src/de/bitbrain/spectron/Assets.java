package de.bitbrain.spectron;

import com.badlogic.gdx.graphics.Texture;

import java.util.Map;

import de.bitbrain.braingdx.assets.GameAssetLoader;

public final class Assets implements GameAssetLoader {

    public static final class Textures {
        public static final String BLOCK = "images/block.png";
        public static final String OVERLAY = "images/overlay.png";

        private static void put(Map<String, Class<?>> map) {
            map.put(Textures.BLOCK, Texture.class);
            map.put(Textures.OVERLAY, Texture.class);
        }
    }

    @Override
    public void put(Map<String, Class<?>> map) {
        Textures.put(map);
    }
}
