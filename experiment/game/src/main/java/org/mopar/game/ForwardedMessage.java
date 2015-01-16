package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public abstract class ForwardedMessage<T extends Message> extends Message {

    private final T message;

    ForwardedMessage(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public Class<?> getForwardingType() {
        return message.getClass();
    }
}
