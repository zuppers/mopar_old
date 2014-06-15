/**
 * 
 */
package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.PrivateChatSentMessage;

public final class PrivateChatSentMessageHandler extends MessageHandler<PrivateChatSentMessage> {

	@Override
	public void handle(Player player, PrivateChatSentMessage message) {
		player.getFriends().privateChatSent(message.getName(), message.getPacked());
	}

}
