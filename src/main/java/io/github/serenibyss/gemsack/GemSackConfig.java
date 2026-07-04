package io.github.serenibyss.gemsack;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(GemSackConfig.CONFIG_GROUP)
public interface GemSackConfig extends Config {

    String CONFIG_GROUP = "gemsack";

    @ConfigItem(
            keyName = "gemPouchOverlay",
            name = "Gem Pouch Overlay",
            description = "Whether to show an amount overlay for the Gem Pouch")
    default boolean gemPouchOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "gemPouchHover",
            name = "Gem Pouch Hover",
            description = "Whether to show hover info showing contained gems for the Gem Pouch")
    default boolean gemPouchHover() {
        return true;
    }

    @ConfigItem(
            keyName = "gemSatchelOverlay",
            name = "Gem Satchel Overlay",
            description = "Whether to show an amount overlay for the Gem Satchel")
    default boolean gemSatchelOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "gemSatchelHover",
            name = "Gem Satchel Hover",
            description = "Whether to show hover info showing contained gems for the Gem Satchel")
    default boolean gemSatchelHover() {
        return true;
    }

    @ConfigItem(
            keyName = "gemToteOverlay",
            name = "Gem Tote Overlay",
            description = "Whether to show an amount overlay for the Gem Tote")
    default boolean gemToteOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "gemToteHover",
            name = "Gem Tote Hover",
            description = "Whether to show hover info showing contained gems for the Gem Tote")
    default boolean gemToteHover() {
        return true;
    }

    @ConfigItem(
            keyName = "gemBagOverlay",
            name = "Gem Bag Overlay",
            description = "Whether to show an amount overlay for the Gem Bag")
    default boolean gemBagOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "gemBagHover",
            name = "Gem Bag Hover",
            description = "Whether to show hover info showing contained gems for the Gem Bag")
    default boolean gemBagHover() {
        return true;
    }

    @ConfigItem(
            keyName = "gemSackOverlay",
            name = "Gem Sack Overlay",
            description = "Whether to show an amount overlay for the Gem Sack")
    default boolean gemSackOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "gemSackHover",
            name = "Gem Sack Hover",
            description = "Whether to show hover info showing contained gems for the Gem Sack")
    default boolean gemSackHover() {
        return true;
    }
}
