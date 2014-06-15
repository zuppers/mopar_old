package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class PrivacySettingsUpdateMessage extends Message {

	private final int publicChat;
	private final int privateChat;
	private final int trade;
	
	public PrivacySettingsUpdateMessage(int publicChat, int privateChat, int trade) {
		this.publicChat = publicChat;
		this.privateChat = privateChat;
		this.trade = trade;
	}


	public int getPublicChatSetting() {
		return publicChat;
	}


	public int getPrivateChatSetting() {
		return privateChat;
	}


	public int getTradeRequestsSetting() {
		return trade;
	}



}
