package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public interface ClientMessageHandler<T extends Message> {
    void handle(Client client, T message);
}
