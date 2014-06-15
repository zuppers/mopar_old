package net.scapeemulator.game.msg.impl.item;

import net.scapeemulator.game.msg.Message;

/**
 * Created by David Insley
 */
public final class ItemInteractMessage extends Message {

    private final int id, slot;
    
    public ItemInteractMessage(int id, int slot) {
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
