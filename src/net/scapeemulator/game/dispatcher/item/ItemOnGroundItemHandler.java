package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author David Insley
 */
public abstract class ItemOnGroundItemHandler {

    protected final int itemId;
    protected final int groundItemId;

    public ItemOnGroundItemHandler(int itemId, int groundItemId) {
        this.itemId = itemId;
        this.groundItemId = groundItemId;
    }

    /**
     * Handles a player using an item from their inventory on an item on the ground. The items have
     * been verified to exist.
     * 
     * @param player the player that used the item
     * @param item the item that was used on the ground item
     * @param groundPosition the position of the ground item
     */
    public abstract void handle(Player player, SlottedItem item, Position groundPosition);

    public int getItemId() {
        return itemId;
    }

    public int getGroundItemId() {
        return groundItemId;
    }
}
