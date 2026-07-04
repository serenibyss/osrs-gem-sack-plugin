package io.github.serenibyss.gemsack.render;

import io.github.serenibyss.gemsack.SackStorage;
import io.github.serenibyss.gemsack.SackTypes;

import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;

import java.awt.*;

public class GemSackTooltip {

    private static final String BR = "</br>";

    private final TooltipManager tooltipManager;

    public GemSackTooltip(TooltipManager tooltipManager) {
        this.tooltipManager = tooltipManager;
    }

    void renderTooltip(SackTypes sackType) {
        SackStorage storage = sackType.getStorage();

        int brLength = 0;
        StringBuilder tb = new StringBuilder();

        if (storage.isEmpty()) {
            tooltipManager.add(new Tooltip("Empty"));
            return;
        }

        tb.append(storage.getOpal() > 0 ? "Uncut opal: " + color(String.valueOf(storage.getOpal())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
            brLength = tb.length();
        }

        tb.append(storage.getJade() > 0 ? "Uncut jade: " + color(String.valueOf(storage.getJade())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
            brLength = tb.length();
        }

        tb.append(storage.getTopaz() > 0 ? "Uncut red topaz: " + color(String.valueOf(storage.getTopaz())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
            brLength = tb.length();
        }

        tb.append(storage.getSapphire() > 0 ? "Uncut sapphire: " + color(String.valueOf(storage.getSapphire())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
            brLength = tb.length();
        }

        tb.append(storage.getEmerald() > 0 ? "Uncut emerald: " + color(String.valueOf(storage.getEmerald())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
            brLength = tb.length();
        }

        tb.append(storage.getRuby() > 0 ? "Uncut ruby: " + color(String.valueOf(storage.getRuby())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
            brLength = tb.length();
        }

        tb.append(storage.getDiamond() > 0 ? "Uncut diamond: " + color(String.valueOf(storage.getDiamond())) : "");
        if (tb.length() > brLength) {
            tb.append(BR);
        }

        tb.append(storage.getDragonstone() > 0 ? "Uncut dragonstone: " + color(String.valueOf(storage.getDragonstone())) : "");
        tooltipManager.add(new Tooltip(tb.toString()));
    }

    private static String color(String text) {
        return ColorUtil.wrapWithColorTag(text, ColorUtil.fromHex("FF9040"));
    }
}
