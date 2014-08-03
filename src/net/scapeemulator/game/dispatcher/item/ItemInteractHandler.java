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

    public abstract void handle(Inventory inventory, Player player, SlottedItem item);
}
