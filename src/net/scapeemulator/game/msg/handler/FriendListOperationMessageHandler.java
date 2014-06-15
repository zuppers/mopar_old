package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.FriendListOperationMessage;

public final class FriendListOperationMessageHandler extends MessageHandler<FriendListOperationMessage> {

	@Override
	public void handle(Player player, FriendListOperationMessage message) {
		switch(message.getOpcode()) {
		case 34:
			player.getFriends().addIgnore(message.getName());
			break;
		case 57:
			player.getFriends().removeFriend(message.getName());
			break;
		case 120:
			player.getFriends().addFriend(message.getName());
			break;
		case 213:
			player.getFriends().removeIgnore(message.getName());
			break;
		}
		System.out.println(message.getName());
	}

}
