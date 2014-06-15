/**
 * 
 */
package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.PrivacySettingsUpdateMessage;

public final class PrivacySettingsUpdateMessageHandler extends MessageHandler<PrivacySettingsUpdateMessage> {

	@Override
	public void handle(Player player, PrivacySettingsUpdateMessage message) {
		player.getFriends().changePrivateChatSetting(message.getPrivateChatSetting());
		player.getFriends().changePublicChatSetting(message.getPublicChatSetting());
		player.getFriends().changeTradeRequestsSetting(message.getTradeRequestsSetting());
	}

}
