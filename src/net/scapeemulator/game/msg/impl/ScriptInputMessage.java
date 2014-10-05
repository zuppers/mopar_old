package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class ScriptInputMessage<T extends Number> extends Message {

    private final T value;
    private final Class<?> type;

    public ScriptInputMessage(T value) {
        this.value = value;
        type = value.getClass();
    }

    public T getValue() {
        return value;
    }

    public Class<?> getType() {
        return type;
    }

}
