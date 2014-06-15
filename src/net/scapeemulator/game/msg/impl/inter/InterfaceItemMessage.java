package net.scapeemulator.game.msg.impl.inter;

import net.scapeemulator.game.msg.Message;

public final class InterfaceItemMessage extends Message {

	private final int id, child, size, item;

	public InterfaceItemMessage(int id, int child, int size, int item) {
		this.id = id;
		this.child = child;
		this.size = size;
		this.item = item;
	}

	public int getId() {
		return id;
	}

	public int getChild() {
		return child;
	}

	public int getSize() {
		return size;
	}

	public int getItem() {
		return item;
	}

}
