package net.scapeemulator.game.msg.handler.npc;

import net.scapeemulator.game.dispatcher.npc.NPCDispatcher;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.npc.MagicOnNPCMessage;

public final class MagicOnNPCMessageHandler extends MessageHandler<MagicOnNPCMessage> {

	private final NPCDispatcher dispatcher;

	public MagicOnNPCMessageHandler(NPCDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void handle(Player player, MagicOnNPCMessage msg) {
		dispatcher.handleMagic(player, msg.getInterfaceId(), msg.getChildId(), msg.getIndex());
	}
}
