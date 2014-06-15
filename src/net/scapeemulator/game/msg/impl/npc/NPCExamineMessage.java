package net.scapeemulator.game.msg.impl.npc;

import net.scapeemulator.game.msg.Message;

/**
 * Written by David Insley
 */
public final class NPCExamineMessage extends Message {
    
    private final int type;
    
    public NPCExamineMessage(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    
}
