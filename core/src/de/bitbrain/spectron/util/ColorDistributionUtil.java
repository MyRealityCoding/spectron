package de.bitbrain.spectron.util;

import com.badlogic.gdx.graphics.Color;

import net.engio.mbassy.listener.Handler;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.event.Events;
import de.bitbrain.spectron.event.EventType;

public class ColorDistributionUtil {

    private static ColorDistributionUtil INSTANCE = new ColorDistributionUtil();

    private Events events = Events.getInstance();

    private Map<Color, Integer> distribution = new HashMap<Color, Integer>();

    private ColorDistributionUtil() {
        events.register(this);
    }

    public static ColorDistributionUtil getInstance() {
        return INSTANCE;
    }

    public void clear() {
        distribution.clear();
    }

    public Map<Color, Integer> getDistribution() {
        return distribution;
    }

    @Handler
    public void onColorChanged(Events.GdxEvent event) {
        if (event.isTypeOf(EventType.CELL_COLORED)) {
            Color color = (Color) event.getPrimaryParam();
            Color oldColor = (Color) event.getSecondaryParam(0);
            dropColor(oldColor);
            pushColor(color);

        }
    }

    private void pushColor(Color color) {
        if (!distribution.containsKey(color)) {
            distribution.put(color, 1);
        } else {
            distribution.put(color, distribution.get(color) + 1);
        }
    }

    private void dropColor(Color color) {
        if (distribution.containsKey(color)) {
            distribution.put(color, distribution.get(color) - 1);
            if (distribution.get(color) <= 0) {
                distribution.remove(color);
            }
        }
    }
}
