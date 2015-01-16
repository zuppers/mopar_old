package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public interface MessageHandler<T extends Message> {
    void handle(T message);
}
