package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.DoorType;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import net.scapeemulator.game.model.player.skills.construction.room.RoomType;

/**
 * This class is poorly designed. Rewrite later.
 * 
 * @author David Insley
 */
public class DoorHotspot extends BuildableHotspot {

    private final int origRot;
    private final DoorType doorType;
    private final boolean solid;
    private final RoomPlaced adjacent;

    public DoorHotspot(RoomPlaced room, DoorType doorType, boolean solid, RoomPlaced adjacent, GroundObject object) {
        super(room, object);
        this.solid = solid;
        this.adjacent = adjacent;
        this.doorType = doorType;
        origRot = object.getRotation();
    }

    /**
     * Gets the room adjacent to this door, used when removing and building new
     * rooms.
     * 
     * @return the room adjacent to this door
     */
    public RoomPlaced getAdjacent() {
        return adjacent;
    }

    @Override
    public void handleBuildOption(BuildingSession session) {
        if (adjacent != null) {
            if (adjacent.getType() == RoomType.GRASS || adjacent.getType() == RoomType.NONE) {
                session.initRoomBuild(adjacent.getRoomPos());
            } else {
                session.initRoomDelete(adjacent.getRoomPos());
            }
        } else {
            session.getBuilder().sendMessage("You can't build a room there.");
        }
    }

    @Override
    public void buildingMode(boolean building) {
        if (building) {
            object.setRotation(origRot);
            object.setId(doorType.getHotspotId());
            object.reveal();
        } else {
            if (!solid || (adjacent != null && adjacent.getType().isSolid())) {
                object.hide();
            } else {
                object.setId(doorType.getClosedId());
                // TODO figure out how to open/close all doors
            }
        }
    }

    @Override
    public int value() {
        return -1;
    }

    public void setValue(int value) {
    }

    @Override
    public void handleBuildInterface(BuildingSession session, int itemIndex) {
    }

    @Override
    public void finishRemove(BuildingSession session) {
    }
}
