package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;

/**
 * Represents an instance of a house hotspot.
 * 
 * @author David Insley
 */
public abstract class Hotspot {

    protected final GroundObject object;

    public Hotspot(GroundObject object) {
        this.object = object;
    }

    public abstract void buildingMode(boolean building);

    public abstract int getHotspotId();

}
