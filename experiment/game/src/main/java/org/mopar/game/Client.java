package org.mopar.game;

import org.mopar.game.net.*;
import org.mopar.game.net.Attachment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eve on 1/14/2015
 */
public final class Client {

    /**
     * The attachments for this client.
     */
    private final Map<Class<? extends Attachment>, Attachment> attachments = new HashMap<>();

    /**
     * Attaches an attachment.
     *
     * @param attachment The attachment.
     * @param <T> The attachment generic type.
     * @return The replaced attachment or {@code null} if no attachment was replaced.
     */
    public <T extends Attachment> T attach(T attachment) {
        return (T) attachments.put(attachment.getClass(), attachment);
    }

    /**
     * Removes an attachment.
     *
     * @param attachment The attachment class.
     * @param <T> The attachment generic type.
     * @return The removed attachment or {@code null}.
     */
    public <T extends Attachment> T removeAttachment(Class<T> attachment) {
        return (T) attachments.remove(attachment);
    }

    /**
     * Gets an attachment.
     *
     * @param attachment The attachment class.
     * @param <T> The attachment generic type.
     * @return The attachment or {@code null} if the attachment for the specified type does not exist.
     */
    public <T extends Attachment> T getAttachment(Class<T> attachment) {
        return (T) attachments.get(attachment);
    }

    /**
     * Gets an attachment or if the attachment does not exist sets its default value
     * and returns it.
     *
     * @param attachment The attachment class.
     * @param defaultValue The default attachment.
     * @param <T> The attachment generic type.
     * @return The attachment.
     */
    public <T extends Attachment> T getAttachmentOrDefault(Class<T> attachment, T defaultValue) {
        return (T) attachments.getOrDefault(attachment, defaultValue);
    }
}
