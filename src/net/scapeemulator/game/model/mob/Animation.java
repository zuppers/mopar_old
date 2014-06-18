package net.scapeemulator.game.model.mob;

/**
 * Represents an Animation with an Id and delay. All its attributes are
 * immutable.
 */
public final class Animation {

    private final int id, delay;

    /**
     * Creates an {@link Animation} with the provided id and 0 delay.
     * @param id The id of the {@link Animation}.
     */
    public Animation(int id) {
        this(id, 0);
    }

    /**
     * Creates an {@link Animation} with the provided id, delay.
     *
     * @param id The id of the {@link Animation}.
     * @param delay The delay of the {@link Animation}.
     */
    public Animation(int id, int delay) {
        this.id = id;
        this.delay = delay;
    }

    /**
     * Gets the id of this {@link Animation}.
     * @return The id of this {@link Animation}.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the delay of this {@link Animation}.
     * @return The delay of this {@link Animation}.
     */
    public int getDelay() {
        return delay;
    }

}
