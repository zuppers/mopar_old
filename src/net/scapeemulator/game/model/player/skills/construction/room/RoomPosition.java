package net.scapeemulator.game.model.player.skills.construction.room;

import net.scapeemulator.game.model.player.skills.construction.House;

/**
 * @author David Insley
 */
public class RoomPosition {

    private final int height;
    private final int houseX;
    private final int houseY;

    public RoomPosition(int height, int houseX, int houseY) {
        this.height = height;
        this.houseX = houseX;
        this.houseY = houseY;
    }

    /**
     * Gets the height level of this position relative to the house.
     * 
     * @return the room plane
     */
    public int getHouseHeight() {
        return height;
    }

    /**
     * Gets the house room array x coordinate of this position.
     * 
     * @return the house room x coordinate
     */
    public int getHouseX() {
        return houseX;
    }

    /**
     * Gets the house room array y coordinate of this position.
     * 
     * @return the house room y coordinate
     */
    public int getHouseY() {
        return houseY;
    }

    /**
     * Gets the map x coordinate for the bottom left corner of this room position.
     * 
     * @return the map x coordinate for the bottom left corner of this room position
     */
    public int getBaseX() {
        return House.BASE_X + (houseX * Room.ROOM_SIZE);
    }

    /**
     * Gets the map x coordinate for the bottom left corner of this room position.
     * 
     * @return the map x coordinate for the bottom left corner of this room position
     */
    public int getBaseY() {
        return House.BASE_Y + (houseY * Room.ROOM_SIZE);
    }
}
