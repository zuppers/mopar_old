package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class FriendsListStatusMessage extends Message {

	private final byte status;

	public FriendsListStatusMessage(byte status) {
		this.status = status;
	}

	public byte getStatus() {
		return status;
	}

}
