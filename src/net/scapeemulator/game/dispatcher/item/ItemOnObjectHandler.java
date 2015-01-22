package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author Hadyn Richard
 */
public abstract class ItemOnObjectHandler {

    private final int itemId;
    private final int objectId;

    public ItemOnObjectHandler(int itemId, int objectId) {
        this.itemId = itemId;
        this.objectId = objectId;
    }

    /**
     * Handles an item on object usage by a player. The object has been verified to exist and the
     * item has been verified to exist in the players backpack inventory.
     * 
     * @param player the player that used the item
     * @param object the object the item was used on
     * @param item the item used on the object
     */
    public abstract void handle(Player player, GroundObject object, SlottedItem item);

    public int getItemId() {
        return itemId;
    }

    public int getObjectId() {
        return objectId;
    }

}
