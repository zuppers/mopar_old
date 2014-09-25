package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.RegionPalette.Tile;

/**
 * Represents an instance of a Construction player owned house (POH).
 * 
 * @author David Insley
 */
public class House {

    /**
     * The size of the actual house region, fit inside of the main 13x13 region palette. Should
     * always be an odd number to guarantee a center subregion.
     */
    private static final int REGION_SIZE = 9;

    /**
     * Because the region palette is 13x13 but house regions can be smaller, we need a subregion
     * buffer around our house area.
     */
    private static final int PALETTE_OFFSET = (13 - REGION_SIZE) / 2;

    /**
     * X coordinate of the bottom left corner of the center subregion.
     */
    private static final int HOUSE_X = 4000;

    /**
     * Y coordinate of the bottom left corner of the center subregion.
     */
    private static final int HOUSE_Y = 4000;

    /**
     * A 3D array containing all potential rooms in the house region. Note that grass and empty
     * dungeon areas are not null but actually a type of room.
     * 
     * @see Room
     */
    private final Room[][][] rooms;

    /**
     * The style of the house, used when calculating palette locations for the construct region
     * packet.
     */
    private HouseStyle style;

    /**
     * The players selected portal in the game world used to access their POH.
     */
    private HousePortal worldPortal;

    /**
     * Constructs a POH with the default settings. Portal location in Rimmington, basic wood style,
     * with a garden and parlour.
     */
    public House() {
        worldPortal = HousePortal.RIMMINGTON;
        style = HouseStyle.BASIC_WOOD;
        rooms = new Room[4][REGION_SIZE][REGION_SIZE];
        for (int x = 0; x < REGION_SIZE; x++) {
            for (int y = 0; y < REGION_SIZE; y++) {
                rooms[0][x][y] = new Room(RoomType.DUNGEON_CLEAR);
                rooms[1][x][y] = new Room(RoomType.GRASS);
            }
        }
        rooms[1][4][4] = new Room(RoomType.GARDEN);
        rooms[1][4][5] = new Room(RoomType.PARLOUR);
    }

    /**
     * Constructs and returns the RegionPalette for this POH.
     * 
     * @return the constructed region palette
     */
    public RegionPalette getRegionPalette() {
        RegionPalette palette = new RegionPalette();
        for (int height = 0; height < rooms.length; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    if (rooms[height][x][y] != null) {
                        palette.setTile(height, x + PALETTE_OFFSET, y + PALETTE_OFFSET, rooms[height][x][y].toTile());
                    }
                }
            }
        }
        return palette;
    }

    /**
     * Gets the Room for the given coordinates in the POH.
     * 
     * @param height the height
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the Room at the given coordinates if it exists, or null if it doesn't
     */
    public Room forCoords(int height, int x, int y) {
        int roomX = 4 + ((x - HOUSE_X) / 8);
        int roomY = 4 + ((y - HOUSE_Y) / 8);
        try {
            return rooms[height][roomX][roomY];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private class Room {

        /**
         * The type of this room.
         */
        RoomType type;

        /**
         * The rotation of this room.
         */
        Rotation rotation;

        /**
         * Constructs a room with the given type and no rotation.
         * 
         * @param type the room type
         */
        Room(RoomType type) {
            this(type, Rotation.NONE);
        }

        /**
         * Constructs a room with the given type and rotation.
         * 
         * @param type the room type
         * @param rotation the rotation
         */
        Room(RoomType type, Rotation rotation) {
            this.type = type;
            this.rotation = rotation;
        }

        /**
         * Uses the {@link HouseStyle} of the parent {@link House}, room type, and rotation, to
         * create a region tile from the world Construction palette.
         * 
         * @return the generated tile ready to add to a {@link RegionPalette}
         */
        Tile toTile() {
            return new Tile(style.getRoomPosition(type), rotation);
        }
    }
}
