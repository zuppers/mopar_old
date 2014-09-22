package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.RegionPalette.Tile;

/**
 * @author David Insley
 */
public class House {

    /**
     * Because region palettes are 13x13 but house regions are 9x9, we need a 2 subregion buffer
     * around our house area.
     */
    private static final int PALETTE_OFFSET = 2;
    private static final int HOUSE_X = 4000;
    private static final int HOUSE_Y = 4000;

    private final Room[][][] rooms;
    private HousePortal worldPortal;
    private HouseStyle style;

    // SC: 1926, 5716
    //39619 - 39638
    public House() {
        worldPortal = HousePortal.RIMMINGTON;
        style = HouseStyle.BASIC_WOOD;
        rooms = new Room[4][9][9];
        for (int x = 0; x < rooms[1].length; x++) {
            for (int y = 0; y < rooms[1][x].length; y++) {
                rooms[0][x][y] = new Room(RoomType.DUNGEON_CLEAR);
                rooms[1][x][y] = new Room(RoomType.GRASS);
            }
        }
        rooms[1][4][4] = new Room(RoomType.GARDEN);
        rooms[1][4][5] = new Room(RoomType.PARLOUR);
        rooms[0][4][4] = new Room(RoomType.DUNGEON_STAIRS);
        rooms[0][4][5] = new Room(RoomType.DUNGEON_STAIRS);
    }

    public RegionPalette getRegionPalette() {
        RegionPalette palette = new RegionPalette();
        for (int height = 0; height < rooms.length; height++) {
            for (int x = 0; x < rooms[height].length; x++) {
                for (int y = 0; y < rooms[height][x].length; y++) {
                    if (rooms[height][x][y] != null) {
                        palette.setTile(height, x + PALETTE_OFFSET, y + PALETTE_OFFSET, rooms[height][x][y].toTile());
                    }
                }
            }
        }
        return palette;
    }

    public void loadHouse(Player player) {
        player.teleport(new Position(HOUSE_X, HOUSE_Y, 0));
        player.setConstructedRegion(getRegionPalette());
        // player.teleport(new Position());
    }

    public Room forCoords(int height, int x, int y) throws IllegalArgumentException {
        int roomX = 4 + ((x - HOUSE_X) / 8);
        int roomY = 4 + ((y - HOUSE_Y) / 8);
        try {
            return rooms[height][roomX][roomY];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    // TODO - check for valid next room
    public Room getNextRoom(int height, int x, int y) {
        int roomX = 4 + ((x - HOUSE_X) / 8);
        int roomY = 4 + ((y - HOUSE_Y) / 8);
        if (roomX > 8 || roomY > 8 || roomX < 0 || roomY < 0 || height < 0 || height > 2) {
            throw new IllegalArgumentException("Invalid house coords");
        }
        int botLeftX = HOUSE_X + ((roomX - 4) * 8);
        int botLeftY = HOUSE_Y + ((roomY - 4) * 8);
        if (x == botLeftX) {
            return rooms[height][roomX - 1][roomY];
        }
        if (x == botLeftX + 8) {
            return rooms[height][roomX + 1][roomY];
        }
        if (y == botLeftY) {
            return rooms[height][roomX][roomY - 1];
        }
        if (y == botLeftY + 8) {
            return rooms[height][roomX][roomY + 1];
        }
        return null;
    }

    private class Room {
        RoomType type;
        Rotation rotation;

        Room(RoomType type) {
            this(type, Rotation.NONE);
        }

        Room(RoomType type, Rotation rotation) {
            this.type = type;
            this.rotation = rotation;
        }

        Tile toTile() {
            return new Tile(style.getRoomPosition(type), rotation);
        }
    }
}
