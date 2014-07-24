package net.scapeemulator.game.msg.handler.inter;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.inter.InterfaceClosedMessage;

public final class InterfaceClosedMessageHandler extends MessageHandler<InterfaceClosedMessage> {

	@Override
	public void handle(Player player, InterfaceClosedMessage message) {
		player.getInterfaceSet().closeWindow();
	}

}
