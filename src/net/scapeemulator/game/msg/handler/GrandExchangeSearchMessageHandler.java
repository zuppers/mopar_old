package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.GrandExchangeSearchMessage;

public final class GrandExchangeSearchMessageHandler extends MessageHandler<GrandExchangeSearchMessage> {

	@Override
	public void handle(Player player, GrandExchangeSearchMessage message) {
		if(player.actionsBlocked()) {
			return;
		}
		player.getGrandExchangeHandler().searchComplete(message.getItemId());
	}

}
