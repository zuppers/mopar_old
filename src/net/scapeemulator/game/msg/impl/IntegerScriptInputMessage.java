package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class IntegerScriptInputMessage extends Message {

	private final int value;

	public IntegerScriptInputMessage(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
