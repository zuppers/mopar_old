package net.scapeemulator.game.msg.handler.npc;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.npc.NPCInteractMessage;
import net.scapeemulator.game.npc.NPCDispatcher;

public final class NPCInteractMessageHandler extends MessageHandler<NPCInteractMessage> {

	private final NPCDispatcher dispatcher;

	public NPCInteractMessageHandler(NPCDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void handle(Player player, NPCInteractMessage msg) {
		dispatcher.handleInteract(player, msg.getIndex());
	}
}
