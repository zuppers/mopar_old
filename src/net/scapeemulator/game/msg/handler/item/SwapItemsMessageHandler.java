package net.scapeemulator.game.msg.handler.item;

import net.scapeemulator.game.model.player.Interface;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.SwapItemsMessage;

public final class SwapItemsMessageHandler extends MessageHandler<SwapItemsMessage> {

	@Override
	public void handle(Player player, SwapItemsMessage message) {
		if(player.actionsBlocked()) {
			return;
		}
		if (message.getId() == Interface.INVENTORY && message.getSlot() == 0) {
			player.getInventory().swap(message.getSource(), message.getDestination());
		}
	}

}
