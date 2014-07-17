package net.scapeemulator.game.msg.impl.inter;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.msg.Message;

public final class InterfaceItemsMessage extends Message {

    private final int id, slot, type;
    private final Item[] items;

    public InterfaceItemsMessage(int type, Item[] items) {
        this(-1, -1, type, items);
    }

    /**
     * Constructs a message to send to the player to populate an interface container. Set id and
     * slot to -1 for the special container ids.
     * 
     * @param id interface id
     * @param slot interface slot for container
     * @param type container type
     * @param items item array
     */
    public InterfaceItemsMessage(int id, int slot, int type, Item[] items) {
        this.id = id;
        this.slot = slot;
        this.type = type;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public int getType() {
        return type;
    }

    public Item[] getItems() {
        return items;
    }

}
