package net.scapeemulator.game.model.player.skills.construction.room;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.ObjectGroup;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.skills.construction.DoorType;
import net.scapeemulator.game.model.player.skills.construction.House;
import net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspotType;

/**
 * @author David Insley
 */
public class RoomPreview extends Room {

    /**
     * Constructs a room with the given type and rotation.
     * 
     * @param house the house this room is in
     * @param type the room type
     * @param rotation the rotation
     */
    public RoomPreview(House house, RoomType type, RoomPosition roomPos) {
        super(house, roomPos, type, Rotation.NONE);
    }

    public void previewRoom() {
        for (int x = 0; x < Room.ROOM_SIZE; x++) {
            for (int y = 0; y < Room.ROOM_SIZE; y++) {
                GroundObject[] objs = roomType.getHotspotObjs(x, y);
                for (GroundObject obj : objs) {
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
                        newX = Room.ROOM_SIZE - x - length;
                        newY = Room.ROOM_SIZE - y - width;
                        break;
                    case CW_270:
                        newX = Room.ROOM_SIZE - y - width;
                        newY = x;
                        break;
                    case CW_90:
                        newX = y;
                        newY = Room.ROOM_SIZE - x - length;
                        break;
                    case NONE:
                        break;
                    }

                    int height = house.getHeightOffset() + roomPos.getHouseHeight();
                    int baseX = roomPos.getBaseX();
                    int baseY = roomPos.getBaseY();
                    FurnitureHotspotType type = FurnitureHotspotType.forObjectId(obj.getId());
                    if (type == FurnitureHotspotType.DOOR) {
                        DoorType doorType = obj.getId() == DoorType.BASIC_WOOD_1.getHotspotId() ? house.getStyle().getDoorType1() : house.getStyle().getDoorType2();
                        house.getObjectList().put(new Position(baseX + newX, baseY + newY, height), doorType.getHotspotId(), newRot, obj.getType());
                    } else {
                        if (obj.getType().getObjectGroup() == ObjectGroup.GROUP_2) {
                            house.getObjectList().put(new Position(baseX + newX, baseY + newY, height), obj.getId(), newRot, obj.getType());
                        }
                    }
                }
            }
        }
    }

    public void rotate(Rotation rotation) {
        house.clearRoomSpace(roomPos, false);
        roomRotation = roomRotation.rotate(rotation);            
        previewRoom();
    }

}
