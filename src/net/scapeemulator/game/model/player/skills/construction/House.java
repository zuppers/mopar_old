package net.scapeemulator.game.model.player.skills.construction;

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

    private final Room[][][] rooms;
    private HouseStyle style;

    public House() {
        style = HouseStyle.BASIC_WOOD;
        rooms = new Room[4][9][9];
        for (int x = 0; x < rooms[1].length; x++) {
            for (int y = 0; y < rooms[1][x].length; y++) {
                rooms[1][x][y] = new Room(RoomType.DEFAULT);
            }
        }
        rooms[1][5][5] = new Room(RoomType.GARDEN);
        rooms[1][5][6] = new Room(RoomType.PARLOUR);
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

    private class Room {
        RoomType type;
        Rotation rotation;

        Room(RoomType type) {
            this.type = type;
            this.rotation = Rotation.NONE;
        }

        Tile toTile() {
            return new Tile(style.getRoomPosition(type), rotation);
        }
    }
}
