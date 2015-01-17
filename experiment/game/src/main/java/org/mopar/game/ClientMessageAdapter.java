package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public final class ClientMessageAdapter<T extends Message> extends MessageAdapter<ClientMessage<T>> {

    private final ClientMessageHandler<T> handler;

    ClientMessageAdapter(Class<? extends Message> type, ClientMessageHandler<T> handler) {
        super(type);
        this.handler = handler;
    }

    @Override
    public void handle(ClientMessage<T> message) {
        handler.handle(message.getClient(), message.getMessage());
    }

    public static <T extends Message> ClientMessageAdapter<T> create(Class<T> type, ClientMessageHandler<T> handler) {
        return new ClientMessageAdapter<>(type, handler);
    }
}