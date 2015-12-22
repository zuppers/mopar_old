package net.scapeemulator.game.model.player.skills.construction.room;

import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.skills.construction.House;

/**
 * @author David Insley
 */
public class Room {

    /**
     * Size of an individual room in tiles.
     */
    public static final int ROOM_SIZE = 8;

    /**
     * The house this room belongs to.
     */
    protected final House house;

    /**
     * The position of this room in the house.
     */
    protected final RoomPosition roomPos;

    /**
     * The type of this room.
     */
    protected final RoomType roomType;

    /**
     * The rotation of this room.
     */
    protected Rotation roomRotation;

    /**
     * Constructs a room with the given type and rotation.
     * 
     * @param house the house this room is in
     * @param type the room type
     * @param rotation the rotation
     */
    public Room(House house, RoomPosition roomPos, RoomType type, Rotation rotation) {
        this.house = house;
        this.roomType = type;
        this.roomRotation = rotation;
        this.roomPos = roomPos;
    }

    public House getHouse() {
        return house;
    }
    
    public RoomType getType() {
        return roomType;
    }

    public RoomPosition getRoomPos() {
        return roomPos;
    }

    public Rotation getRoomRotation() {
        return roomRotation;
    }
}
