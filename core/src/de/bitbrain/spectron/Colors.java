package de.bitbrain.spectron;


import com.badlogic.gdx.graphics.Color;

public final class Colors {
    public static final Color EMERALD = Color.valueOf("10db79");
    public static final Color DARK_EMERALD = lighten(EMERALD, 0.1f);
    public static final Color ORANGE = Color.valueOf("ff3c00");
    public static final Color BLUE = Color.valueOf("007eff");

    public static Color lighten(Color color, float factor) {
        Color result = color.cpy();
        result.r *= factor;
        result.g *= factor;
        result.b *= factor;
        return result;
    }
}
