package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class FriendListOperationMessage extends Message {

	private final int opcode;
	private final long name;

	public FriendListOperationMessage(int opcode, long name) {
		this.opcode = opcode;
		this.name = name;
	}

	public int getOpcode() {
		return opcode;
	}
	
	public long getName() {
		return name;
	}

}
