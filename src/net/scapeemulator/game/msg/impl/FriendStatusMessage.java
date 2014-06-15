package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class FriendStatusMessage extends Message {

	private final long friend;
	private final int world;

	public FriendStatusMessage(long friend, int world) {
		this.friend = friend;
		this.world = world;
	}

	public long getFriend() {
		return friend;
	}
	
	public int getWorld() {
		return world;
	}

}
