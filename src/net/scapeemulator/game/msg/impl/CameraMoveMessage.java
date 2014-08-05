package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class CameraMoveMessage extends Message {

    private final int x;
    private final int y;
    
    private final int speed;
    
	public final int[] data;

	public CameraMoveMessage(int x, int y, int ... data) {
	    this.x = x;
	    this.y = y;
		this.data = data;
	}

	public int getX() {
	    return x;
	}
	
	public int getY() {
	    return y;
	}

}
