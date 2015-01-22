package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author Hadyn Richard
 */
public abstract class ItemOnItemHandler {

    private final int itemOne, itemTwo;

    public ItemOnItemHandler(int itemOne, int itemTwo) {
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
    }

    /**
     * Handles a player using an item from their inventory on another item in their inventory. The
     * items have been verified to exist in the players backpack inventory.
     * 
     * @param player the player 
     * @param itemOne 
     * @param itemTwo
     */
    public abstract void handle(Player player, SlottedItem itemOne, SlottedItem itemTwo);

    public int getItemOne() {
        return itemOne;
    }

    public int getItemTwo() {
        return itemTwo;
    }

}
