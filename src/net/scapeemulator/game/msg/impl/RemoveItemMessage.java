package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class RemoveItemMessage extends Message {

    private final int id, childId, itemSlot, itemId;

    public RemoveItemMessage(int id, int childId, int itemSlot, int itemId) {
        this.id = id;
        this.childId = childId;
        this.itemSlot = itemSlot;
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public int getChildId() {
        return childId;
    }

    public int getItemSlot() {
        return itemSlot;
    }

    public int getItemId() {
        return itemId;
    }

}
