package net.scapeemulator.game.msg.impl;

import net.scapeemulator.game.msg.Message;

public final class ScriptMessage extends Message {

    private final int id;
    private final int id2;
    private final String types;
    private final Object[] parameters;

    public ScriptMessage(int id, String types, Object... parameters) {
        this(id, 0, types, parameters);
    }

    public ScriptMessage(int id, int id2, String types, Object... parameters) {
        this.id = id;
        this.id2 = id2;
        this.types = types;
        this.parameters = parameters;
    }

    public int getId() {
        return id;
    }

    public int getId2() {
        return id2;
    }

    public String getTypes() {
        return types;
    }

    public Object[] getParameters() {
        return parameters;
    }

}
