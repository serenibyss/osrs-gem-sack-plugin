package io.github.serenibyss.gemsack;

import lombok.Getter;

import net.runelite.api.gameval.ItemID;

public enum GemTypes {

    OPAL(ItemID.UNCUT_OPAL, "opal", "opals"),
    JADE(ItemID.UNCUT_JADE, "jade", "jades"),
    TOPAZ(ItemID.UNCUT_RED_TOPAZ, "red topaz", "red topazes"),
    SAPPHIRE(ItemID.UNCUT_SAPPHIRE, "sapphire", "sapphires"),
    EMERALD(ItemID.UNCUT_EMERALD, "emerald", "emeralds"),
    RUBY(ItemID.UNCUT_RUBY, "ruby", "rubies"),
    DIAMOND(ItemID.UNCUT_DIAMOND, "diamond", "diamonds"),
    DRAGONSTONE(ItemID.UNCUT_DRAGONSTONE, "dragonstone", "dragonstones");

    private static final GemTypes[] VALUES = values();

    @Getter
    private final int itemID;
    @Getter
    private final String singularName;
    @Getter
    private final String pluralName;

    GemTypes(int itemID, String singularName, String pluralName) {
        this.itemID = itemID;
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    public boolean isGemByName(String name) {
        name = name.toLowerCase();
        if (!name.contains("uncut")) return false;
        return name.contains(singularName) || name.contains(pluralName);
    }

    public static boolean isGemName(String name) {
        return getGemByName(name) != null;
    }

    public static GemTypes getGemByName(String name) {
        for (GemTypes type : VALUES) {
            if (type.isGemByName(name)) return type;
        }
        return null;
    }

    public static GemTypes getGemByID(int itemID) {
        for (GemTypes type : VALUES) {
            if (itemID == type.itemID) return type;
        }
        return null;
    }
}
