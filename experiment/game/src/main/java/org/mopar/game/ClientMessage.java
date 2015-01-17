package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public final class ClientMessage<T extends Message> extends ForwardedMessage<T> {

    private final Client client;

    public ClientMessage(Client client, T message) {
        super(message);
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
