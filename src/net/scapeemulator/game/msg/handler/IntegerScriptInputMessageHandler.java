package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.IntegerScriptInputMessage;

public final class IntegerScriptInputMessageHandler extends MessageHandler<IntegerScriptInputMessage> {

	@Override
	public void handle(Player player, IntegerScriptInputMessage message) {
	    if(message.getValue() < 1) {
	        return;
	    }
		if(player.getScriptInput().getIntegerInputListener() != null) {
			player.getScriptInput().getIntegerInputListener().inputReceived(message.getValue());
		} else {
			System.out.println("Unhandled script input: " + message.getValue());
		}
	}

}
