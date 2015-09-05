package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class SetPositionMessage extends Message {

    private final int x, y;

    public SetPositionMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}