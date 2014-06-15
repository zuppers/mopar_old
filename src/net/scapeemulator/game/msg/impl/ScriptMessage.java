package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class ScriptMessage extends Message {

	private final int id;
	private final String types;
	private final Object[] parameters;
	private final boolean blank;
	
	public ScriptMessage(int id, String types, Object... parameters) {
		this.id = id;
		this.types = types;
		this.blank = parameters.length < 1;
		this.parameters = parameters;
	}

	public int getId() {
		return id;
	}

	public String getTypes() {
		return types;
	}

	public boolean blank() {
		return blank;
	}
	
	public Object[] getParameters() {
		return parameters;
	}

}
