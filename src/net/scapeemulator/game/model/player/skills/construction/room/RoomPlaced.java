package net.scapeemulator.game.model.player.skills.construction.room;

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
import net.scapeemulator.game.model.player.skills.construction.hotspot.Hotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.HotspotType;
import net.scapeemulator.game.model.player.skills.construction.hotspot.WindowHotspot;

/**
 * Represents a 'set in stone' room inside a house that can no longer be rotated or moved, only
 * furniture can change.
 * 
 * @author David Insley
 */
public class RoomPlaced extends Room {

    private Hotspot[][][] hotspots;

    int[][][] furnitureIndices;

    /**
     * Constructs a room with the given type and rotation.
     * 
     * @param house the house this room is in
     * @param type the room type
     * @param rotation the rotation
     */
    public RoomPlaced(House house, RoomPosition roomPos, RoomType type, Rotation rotation) {
        super(house, roomPos, type, rotation);
        furnitureIndices = new int[ROOM_SIZE][ROOM_SIZE][ObjectGroup.values().length];
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
                    HotspotType type = HotspotType.forObjectId(obj.getId());
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

                    int height = roomPos.getHeight();
                    int baseX = roomPos.getBaseX();
                    int baseY = roomPos.getBaseY();

                    /*
                     * Because the door hotspot ids change for each style unlike all others, we have
                     * to make sure we got the right hotspot id and door type. The map reader only
                     * takes the hotspot ids from one style. Hoping to change this eventually.
                     */
                    if (type == HotspotType.DOOR) {
                        DoorType doorType = obj.getId() == DoorType.BASIC_WOOD_1.getHotspotId() ? house.getStyle().getDoorType1() : house.getStyle().getDoorType2();
                        RoomPlaced adjacent = house.forCoords(height, baseX + newX - 1, baseY + newY - 1);
                        if (adjacent == null || adjacent == this) {
                            adjacent = house.forCoords(height, baseX + newX + 1, baseY + newY + 1);
                        }
                        GroundObject placed = house.getObjectList().put(new Position(baseX + newX, baseY + newY, height), doorType.getHotspotId(), newRot, obj.getType());
                        hotspots[newX][newY][i] = new DoorHotspot(doorType, roomType.isSolid(), adjacent == this ? null : adjacent, placed);
                    } else {
                        GroundObject placed = house.getObjectList().put(new Position(baseX + newX, baseY + newY, height), obj.getId(), newRot, obj.getType());
                        if (type == HotspotType.WINDOW) {
                            hotspots[newX][newY][i] = new WindowHotspot(house.getStyle(), placed);
                        } else {
                            hotspots[newX][newY][i] = new FurnitureHotspot(type, furnitureIndices[x][y][i], placed);
                        }
                    }
                }
            }
        }
    }

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

    public Hotspot getHotspot(int x, int y, int id) {
        for (Hotspot hotspot : hotspots[x][y]) {
            if (hotspot != null && hotspot.getHotspotId() == id) {
                return hotspot;
            }
        }
        return null;
    }

    /**
     * Uses the {@link HouseStyle} of the parent {@link House}, room type, and rotation, to create a
     * region tile from the world Construction palette.
     * 
     * @return the generated tile ready to add to a {@link RegionPalette}
     */
    public Tile getPaletteSourceTile() {
        return new Tile(house.getStyle().getRoomPosition(roomType), roomRotation);
    }
}
