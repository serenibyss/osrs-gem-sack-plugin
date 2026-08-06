package io.github.serenibyss.gemsack.render;

import io.github.serenibyss.gemsack.SackStorage;
import io.github.serenibyss.gemsack.SackTypes;

import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;

import java.awt.*;

public class GemSackTooltip {

    private static final String BR = "</br>";
    private static final Color VALUE_COLOR = Color.YELLOW;

    private final TooltipManager tooltipManager;
    private final ItemManager itemManager;

    public GemSackTooltip(TooltipManager tooltipManager, ItemManager itemManager) {
        this.tooltipManager = tooltipManager;
        this.itemManager = itemManager;
    }

    void renderTooltip(SackTypes sackType, boolean showValue) {
        SackStorage storage = sackType.getStorage();

        if (storage.isEmpty()) {
            tooltipManager.add(new Tooltip("Empty"));
            return;
        }

        int brLength = 0;
        StringBuilder tb = new StringBuilder();

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
            brLength = tb.length();
        }

        tb.append(storage.getDragonstone() > 0 ? "Uncut dragonstone: " + color(String.valueOf(storage.getDragonstone())) : "");

        if (showValue) {
            long value = storage.getTotalValue(itemManager);
            if (value > 0) {
                if (tb.length() > 0) tb.append(BR);
                tb.append("GE Value: ").append(ColorUtil.wrapWithColorTag(
                        QuantityFormatter.quantityToStackSize(value) + " gp", VALUE_COLOR));
            }
        }

        tooltipManager.add(new Tooltip(tb.toString()));
    }

    private static String color(String text) {
        return ColorUtil.wrapWithColorTag(text, ColorUtil.fromHex("FF9040"));
    }
}
