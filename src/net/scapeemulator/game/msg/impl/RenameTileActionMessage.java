package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class RenameTileActionMessage extends Message {

    public static final RenameTileActionMessage RESET = new RenameTileActionMessage(null);

    private final String text;

    public RenameTileActionMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
