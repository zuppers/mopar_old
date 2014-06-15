package net.scapeemulator.game.msg.impl.npc;

import net.scapeemulator.game.msg.Message;

public final class NPCInteractMessage extends Message {

	private final int index;

	public NPCInteractMessage(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
