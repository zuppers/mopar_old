package net.scapeemulator.game.msg.handler.object;

import net.scapeemulator.cache.def.ObjectDefinition;
import net.scapeemulator.game.model.definition.ObjectDefinitions;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.object.ObjectExamineMessage;

/**
 * Written by David Insley
 */
public final class ObjectExamineMessageHandler extends MessageHandler<ObjectExamineMessage> {
	
    @Override
    public void handle(Player player, ObjectExamineMessage msg) {
        int id = msg.getType();
        ObjectDefinition def = ObjectDefinitions.forId(id);
    	player.sendMessage("Object: " + id + " (" + def.getName() + ")");
    }
}
