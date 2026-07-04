package io.github.serenibyss.gemsack.render;

import io.github.serenibyss.gemsack.SackTypes;

import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;

import java.awt.*;

public class GemSackTextOverlay {

    void draw(Graphics2D graphics, WidgetItem widgetItem, SackTypes sackType) {
        graphics.setFont(FontManager.getRunescapeFont());
        int stored = sackType.getStorage().getTotalStored();

        int startDrawX = widgetItem.getCanvasLocation().getX();
        int startDrawY = widgetItem.getCanvasLocation().getY() + 31;

        drawString(graphics, FontManager.getRunescapeSmallFont(), "" + stored, startDrawX, startDrawY);
    }

    private void drawString(Graphics2D graphics, Font font, String label, int x, int y) {
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(label, x + 1, y + 1);
        graphics.setColor(Color.WHITE);
        graphics.drawString(label, x, y);
    }
}
