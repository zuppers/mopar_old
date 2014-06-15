package net.scapeemulator.game.msg.handler.object;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.object.ObjectExamineMessage;

/**
 * Written by David Insley
 */
public final class ObjectExamineMessageHandler extends MessageHandler<ObjectExamineMessage> {
	
    @Override
    public void handle(Player player, ObjectExamineMessage msg) {
    	player.sendMessage("Object: " + msg.getType());
    }
}
