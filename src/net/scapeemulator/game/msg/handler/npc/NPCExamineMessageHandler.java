package net.scapeemulator.game.msg.handler.npc;

import net.scapeemulator.cache.def.NPCDefinition;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.npc.NPCExamineMessage;

public final class NPCExamineMessageHandler extends MessageHandler<NPCExamineMessage> {

	@Override
	public void handle(Player player, NPCExamineMessage msg) {
		NPCDefinition def = NPCDefinitions.forId(msg.getType());
		if (def.getExamine() != null) {
			player.sendMessage(def.getExamine());
		} else {
			player.sendMessage("NPC: " + msg.getType());
		}
	}
}
