package net.scapeemulator.game.msg.impl.item;

import net.scapeemulator.game.msg.Message;

public final class ItemDropMessage extends Message {

    private final int id, slot;
    
    public ItemDropMessage(int id, int slot) {
        this.id = id;
        this.slot = slot;
    }
    
    public int getId() {
        return id;
    }
    
    public int getSlot() {
    	return slot;
    }
    
}
