package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;

/**
 * Represents an instance of a house hotspot that can use the furniture building interface.
 * 
 * @author David Insley
 */
public abstract class BuildableHotspot extends Hotspot {

    public BuildableHotspot(RoomPlaced room, GroundObject object) {
        super(room, object);
    }

    public abstract void handleBuildOption(BuildingSession session);

    public abstract void finishRemove(BuildingSession session);
    
    public abstract void handleBuildInterface(BuildingSession session, int itemIndex);
}
