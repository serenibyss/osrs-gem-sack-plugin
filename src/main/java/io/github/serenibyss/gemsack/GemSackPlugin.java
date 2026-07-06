package io.github.serenibyss.gemsack;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.inject.Provides;

import javax.inject.Inject;

import io.github.serenibyss.gemsack.render.GemSackOverlay;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@PluginDescriptor(
        name = "Gem Sack",
        description = "Extra info at a glance about the Gem Sack (and its earlier forms)",
        tags = {"gem", "sack", "crafting", "sapphire", "emerald", "ruby", "diamond", "dragonstone", "opal", "jade", "topaz"}
)
public class GemSackPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private TooltipManager tooltipManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private GemSackConfig config;

    private GemSackOverlay overlay = null;

    // For when the sack is used on an item, or an item is used on the sack, or the sack is emptied to bank
    private Multiset<Integer> inventorySnapshot;
    private final List<SackTypes> clickedSacks = new ArrayList<>();
    private boolean checkForUpdate;

    // For when the "Check" message is clicked on a sack
    private SackTypes checkedSack;

    // For when a gem is picked up off the ground
    private GemTypes pickedUpGem;

    @Override
    protected void startUp() throws Exception {
        overlay = new GemSackOverlay(config, client, tooltipManager);
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay);
    }

    @Provides
    GemSackConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(GemSackConfig.class);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            for (SackTypes sackType : SackTypes.values()) {
                sackType.getStorage().load(configManager);
            }
        }
    }

    @Subscribe
    public void onItemDespawned(ItemDespawned event) {
        if (pickedUpGem == null) return;
        if (event.getItem() == null) return;
        if (event.getItem().getId() != pickedUpGem.getItemID()) return;

        ItemContainer playerInv = client.getItemContainer(InventoryID.INV);
        List<SackTypes> sackTypes = Utils.getOpenSacksInInventory(playerInv);
        if (sackTypes != null) {
            for (SackTypes type : sackTypes) {
                type.getStorage().addSingle(pickedUpGem, configManager);
            }
        }

        pickedUpGem = null;
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        String option = event.getMenuOption();

        // On the ground, can only pick up one ground item per tick so take the first.
        // The game tick event will clear this state for the next tick's gem if necessary.
        if (option.equals("Take") && GemTypes.isGemName(event.getMenuTarget()) && pickedUpGem == null) {
            pickedUpGem = GemTypes.getGemByName(event.getMenuTarget());
        }

        if (event.getWidget() == null) return;

        int id = event.getWidget().getItemId();


        // In inventory
        // Right click empty into bank
        if (SackTypes.isSackType(id) && option.equals("Empty") || option.equals("Fill")) {

            // For emptying into inventory
            inventorySnapshot = Utils.createInventorySnapshot(client.getItemContainer(InventoryID.INV));
            clickedSacks.add(SackTypes.getSackType(id));
            checkForUpdate = true;

            // For emptying into bank
            checkedSack = SackTypes.getSackType(id);
        }
        // Use gem on sack thing or sack thing on gem
        else if (option.equals("Use")
                && event.getMenuAction() == MenuAction.WIDGET_TARGET_ON_WIDGET
                && client.getSelectedWidget() != null) {

            int selectedItemID = client.getSelectedWidget().getItemId();
            int clickedOnItemID = event.getWidget().getItemId();

            // Clicked gem thing onto a gem
            if (SackTypes.isSackType(selectedItemID)) {
                SackTypes sackType = SackTypes.getSackType(selectedItemID);
                List<Integer> allowedGems = sackType.getAllowedGems();
                if (allowedGems.contains(clickedOnItemID)) {
                    inventorySnapshot = Utils.createInventorySnapshot(client.getItemContainer(InventoryID.INV));
                    clickedSacks.add(sackType);
                    checkForUpdate = true;
                }
            }
            // Clicked a gem onto gem thing
            else if (SackTypes.isSackType(clickedOnItemID)) {
                SackTypes sackType = SackTypes.getSackType(clickedOnItemID);
                List<Integer> allowedGems = sackType.getAllowedGems();
                if (allowedGems.contains(selectedItemID)) {
                    inventorySnapshot = Utils.createInventorySnapshot(client.getItemContainer(InventoryID.INV));
                    clickedSacks.add(sackType);
                    checkForUpdate = true;
                }
            }
        }
        // Fallback, if the gem sack is checked, read the text
        else if (SackTypes.isSackType(id) && option.equals("Check")) {
            checkedSack = SackTypes.getSackType(id);
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        // Gem rock mining
        if (event.getType() == ChatMessageType.SPAM) {
            String message = event.getMessage();
            // "mined": gem rocks, "found": other mining sources
            if (message.startsWith("You just mined a") || message.startsWith("You just found a")) {
                GemTypes gemType = GemTypes.getGemByName(message + "uncut"); // hacky once again
                if (gemType != null) {
                    List<SackTypes> sacks = Utils.getOpenSacksInInventory(client.getItemContainer(InventoryID.INV));
                    if (sacks != null) {
                        for (SackTypes sack : sacks) {
                            sack.getStorage().addSingle(gemType, configManager);
                        }
                    }
                }
            }
        }

        if (event.getType() == ChatMessageType.GAMEMESSAGE && checkedSack != null) {
            String message = event.getMessage();

            if (message.startsWith("You empty your gem") && message.contains("into the bank")) {
                checkedSack.getStorage().clear(configManager);
            }

            // Update state based on what the chat says
            if (message.startsWith("Opal:") || message.startsWith("Sapphires:")) {
                Map<GemTypes, Integer> newGems = Utils.getGemsFromChat(message);
                checkedSack.getStorage().forceUpdate(newGems, configManager);
            }

            // Emergency exit if the bag gets a messed up state
            if (message.startsWith("The gem") && (message.contains("is now empty.") || message.contains("is empty."))) {
                checkedSack.getStorage().clear(configManager);
            }

            checkedSack = null;
        }
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INV) return;

        if (checkForUpdate) {
            checkForUpdate = false;
            SackTypes[] sacks = clickedSacks.toArray(new SackTypes[0]);
            clickedSacks.clear();

            Multiset<Integer> currentInventory = Utils.createInventorySnapshot(event.getItemContainer());
            Multiset<Integer> deltaMinus = Multisets.difference(currentInventory, inventorySnapshot);
            Multiset<Integer> deltaPlus = Multisets.difference(inventorySnapshot, currentInventory);
            for (SackTypes sackType : sacks) {
                sackType.getStorage().update(deltaMinus, deltaPlus, configManager);
            }
        }
    }
}
