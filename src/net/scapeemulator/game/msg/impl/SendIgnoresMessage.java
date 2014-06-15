package net.scapeemulator.game.msg.impl;

import java.util.List;

import net.scapeemulator.game.msg.Message;

public final class SendIgnoresMessage extends Message {

	private final List<Long> ignores;

	public SendIgnoresMessage(List<Long> ignores) {
		this.ignores = ignores;
	}

	public List<Long> getIgnores() {
		return ignores;
	}

}
