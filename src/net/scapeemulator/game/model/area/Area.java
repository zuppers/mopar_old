package net.scapeemulator.game.model.area;

import net.scapeemulator.game.model.Position;

/**
 * @author Hadyn Richard
 */
public abstract class Area {

    /**
     * Returns whether or not all of the defined tiles (bottom left corner defined by position,
     * length and width by offset) are within this area.
     * 
     * @param position The bottom left corner of the area to check
     * @param offset The area length and width
     * @return true if the square area is entirely within the specified area.
     */
    public boolean allWithinArea(Position position, int offset, int padding) {
        for (int xOffset = 0; xOffset < offset; xOffset++) {
            for (int yOffset = 0; yOffset < offset; yOffset++) {

                /* Check if the position at the new x and y offset is within the area */
                if (!withinArea(position.getX() + xOffset, position.getY() + yOffset, padding)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether or not any of the defined tiles (bottom left corner defined by position,
     * length and width by offset) are within this area.
     * 
     * @param position The bottom left corner of the area to check
     * @param offset The area length and width
     * @return true if the square area is at least partially within the specified area.
     */
    public boolean anyWithinArea(Position position, int offset, int padding) {
        for (int xOffset = 0; xOffset < offset; xOffset++) {
            for (int yOffset = 0; yOffset < offset; yOffset++) {

                /* Check if the position at the new x and y offset is within the area */
                if (withinArea(position.getX() + xOffset, position.getY() + yOffset, padding)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets if a certain coordinate is within the area.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param padding The padding around the area to also include in calculation
     * @return true if the coordinate is within the area
     */
    public abstract boolean withinArea(int x, int y, int padding);

    public abstract Position center();

    public abstract Position randomPosition(int height);
}
