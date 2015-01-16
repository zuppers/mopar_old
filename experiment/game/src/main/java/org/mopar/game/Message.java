package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public abstract class Message {

    /**
     * Gets the type the message will forward as.
     *
     * @return
     *          The message class.
     */
    public Class<?> getForwardingType() {
        return getClass();
    }
}
