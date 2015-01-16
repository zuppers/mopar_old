package org.mopar.game;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by eve on 1/14/2015
 */
public class MessageDispatcher {

    /**
     * Handlers to dispatch messages to.
     */
    private final Map<Class<? extends Message>, MessageHandler> handlers = new HashMap<>();

    /**
     * Collection of queued messages.
     */
    private final Queue<Message> messages = new ArrayDeque<>();

    /**
     * Constructs a new {@link MessageDispatcher};
     */
    public MessageDispatcher() {}

    public <T extends Message> MessageHandler bind(Class<T> type, MessageHandler<T> handler) {
        return handlers.put(type, handler);
    }

    public MessageHandler bind(MessageAdapter adapter) {
        return bind(adapter.getAdaptingClass(), adapter);
    }

    public void queue(Message message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    public void dispatch(Message message) {
        if(!handlers.containsKey(message.getForwardingType())) {
            throw new IllegalStateException("No handler registered for message; " + message.getForwardingType().getSimpleName());
        }
        handlers.get(message.getForwardingType()).handle(message);
    }

    public void flush() {
        synchronized (messages) {
            Message message;
            while ((message = messages.poll()) != null) {
                dispatch(message);
            }
        }
    }
}
