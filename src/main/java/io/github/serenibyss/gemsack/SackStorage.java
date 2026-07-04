package io.github.serenibyss.gemsack;

import com.google.common.collect.Multiset;

import com.google.common.primitives.Ints;

import lombok.Getter;

import net.runelite.api.gameval.ItemID;
import net.runelite.client.config.ConfigManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SackStorage {

    private static final String CONFIG_POUCH_OPAL = "z_pouchopal";
    private static final String CONFIG_POUCH_JADE = "z_pouchjade";
    private static final String CONFIG_POUCH_TOPAZ = "z_pouchtopaz";

    private static final String CONFIG_SATCHEL_OPAL = "z_satchelopal";
    private static final String CONFIG_SATCHEL_JADE = "z_satcheljade";
    private static final String CONFIG_SATCHEL_TOPAZ = "z_satcheltopaz";

    private static final String CONFIG_TOTE_OPAL = "z_toteopal";
    private static final String CONFIG_TOTE_JADE = "z_totejade";
    private static final String CONFIG_TOTE_TOPAZ = "z_totetopaz";

    private static final String CONFIG_BAG_SAPPHIRE = "z_bagsapphire";
    private static final String CONFIG_BAG_EMERALD = "z_bagemerald";
    private static final String CONFIG_BAG_RUBY = "z_bagruby";
    private static final String CONFIG_BAG_DIAMOND = "z_bagdiamond";
    private static final String CONFIG_BAG_DRAGONSTONE = "z_bagdragonstone";

    private static final String CONFIG_SACK_OPAL = "z_sackopal";
    private static final String CONFIG_SACK_JADE = "z_sackjade";
    private static final String CONFIG_SACK_TOPAZ = "z_sacktopaz";
    private static final String CONFIG_SACK_SAPPHIRE = "z_sacksapphire";
    private static final String CONFIG_SACK_EMERALD = "z_sackemerald";
    private static final String CONFIG_SACK_RUBY = "z_sackruby";
    private static final String CONFIG_SACK_DIAMOND = "z_sackdiamond";
    private static final String CONFIG_SACK_DRAGONSTONE = "z_sackdragonstone";

    private final SackTypes sackType;

    @Getter
    private int opal = -1;
    @Getter
    private int jade = -1;
    @Getter
    private int topaz = -1;
    @Getter
    private int sapphire = -1;
    @Getter
    private int emerald = -1;
    @Getter
    private int ruby = -1;
    @Getter
    private int diamond = -1;
    @Getter
    private int dragonstone = -1;

    public SackStorage(SackTypes sackType) {
        this.sackType = sackType;
    }

    public boolean isEmpty() {
        if (sackType.canStoreGem(GemTypes.OPAL) && opal > 0) return false;
        if (sackType.canStoreGem(GemTypes.JADE) && jade > 0) return false;
        if (sackType.canStoreGem(GemTypes.TOPAZ) && topaz > 0) return false;
        if (sackType.canStoreGem(GemTypes.SAPPHIRE) && sapphire > 0) return false;
        if (sackType.canStoreGem(GemTypes.EMERALD) && emerald > 0) return false;
        if (sackType.canStoreGem(GemTypes.RUBY) && ruby > 0) return false;
        if (sackType.canStoreGem(GemTypes.DIAMOND) && diamond > 0) return false;
        if (sackType.canStoreGem(GemTypes.DRAGONSTONE) && dragonstone > 0) return false;
        return true;
    }

    public int getTotalStored() {
        constrain();

        int total = 0;
        if (sackType.canStoreGem(GemTypes.OPAL) && opal > 0) total += opal;
        if (sackType.canStoreGem(GemTypes.JADE) && jade > 0) total += jade;
        if (sackType.canStoreGem(GemTypes.TOPAZ) && topaz > 0) total += topaz;
        if (sackType.canStoreGem(GemTypes.SAPPHIRE) && sapphire > 0) total += sapphire;
        if (sackType.canStoreGem(GemTypes.EMERALD) && emerald > 0) total += emerald;
        if (sackType.canStoreGem(GemTypes.RUBY) && ruby > 0) total += ruby;
        if (sackType.canStoreGem(GemTypes.DIAMOND) && diamond > 0) total += diamond;
        if (sackType.canStoreGem(GemTypes.DRAGONSTONE) && dragonstone > 0) total += dragonstone;
        return total;
    }

    public void forceUpdate(Map<GemTypes, Integer> newGems, ConfigManager configManager) {
        this.opal = newGems.getOrDefault(GemTypes.OPAL, 0);
        this.jade = newGems.getOrDefault(GemTypes.JADE, 0);
        this.topaz = newGems.getOrDefault(GemTypes.TOPAZ, 0);
        this.sapphire = newGems.getOrDefault(GemTypes.SAPPHIRE, 0);
        this.emerald = newGems.getOrDefault(GemTypes.EMERALD, 0);
        this.ruby = newGems.getOrDefault(GemTypes.RUBY, 0);
        this.diamond = newGems.getOrDefault(GemTypes.DIAMOND, 0);
        this.dragonstone = newGems.getOrDefault(GemTypes.DRAGONSTONE, 0);
        persist(configManager);
    }

    public void addSingle(GemTypes gemType, ConfigManager configManager) {
        if (!sackType.canStoreGem(gemType)) return;
        switch (gemType) {
            case OPAL: opal += 1; break;
            case JADE: jade += 1; break;
            case TOPAZ: topaz += 1; break;
            case SAPPHIRE: sapphire += 1; break;
            case EMERALD: emerald += 1; break;
            case RUBY: ruby += 1; break;
            case DIAMOND: diamond += 1; break;
            case DRAGONSTONE: dragonstone += 1; break;
        }
        persist(configManager);
    }

    public void clear(ConfigManager configManager) {
        this.opal = 0;
        this.jade = 0;
        this.topaz = 0;
        this.sapphire = 0;
        this.emerald = 0;
        this.ruby = 0;
        this.diamond = 0;
        this.dragonstone = 0;
        persist(configManager);
    }

    public void update(Multiset<Integer> deltaMinus, Multiset<Integer> deltaPlus, ConfigManager configManager) {
        List<Integer> allowedGems = sackType.getAllowedGems();

        if (deltaPlus != null) {
            deltaPlus.forEachEntry((id, c) -> {
                if (allowedGems.contains(id)) {
                    switch (id) {
                        case ItemID.UNCUT_OPAL: opal += c;break;
                        case ItemID.UNCUT_JADE: jade += c;break;
                        case ItemID.UNCUT_RED_TOPAZ: topaz += c;break;
                        case ItemID.UNCUT_SAPPHIRE: sapphire += c;break;
                        case ItemID.UNCUT_EMERALD: emerald += c;break;
                        case ItemID.UNCUT_RUBY: ruby += c;break;
                        case ItemID.UNCUT_DIAMOND: diamond += c;break;
                        case ItemID.UNCUT_DRAGONSTONE: dragonstone += c;break;
                    }
                }
            });
        }

        if (deltaMinus != null) {
            deltaMinus.forEachEntry((id, c) -> {
                if (allowedGems.contains(id)) {
                    switch (id) {
                        case ItemID.UNCUT_OPAL: opal -= c;break;
                        case ItemID.UNCUT_JADE: jade -= c;break;
                        case ItemID.UNCUT_RED_TOPAZ: topaz -= c;break;
                        case ItemID.UNCUT_SAPPHIRE: sapphire -= c;break;
                        case ItemID.UNCUT_EMERALD: emerald -= c;break;
                        case ItemID.UNCUT_RUBY: ruby -= c;break;
                        case ItemID.UNCUT_DIAMOND: diamond -= c;break;
                        case ItemID.UNCUT_DRAGONSTONE: dragonstone -= c;break;
                    }
                }
            });
        }
        persist(configManager);
    }

    private void constrain() {
        int max = sackType.getMaxGems();

        this.opal = Ints.constrainToRange(this.opal, 0, sackType.canStoreGem(GemTypes.OPAL) ? max : 0);
        this.jade = Ints.constrainToRange(this.jade, 0, sackType.canStoreGem(GemTypes.JADE) ? max : 0);
        this.topaz = Ints.constrainToRange(this.topaz, 0, sackType.canStoreGem(GemTypes.TOPAZ) ? max : 0);
        this.sapphire = Ints.constrainToRange(this.sapphire, 0, sackType.canStoreGem(GemTypes.SAPPHIRE) ? max : 0);
        this.emerald = Ints.constrainToRange(this.emerald, 0, sackType.canStoreGem(GemTypes.EMERALD) ? max : 0);
        this.ruby = Ints.constrainToRange(this.ruby, 0, sackType.canStoreGem(GemTypes.RUBY) ? max : 0);
        this.diamond = Ints.constrainToRange(this.diamond, 0, sackType.canStoreGem(GemTypes.DIAMOND) ? max : 0);
        this.dragonstone = Ints.constrainToRange(this.dragonstone, 0, sackType.canStoreGem(GemTypes.DRAGONSTONE) ? max : 0);
    }

    private void persist(ConfigManager configManager) {
        constrain();
        if (this.sackType == SackTypes.POUCH) {
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_POUCH_OPAL, this.opal);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_POUCH_JADE, this.jade);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_POUCH_TOPAZ, this.topaz);
        } else if (this.sackType == SackTypes.SATCHEL) {
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SATCHEL_OPAL, this.opal);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SATCHEL_JADE, this.jade);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SATCHEL_TOPAZ, this.topaz);
        } else if (this.sackType == SackTypes.TOTE) {
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_TOTE_OPAL, this.opal);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_TOTE_JADE, this.jade);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_TOTE_TOPAZ, this.topaz);
        } else if (this.sackType == SackTypes.BAG) {
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_SAPPHIRE, this.sapphire);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_EMERALD, this.emerald);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_RUBY, this.ruby);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_DIAMOND, this.diamond);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_DRAGONSTONE, this.dragonstone);
        } else if (this.sackType == SackTypes.SACK) {
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_OPAL, this.opal);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_JADE, this.jade);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_TOPAZ, this.topaz);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_SAPPHIRE, this.sapphire);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_EMERALD, this.emerald);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_RUBY, this.ruby);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_DIAMOND, this.diamond);
            configManager.setRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_DRAGONSTONE, this.dragonstone);
        }
    }

    public void load(ConfigManager configManager) {
        if (this.sackType == SackTypes.POUCH) {
            this.opal = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_POUCH_OPAL, Integer.class)).orElse(-1);
            this.jade = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_POUCH_JADE, Integer.class)).orElse(-1);
            this.topaz = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_POUCH_TOPAZ, Integer.class)).orElse(-1);
        } else if (this.sackType == SackTypes.SATCHEL) {
            this.opal = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SATCHEL_OPAL, Integer.class)).orElse(-1);
            this.jade = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SATCHEL_JADE, Integer.class)).orElse(-1);
            this.topaz = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SATCHEL_TOPAZ, Integer.class)).orElse(-1);
        } else if (this.sackType == SackTypes.TOTE) {
            this.opal = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_TOTE_OPAL, Integer.class)).orElse(-1);
            this.jade = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_TOTE_JADE, Integer.class)).orElse(-1);
            this.topaz = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_TOTE_TOPAZ, Integer.class)).orElse(-1);
        } else if (this.sackType == SackTypes.BAG) {
            this.sapphire = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_SAPPHIRE, Integer.class)).orElse(-1);
            this.emerald = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_EMERALD, Integer.class)).orElse(-1);
            this.ruby = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_RUBY, Integer.class)).orElse(-1);
            this.diamond = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_DIAMOND, Integer.class)).orElse(-1);
            this.dragonstone = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_BAG_DRAGONSTONE, Integer.class)).orElse(-1);
        } else if (this.sackType == SackTypes.SACK) {
            this.opal = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_OPAL, Integer.class)).orElse(-1);
            this.jade = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_JADE, Integer.class)).orElse(-1);
            this.topaz = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_TOPAZ, Integer.class)).orElse(-1);
            this.sapphire = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_SAPPHIRE, Integer.class)).orElse(-1);
            this.emerald = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_EMERALD, Integer.class)).orElse(-1);
            this.ruby = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_RUBY, Integer.class)).orElse(-1);
            this.diamond = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_DIAMOND, Integer.class)).orElse(-1);
            this.dragonstone = Optional.ofNullable((Integer) configManager.getRSProfileConfiguration(GemSackConfig.CONFIG_GROUP, CONFIG_SACK_DRAGONSTONE, Integer.class)).orElse(-1);
        }
    }
}
