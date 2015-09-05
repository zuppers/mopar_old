package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class MinimapUpdateMessage extends Message {

    public static final MinimapUpdateMessage RESET = new MinimapUpdateMessage(0);
    public static final MinimapUpdateMessage HIDE_MINIMAP = new MinimapUpdateMessage(2);
    public static final MinimapUpdateMessage HIDE_COMPASS = new MinimapUpdateMessage(3);
    public static final MinimapUpdateMessage HIDE_BOTH = new MinimapUpdateMessage(5);

    private final int value;

    private MinimapUpdateMessage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
