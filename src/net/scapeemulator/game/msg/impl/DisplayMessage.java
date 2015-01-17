package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class DisplayMessage extends Message {

	private final int mode, width, height, antialiasing;

	public DisplayMessage(int mode, int width, int height, int antialiasing) {
		this.mode = mode;
		this.width = width;
		this.height = height;
		this.antialiasing = antialiasing;
	}

	public int getMode() {
		return mode;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getAntiAliasing(){
		return antialiasing;
	}

}
