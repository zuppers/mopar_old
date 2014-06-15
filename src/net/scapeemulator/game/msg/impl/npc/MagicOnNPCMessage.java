package net.scapeemulator.game.msg.impl.npc;

import net.scapeemulator.game.msg.Message;

public final class MagicOnNPCMessage extends Message {

	private final int interfaceId;
	private final int childId;
	private final int index;

	public MagicOnNPCMessage(int interfaceId, int childId, int index) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int getChildId() {
		return childId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

}
