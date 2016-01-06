package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

/**
 * @author David Insley
 */
public final class VarbitMessage extends Message {

    private final int id;
    private final int value;

    public VarbitMessage(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

}
