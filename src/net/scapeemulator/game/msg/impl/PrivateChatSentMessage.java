package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class PrivateChatSentMessage extends Message {

	private final long name;
	private final byte[] packed;

	public PrivateChatSentMessage(long name, byte[] packed) {
		this.name = name;
		this.packed = packed;
	}

	public long getName() {
		return name;
	}

	public byte[] getPacked() {
		return packed;
	}

}
