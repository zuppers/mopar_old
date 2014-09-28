package net.scapeemulator.game.model.object;

/**
 * @author Hadyn Richard
 */
public final class ObjectOrientation {

    // These values are for setRotation(value)
    public static final int WEST = 0;
    public static final int NORTH = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;

    // These values are used in the rotate(value) method in GroundObject
    public static final int ROTATE_CW = 1;
    public static final int ROTATE_CCW = -1;
    public static final int HALF_TURN = 2;

    private ObjectOrientation() {
    }
}
