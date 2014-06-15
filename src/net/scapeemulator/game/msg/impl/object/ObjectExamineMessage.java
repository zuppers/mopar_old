package net.scapeemulator.game.msg.impl.object;

import net.scapeemulator.game.msg.Message;

/**
 * Written by David Insley
 */
public final class ObjectExamineMessage extends Message {
    
    private final int type;
    
    public ObjectExamineMessage(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    
}
