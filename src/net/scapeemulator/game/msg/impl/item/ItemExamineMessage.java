package net.scapeemulator.game.msg.impl.item;

import net.scapeemulator.game.msg.Message;

/**
 * Created by David Insley
 */
public final class ItemExamineMessage extends Message {

    private final int id;
    
    public ItemExamineMessage(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
