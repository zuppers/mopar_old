package org.mopar.game;

/**
 * Created by eve on 1/14/2015
 */
public enum DispatchPolicy {

    /**
     * Immediately dispatch messages on receive.
     */
    IMMEDIATE,

    /**
     * Queue messages on receive.
     */
    QUEUE
}
