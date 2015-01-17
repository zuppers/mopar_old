package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public abstract class Service {

    /**
     * Dispatcher for sent messages.
     */
    private final MessageDispatcher dispatcher = new MessageDispatcher();

    /**
     * Status for if the service is active.
     */
    private boolean active;

    /**
     * Hook to initialize the service.
     */
    public abstract void init();

    /**
     * Handles a client.
     *
     * @param client The client to handle
     */
    public abstract void handle(Client client);

    /**
     *
     *
     * @param type
     * @param handler
     * @param <T>
     */
    public <T extends Message> void use(Class<T> type, ClientMessageHandler<T> handler) {
        dispatcher.bind(ClientMessageAdapter.create(type, handler));
    }

    /**
     * Sets if the service is active.
     *
     * @param active The flag.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets if the service is active.
     *
     * @return The active flag.
     */
    public boolean isActive() {
        return active;
    }
}
