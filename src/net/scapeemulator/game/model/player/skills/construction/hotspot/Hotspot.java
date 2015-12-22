package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;

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

    protected final RoomPlaced room;
    
    public Hotspot(RoomPlaced room, GroundObject object) {
        this.room = room;
        this.object = object;
    }

    public abstract int value();
    
    public abstract void setValue(int value);
    
    public abstract void buildingMode(boolean building);

    /**
     * Returns whether or not the given object is part of this hotspot.
     * 
     * @param object the GroundObject to check
     * @return true if the given object is part of this hotspot
     */
    public boolean matchesObject(GroundObject object) {
        return this.object == object;
    }

}
