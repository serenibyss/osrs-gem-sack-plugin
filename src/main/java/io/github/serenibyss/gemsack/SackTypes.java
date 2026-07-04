package io.github.serenibyss.gemsack;

import lombok.Getter;

import net.runelite.api.gameval.ItemID;

import java.util.Arrays;
import java.util.List;

public enum SackTypes {

    POUCH(ItemID.GEM_POUCH, ItemID.GEM_POUCH_OPEN, 5),
    SATCHEL(ItemID.GEM_SATCHEL, ItemID.GEM_SATCHEL_OPEN, 10),
    TOTE(ItemID.GEM_TOTE, ItemID.GEM_TOTE_OPEN, 20),
    BAG(ItemID.GEM_BAG, ItemID.GEM_BAG_OPEN, 60),
    SACK(ItemID.GEM_SACK, ItemID.GEM_SACK_OPEN, 60);

    private static final List<Integer> BASIC_GEMS = Arrays.asList(
            ItemID.UNCUT_SAPPHIRE,
            ItemID.UNCUT_EMERALD,
            ItemID.UNCUT_RUBY,
            ItemID.UNCUT_DIAMOND,
            ItemID.UNCUT_DRAGONSTONE);

    private static final List<Integer> WEIRD_GEMS = Arrays.asList(
            ItemID.UNCUT_OPAL,
            ItemID.UNCUT_JADE,
            ItemID.UNCUT_RED_TOPAZ);

    private static final List<Integer> ALL_GEMS = Arrays.asList(
            ItemID.UNCUT_OPAL,
            ItemID.UNCUT_JADE,
            ItemID.UNCUT_RED_TOPAZ,
            ItemID.UNCUT_SAPPHIRE,
            ItemID.UNCUT_EMERALD,
            ItemID.UNCUT_RUBY,
            ItemID.UNCUT_DIAMOND,
            ItemID.UNCUT_DRAGONSTONE);

    private static final List<String> BASIC_GEM_NAMES = Arrays.asList(
            "Uncut sapphire",
            "Uncut emerald",
            "Uncut ruby",
            "Uncut diamond",
            "Uncut dragonstone");

    private static final List<String> WEIRD_GEM_NAMES = Arrays.asList(
            "Uncut opal",
            "Uncut jade",
            "Uncut red topaz");

    private static final List<String> ALL_GEM_NAMES = Arrays.asList(
            "Uncut opal",
            "Uncut jade",
            "Uncut red topaz",
            "Uncut sapphire",
            "Uncut emerald",
            "Uncut ruby",
            "Uncut diamond",
            "Uncut dragonstone");

    @Getter
    private final int closedItemID;
    @Getter
    private final int openItemID;
    @Getter
    private final int maxGems;
    @Getter
    private final SackStorage storage;

    SackTypes(int closedItemID, int openItemID, int maxGems) {
        this.closedItemID = closedItemID;
        this.openItemID = openItemID;
        this.maxGems = maxGems;
        this.storage = new SackStorage(this);
    }

    public boolean enableHover(GemSackConfig config) {
        switch (this) {
            case POUCH: return config.gemPouchHover();
            case SATCHEL: return config.gemSatchelHover();
            case TOTE: return config.gemToteHover();
            case BAG: return config.gemBagHover();
            case SACK: return config.gemSackHover();
            default: return false;
        }
    }

    public boolean enableOverlay(GemSackConfig config) {
        switch (this) {
            case POUCH: return config.gemPouchOverlay();
            case SATCHEL: return config.gemSatchelOverlay();
            case TOTE: return config.gemToteOverlay();
            case BAG: return config.gemBagOverlay();
            case SACK: return config.gemSackOverlay();
            default: return false;
        }
    }

    public List<Integer> getAllowedGems() {
        switch (this) {
            case POUCH:
            case SATCHEL:
            case TOTE:
                return WEIRD_GEMS;
            case BAG:
                return BASIC_GEMS;
            case SACK:
                return ALL_GEMS;
            default:
                return null;
        }
    }

    public boolean canStoreGem(int gemItemID) {
        List<Integer> allowedGems = getAllowedGems();
        if (allowedGems == null) return false;
        return allowedGems.contains(gemItemID);
    }

    public boolean canStoreGem(GemTypes gemType) {
        return canStoreGem(gemType.getItemID());
    }

    public List<String> getAllowedGemNames() {
        switch (this) {
            case POUCH:
            case SATCHEL:
            case TOTE:
                return WEIRD_GEM_NAMES;
            case BAG:
                return BASIC_GEM_NAMES;
            case SACK:
                return ALL_GEM_NAMES;
            default:
                return null;
        }
    }

    public static SackTypes getSackType(int itemID) {
        if (itemID == POUCH.getClosedItemID() || itemID == POUCH.getOpenItemID()) {
            return POUCH;
        } else if (itemID == SATCHEL.getClosedItemID() || itemID == SATCHEL.getOpenItemID()) {
            return SATCHEL;
        } else if (itemID == TOTE.getClosedItemID() || itemID == TOTE.getOpenItemID()) {
            return TOTE;
        } else if (itemID == BAG.getClosedItemID() || itemID == BAG.getOpenItemID()) {
            return BAG;
        } else if (itemID == SACK.getClosedItemID() || itemID == SACK.getOpenItemID()) {
            return SACK;
        }
        return null;
    }

    public static boolean isSackType(int itemID) {
        return getSackType(itemID) != null;
    }

    public static List<Integer> getAllGemTypes() {
        return ALL_GEMS;
    }
}
