package net.scapeemulator.game.dispatcher.item;

import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author David Insley
 */
public abstract class ItemOnNPCHandler {

    private final int itemId;
    private final int npcId;

    public ItemOnNPCHandler(int itemId, int npcId) {
        this.itemId = itemId;
        this.npcId = npcId;
    }

    /**
     * Handles the use of an item in the players inventory on a NPC in their current scene. The NPC
     * and item have both been verified to be real.
     * 
     * @param player the player that used the item
     * @param item the item in the players inventory that was used
     * @param npc the npc the item was used on
     */
    public abstract void handle(Player player, SlottedItem item, NPC npc);

    public int getItemId() {
        return itemId;
    }

    public int getNPCId() {
        return npcId;
    }

}
