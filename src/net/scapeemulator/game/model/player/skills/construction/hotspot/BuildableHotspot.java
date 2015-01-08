package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;

/**
 * Represents an instance of a house hotspot that can use the furniture building interface.
 * 
 * @author David Insley
 */
public abstract class BuildableHotspot extends Hotspot {

    public BuildableHotspot(GroundObject object) {
        super(object);
    }

    public abstract void handleBuildOption(BuildingSession session);

    public abstract void handleBuildInterface(BuildingSession session, int itemIndex);
}
