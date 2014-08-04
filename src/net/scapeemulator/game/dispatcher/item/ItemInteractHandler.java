package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;

/**
 * @author David Insley
 */
public abstract class ItemInteractHandler {

    private final int itemId;

    public ItemInteractHandler(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    /**
     * Handles the item interaction. The handler must verify that the inventory is the correct
     * inventory, but the dispatcher <i>will</i> verify the item exists. For example, eating should
     * not happen in the bank, so verify the Inventory argument matches the players active
     * inventory.
     * 
     * @param inventory Inventory the item was interacted with from
     * @param player Player the interacted with the item
     * @param item represents the id, amount, and slot id of the item interacted with
     */
    public abstract void handle(Inventory inventory, Player player, SlottedItem item);
}
