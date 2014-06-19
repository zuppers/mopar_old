package net.scapeemulator.game.model;

/**
 * Represents a certain Animation with an Id, delay and height. All its
 * attributes are immutable.
 *
 */
public final class SpotAnimation {

    private final int id, delay, height;

    /**
     * Creates a {@link SpotAnimation} with the provided id, 0 delay and 0
     * height.
     *
     * @param id The id of the {@link SpotAnimation}.
     */
    public SpotAnimation(int id) {
        this(id, 0, 0);
    }

    /**
     * Creates a {@link SpotAnimation} with the provided id, delay and 0 height.
     *
     * @param id The id of the {@link SpotAnimation}.
     * @param delay The delay of the {@link SpotAnimation}.
     */
    public SpotAnimation(int id, int delay) {
        this(id, delay, 0);
    }

    /**
     * Creates a {@link SpotAnimation} with the provided id, delay and height.
     *
     * @param id The id of the {@link SpotAnimation}.
     * @param delay The delay of the {@link SpotAnimation}.
     * @param height The height of this {@link SpotAnimation}.
     */
    public SpotAnimation(int id, int delay, int height) {
        this.id = id;
        this.delay = delay;
        this.height = height;
    }

    /**
     * Gets the id of this {@link SpotAnimation}.
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the delay of this {@link SpotAnimation}.
     * @return The delay.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Gets the height of this {@link SpotAnimation}.
     * @return The height.
     */
    public int getHeight() {
        return height;
    }
}
