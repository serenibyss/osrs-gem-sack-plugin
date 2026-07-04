package io.github.serenibyss.gemsack;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static Map<GemTypes, Integer> getGemsFromChat(String text) {
        Map<GemTypes, Integer> gems = new HashMap<>();

        try {
            text = text.replace("<br>", " / ");
            String[] entries = text.split("\\s*/\\s*");
            for (String entry : entries) {
                String[] parts = entry.split(":\\s*");
                if (parts.length == 2) {
                    String gem = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    GemTypes gemType = GemTypes.getGemByName("uncut " + gem); // hacky
                    if (gemType != null) {
                        gems.put(gemType, count);
                    }
                }
            }
        } catch (Throwable ignored) {}

        return gems;
    }

    // Creates a snapshot of all gems in the player's inventory when called
    public static Multiset<Integer> createInventorySnapshot(ItemContainer container) {
        if (container == null) return null;

        Multiset<Integer> inventory = HashMultiset.create();
        Arrays.stream(container.getItems())
                .filter(item -> SackTypes.getAllGemTypes().contains(item.getId()))
                .forEach(i -> inventory.add(i.getId(), i.getQuantity()));
        return inventory;
    }

    // Finds all gem sacks in the player's inventory
    // Could be multiple, if sacks are not combined to the final Gem Sack yet
    public static List<SackTypes> getSacksInInventory(ItemContainer container) {
        if (container == null) return null;

        List<SackTypes> sacks = new ArrayList<>();
        for (Item item : container.getItems()) {
            if (item == null) continue;
            SackTypes sackType = SackTypes.getSackType(item.getId());
            if (sackType != null) {
                sacks.add(sackType);
            }
        }
        return sacks;
    }
}
