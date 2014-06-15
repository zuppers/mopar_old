package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class GrandExchangeSearchMessage extends Message {

	private final int itemId;

	public GrandExchangeSearchMessage(int itemId) {
		this.itemId = itemId;

	}

	public int getItemId() {
		return itemId;
	}

}
