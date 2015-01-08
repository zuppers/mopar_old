package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;

/**
 * Represents an instance of a house hotspot.
 * 
 * @author David Insley
 */
public abstract class Hotspot {

    /**
     * The object in the house ObjectList that this hotspot is linked to.
     */
    protected final GroundObject object;

    public Hotspot(GroundObject object) {
        this.object = object;
    }

    public abstract void buildingMode(boolean building);

    public GroundObject getObject() {
        return object;
    }

}
