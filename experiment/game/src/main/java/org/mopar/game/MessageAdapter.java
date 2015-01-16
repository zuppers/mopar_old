package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public abstract class MessageAdapter<T extends Message> implements MessageHandler<T> {

    private final Class<? extends Message> type;

    protected MessageAdapter(Class<? extends Message> type) {
        this.type = type;
    }

    public Class<? extends Message> getAdaptingClass() {
        return type;
    }
}
