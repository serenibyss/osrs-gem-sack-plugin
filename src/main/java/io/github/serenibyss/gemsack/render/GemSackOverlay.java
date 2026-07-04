package io.github.serenibyss.gemsack.render;

import io.github.serenibyss.gemsack.GemSackConfig;
import io.github.serenibyss.gemsack.SackTypes;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

import java.awt.*;

@Slf4j
public class GemSackOverlay extends WidgetItemOverlay {

    private final Client client;
    private final GemSackConfig config;

    // Renderers
    private final GemSackTextOverlay overlay;
    private final GemSackTooltip tooltip;

    public GemSackOverlay(GemSackConfig config, Client client, TooltipManager tooltipManager) {
        this.config = config;
        this.client = client;

        showOnInventory();

        this.overlay = new GemSackTextOverlay();
        this.tooltip = new GemSackTooltip(tooltipManager);
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
        SackTypes sackType = SackTypes.getSackType(itemId);
        if (sackType == null) return;

        graphics.setFont(FontManager.getRunescapeSmallFont());

        if (sackType.enableOverlay(this.config)) {
            overlay.draw(graphics, widgetItem, sackType);
        }

        if (sackType.enableHover(this.config) && isHovered(widgetItem, client.getMouseCanvasPosition())) {
            tooltip.renderTooltip(sackType);
        }
    }

    private boolean isHovered(WidgetItem widgetItem, Point mousePosition) {
        return widgetItem.getCanvasBounds().contains(mousePosition.getX(), mousePosition.getY());
    }
}
