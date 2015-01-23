package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author David Insley
 */
public abstract class ItemOnGroundItemHandler {

    private final int itemId;
    private final int groundItemId;

    public ItemOnGroundItemHandler(int itemId, int groundItemId) {
        this.itemId = itemId;
        this.groundItemId = groundItemId;
    }

    /**
     * Handles a player using an item from their inventory on an item on the ground. The items have
     * been verified to exist.
     * 
     * @param player the player that used the item
     * @param itemOne the item that was used on the ground item
     * @param groundItem the ground item the item was used on
     */
    public abstract void handle(Player player, SlottedItem item, GroundItem groundItem, GroundItemList list);

    public int getItemId() {
        return itemId;
    }

    public int getGroundItemId() {
        return groundItemId;
    }
}
