package net.scapeemulator.game.model;

/**
 * An Entity which has a certain Position.
 */
public abstract class Entity {

    protected Position position;

    /**
     * Retrieve the position of this {@link Entity}
     * @return The current {@link Position}.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set the {@link Position} of this {@link Entity} to position.
     * @param position The {@link Position} to set it to.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

}
