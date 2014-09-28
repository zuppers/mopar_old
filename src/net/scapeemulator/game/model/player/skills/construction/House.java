package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.object.GroundObjectList;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.SceneRebuiltListener;
import net.scapeemulator.game.model.player.skills.construction.hotspot.DoorHotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.Hotspot;
import net.scapeemulator.game.model.player.skills.construction.room.Room;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPosition;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPreview;
import net.scapeemulator.game.model.player.skills.construction.room.RoomType;

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
    public static final int HOUSE_X = 4000;

    /**
     * Y coordinate of the bottom left corner of the center subregion.
     */
    public static final int HOUSE_Y = 4000;

    /**
     * X coordinate of the bottom left corner of the house region.
     */
    public static final int BASE_X = HOUSE_X - ((REGION_SIZE / 2) * Room.ROOM_SIZE);

    /**
     * Y coordinate of the bottom left corner of the house region.
     */
    public static final int BASE_Y = HOUSE_Y - ((REGION_SIZE / 2) * Room.ROOM_SIZE);

    /**
     * The owner of this house.
     */
    private final Player owner;

    /**
     * The list of all objects in this house.
     */
    private GroundObjectList objects;

    /**
     * A 3D array containing all potential rooms in the house region. Note that grass and empty
     * dungeon areas are not null but actually a type of room.
     * 
     * @see Room
     */
    private final RoomPlaced[][][] rooms;

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
     * Set to true when the object list is populated with all of the house hotspots.
     */
    private boolean loaded = false;

    private boolean buildingMode = true;

    private RoomPosition temp;
    private RoomPreview preview;

    /**
     * Constructs a POH with the default settings. Portal location in Rimmington, basic wood style,
     * with a garden and parlour.
     */
    public House(Player owner) {
        this.owner = owner;
        worldPortal = HousePortal.RIMMINGTON;
        style = HouseStyle.FANCY_STONE;
        rooms = new RoomPlaced[4][REGION_SIZE][REGION_SIZE];
        for (int x = 0; x < REGION_SIZE; x++) {
            for (int y = 0; y < REGION_SIZE; y++) {
                rooms[0][x][y] = new RoomPlaced(this, new RoomPosition(0, x, y), RoomType.DUNGEON_CLEAR, Rotation.NONE);
                rooms[1][x][y] = new RoomPlaced(this, new RoomPosition(1, x, y), RoomType.GRASS, Rotation.NONE);
                rooms[2][x][y] = new RoomPlaced(this, new RoomPosition(2, x, y), RoomType.NONE, Rotation.NONE);
                rooms[3][x][y] = new RoomPlaced(this, new RoomPosition(3, x, y), RoomType.NONE, Rotation.NONE);
            }
        }
        rooms[1][4][4] = new RoomPlaced(this, new RoomPosition(1, 4, 4), RoomType.GARDEN, Rotation.NONE);
        rooms[1][4][5] = new RoomPlaced(this, new RoomPosition(1, 4, 5), RoomType.PARLOUR, Rotation.NONE);
    }

    public void loadHouse() {
        objects = new GroundObjectList();
        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    RoomPlaced room = rooms[height][x][y];
                    if (room == null) {
                        continue;
                    }
                    room.createHotspots();
                }
            }
        }
        loaded = true;
    }

    public void handleClick(Player player, int id, Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        int baseRoomX = x - (x % 8);
        int baseRoomY = y - (y % 8);
        int deltaX = x - baseRoomX;
        int deltaY = y - baseRoomY;
        RoomPlaced room = forCoords(pos.getHeight(), x, y);
        if (room == null) {
            return;
        }
        Hotspot hotspot = room.getHotspot(deltaX, deltaY, id);
        if (hotspot instanceof DoorHotspot) {
            DoorHotspot door = (DoorHotspot) hotspot;
            Room adjacent = door.getAdjacent();
            if (adjacent == null) {
                // Should only happen on the edges of the map.
                return;
            }
            if (adjacent.getType() == RoomType.GRASS || adjacent.getType() == RoomType.NONE) {
                temp = adjacent.getRoomPos();
                player.getInterfaceSet().openWindow(Construction.ROOM_CREATE_INTERFACE);
            } else {
                // TODO room removal
            }
            return;
        }
    }

    public void rotatePreview(Rotation rot) {
        if (!buildingMode || preview == null) {
            return;
        }
        preview.rotate(rot);
    }

    public void enterHouse(Player player) {
        // TODO replace that position with a way of finding the portal entrance
        enterHouse(player, new Position(HOUSE_X + 2, HOUSE_Y + 2, 1));
    }

    public void enterHouse(final Player player, final Position newPos) {
        if (buildingMode && player != owner) {
            player.sendMessage("The owner of that house is in building mode.");
            return;
        }
        if (!loaded) {
            loadHouse();
        }
        Position pos = new Position(HOUSE_X, HOUSE_Y, 1);
        player.teleport(pos);
        player.getInterfaceSet().openWindow(Construction.POH_LOADING_INTERFACE);
        RegionPalette palette = getRegionPalette();
        player.setConstructedRegion(palette);
        player.setSceneRebuiltListener(new SceneRebuiltListener() {
            @Override
            public void sceneRebuilt() {
                if (newPos != null) {
                    player.teleport(newPos);
                }
                buildingMode(buildingMode);
                objects.fireAllEvents(player.getGroundObjectSynchronizer());
                objects.addListener(player.getGroundObjectSynchronizer());
                player.getInterfaceSet().closeWindow();
            }
        });

    }

    public void handleSelectRoomInterface(int childId) {
        owner.getInterfaceSet().closeWindow();
        RoomType roomType = RoomType.forInterfaceId(childId);
        if (!buildingMode || temp == null || roomType == null) {
            return;
        }
        (preview = new RoomPreview(this, roomType, temp)).previewRoom();
        temp = null;
        Construction.PREVIEW_DIALOGUE.displayTo(owner);
    }

    public void finishPreview() {
        if (!buildingMode || preview == null) {
            return;
        }
        RoomPosition pos = preview.getRoomPos();
        rooms[pos.getHeight()][pos.getHouseX()][pos.getHouseY()] = new RoomPlaced(this, preview.getRoomPos(), preview.getType(), preview.getRoomRotation());
        loaded = false;
        enterHouse(owner, owner.getPosition());
    }

    public void buildingMode(boolean buildingMode) {
        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    RoomPlaced room = rooms[height][x][y];
                    if (room == null) {
                        continue;
                    }
                    room.buildingMode(buildingMode);
                }
            }
        }
        this.buildingMode = buildingMode;
    }

    /**
     * Gets the Room for the given coordinates in the POH.
     * 
     * @param height the height
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the Room at the given coordinates if it exists, or null if it doesn't
     */
    public RoomPlaced forCoords(int height, int x, int y) {
        int roomX = (int) (4 + ((x - HOUSE_X) / 8.0));
        int roomY = (int) (4 + ((y - HOUSE_Y) / 8.0));
        try {
            return rooms[height][roomX][roomY];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public GroundObjectList getObjectList() {
        return objects;
    }

    public HouseStyle getStyle() {
        return style;
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
                    if (rooms[height][x][y].getType() != RoomType.NONE) {
                        palette.setTile(height, x + PALETTE_OFFSET, y + PALETTE_OFFSET, rooms[height][x][y].getPaletteSourceTile());
                    }
                }
            }
        }
        return palette;
    }
}
