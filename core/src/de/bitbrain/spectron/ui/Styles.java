package de.bitbrain.spectron.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.spectron.Assets;

public final class Styles {

    private static AssetManager assets = SharedAssetManager.getInstance();

    public static Label.LabelStyle mediumLabelStyle() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = assets.get(Assets.Fonts.MEDIUM, BitmapFont.class);
        style.fontColor = Color.WHITE.cpy();
        return style;
    }
}
