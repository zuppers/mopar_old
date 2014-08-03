package net.scapeemulator.game.msg.handler.npc;

import net.scapeemulator.game.dispatcher.npc.NPCDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.npc.NPCOptionMessage;

/**
 * Written by Hadyn Richard
 */
public final class NPCOptionMessageHandler extends MessageHandler<NPCOptionMessage> {
    
    private final NPCDispatcher dispatcher;
    
    public NPCOptionMessageHandler(NPCDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(Player player, NPCOptionMessage msg) {
        dispatcher.handle(player, msg.getId(), msg.getOption());
    }
}
