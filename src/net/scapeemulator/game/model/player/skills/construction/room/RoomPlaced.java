package net.scapeemulator.game.model.player.skills.construction.room;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.ObjectGroup;
import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.model.player.RegionPalette.Tile;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.skills.construction.DoorType;
import net.scapeemulator.game.model.player.skills.construction.House;
import net.scapeemulator.game.model.player.skills.construction.HouseStyle;
import net.scapeemulator.game.model.player.skills.construction.hotspot.DoorHotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.HotspotGroupType;
import net.scapeemulator.game.model.player.skills.construction.hotspot.Hotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.HotspotGroup;
import net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspotType;
import net.scapeemulator.game.model.player.skills.construction.hotspot.WindowHotspot;

/**
 * Represents a 'set in stone' room inside a house that can no longer be rotated
 * or moved, only furniture can change.
 * 
 * @author David Insley
 */
public class RoomPlaced extends Room {

    /**
     * Array of hotspots, [x][y][objectgroup]
     */
    private Hotspot[][][] hotspots;

    private Map<HotspotGroupType, HotspotGroup> groupedHotspots;

    /**
     * Constructs a room with the given type and rotation.
     * 
     * @param house the house this room is in
     * @param type the room type
     * @param rotation the rotation
     */
    public RoomPlaced(House house, RoomPosition roomPos, RoomType type, Rotation rotation) {
        super(house, roomPos, type, rotation);
        groupedHotspots = new HashMap<>();
    }

    public void createHotspots() {
        hotspots = new Hotspot[ROOM_SIZE][ROOM_SIZE][ObjectGroup.values().length];
        for (int x = 0; x < ROOM_SIZE; x++) {
            for (int y = 0; y < ROOM_SIZE; y++) {
                GroundObject[] objs = roomType.getHotspotObjs(x, y);
                for (int i = 0; i < objs.length; i++) {
                    GroundObject obj = objs[i];
                    if (obj == null) {
                        continue;
                    }
                    int newX = x;
                    int newY = y;
                    int length = obj.getDefinition().getLength();
                    int width = obj.getDefinition().getWidth();
                    int newRot = (obj.getRotation() + roomRotation.getId()) % 4;
                    if (obj.getRotation() % 2 == 0) {
                        length = obj.getDefinition().getWidth();
                        width = obj.getDefinition().getLength();
                    }
                    switch (roomRotation) {
                    case CW_180:
                        newX = ROOM_SIZE - x - length;
                        newY = ROOM_SIZE - y - width;
                        break;
                    case CW_270:
                        newX = ROOM_SIZE - y - width;
                        newY = x;
                        break;
                    case CW_90:
                        newX = y;
                        newY = ROOM_SIZE - x - length;
                        break;
                    case NONE:
                        break;
                    }

                    int height = house.getHeightOffset() + roomPos.getHouseHeight();
                    int absX = roomPos.getBaseX() + newX;
                    int absY = roomPos.getBaseY() + newY;

                    /*
                     * Because the door hotspot ids change for each style unlike
                     * all others, we have to make sure we got the right hotspot
                     * id and door type. The map reader only takes the hotspot
                     * ids from one style. Hoping to change this eventually.
                     */
                    FurnitureHotspotType type = FurnitureHotspotType.forObjectId(obj.getId());

                    if (type == FurnitureHotspotType.DOOR) {
                        DoorType doorType = obj.getId() == DoorType.BASIC_WOOD_1.getHotspotId() ? house.getStyle().getDoorType1() : house.getStyle().getDoorType2();
                        RoomPlaced adjacent = house.forCoords(height, absX - 1, absY - 1);
                        if (adjacent == null || adjacent == this) {
                            adjacent = house.forCoords(height, absX + 1, absY + 1);
                        }
                        GroundObject placed = house.getObjectList().put(new Position(absX, absY, height), doorType.getHotspotId(), newRot, obj.getType());
                        hotspots[newX][newY][i] = new DoorHotspot(this, doorType, roomType.isSolid(), adjacent == this ? null : adjacent, placed);
                    } else {
                        GroundObject placed = house.getObjectList().put(new Position(absX, absY, height), obj.getId(), newRot, obj.getType());
                        if (type == FurnitureHotspotType.WINDOW) {
                            hotspots[newX][newY][i] = new WindowHotspot(this, placed);
                        } else {
                            FurnitureHotspot hotspot = new FurnitureHotspot(this, type, placed);
                            HotspotGroupType groupType = HotspotGroupType.forType(type);
                            if (groupType == HotspotGroupType.UNGROUPED) {
                                hotspots[newX][newY][i] = hotspot;
                            } else {
                                HotspotGroup group = groupedHotspots.get(groupType);
                                if (group == null) {
                                    group = new HotspotGroup(this, groupType);
                                    groupedHotspots.put(groupType, group);
                                }
                                group.addHotspot(hotspot);
                                hotspots[newX][newY][i] = group;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets all hotspots in this room to the given mode.
     * 
     * @param building true if the hotspots should be in building mode
     */
    public void buildingMode(boolean building) {
        for (int x = 0; x < ROOM_SIZE; x++) {
            for (int y = 0; y < ROOM_SIZE; y++) {
                for (int group = 0; group < ObjectGroup.values().length; group++) {
                    Hotspot hotspot = hotspots[x][y][group];
                    if (hotspot != null) {
                        hotspot.buildingMode(building);
                    }
                }
            }
        }
    }

    /**
     * Searches for a FurnitureHotspot given the type.
     * 
     * @param type the furniture hotspot type to search for
     * @return the hotspot if found, null if not found
     */
    public FurnitureHotspot getFurnitureHotspot(FurnitureHotspotType type) {
        for (int x = 0; x < ROOM_SIZE; x++) {
            for (int y = 0; y < ROOM_SIZE; y++) {
                for (Hotspot hotspot : hotspots[x][y]) {
                    if (hotspot != null && hotspot instanceof FurnitureHotspot) {
                        FurnitureHotspot fSpot = (FurnitureHotspot) hotspot;
                        if (fSpot.getType() == type) {
                            return fSpot;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks to see if a hotspot exists in this room at the given location. If
     * it does, it returns it.
     * 
     * @param x the local x to check
     * @param y the local y to check
     * @param object the object to find
     * @return the hotspot if found, null if not found
     */
    public Hotspot getHotspot(int x, int y, GroundObject object) {
        for (Hotspot hotspot : hotspots[x][y]) {
            if (hotspot != null && hotspot.matchesObject(object)) {
                return hotspot;
            }
        }
        return null;
    }

    /**
     * Uses the {@link HouseStyle} of the parent {@link House}, room type, and
     * rotation, to create a region tile from the world Construction palette.
     * 
     * @return the generated tile ready to add to a {@link RegionPalette}
     */
    public Tile getPaletteSourceTile() {
        return new Tile(house.getStyle().getRoomPosition(roomType), roomRotation);
    }

    public void read(ByteArrayInputStream in) {
        if(hotspots == null) {
            throw new IllegalStateException("cannot parse furniture data before loading room");
        }
        for (int pos = in.read(); pos != -1; pos = in.read()) {
            int val2 = in.read();
            hotspots[pos >> 4][pos & 0xF][val2 & 0xF].setValue(val2 >> 4);   
        }
    }

    public void serialize(ByteArrayOutputStream out) {
        out.write(roomType.getId());
        out.write((roomPos.getHouseX() << 4) | roomPos.getHouseY());
        out.write((roomPos.getHouseHeight() << 4) | roomRotation.getId());
        for (int x = 0; x < ROOM_SIZE; x++) {
            for (int y = 0; y < ROOM_SIZE; y++) {
                for (int grp = 0; grp < ObjectGroup.values().length; grp++) {
                    Hotspot spot = hotspots[x][y][grp];
                    if (spot != null) {
                        out.write((x << 4) | y);
                        out.write((spot.value() << 4) | grp);
                    }
                }
            }
        }
        out.write(-1);
    }
}
