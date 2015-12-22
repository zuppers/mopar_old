package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;

/**
 * Represents an instance of a house window that never converts back to hotspot
 * form.
 * 
 * @author David
 */
public class WindowHotspot extends Hotspot {

    private final int windowId;

    public WindowHotspot(RoomPlaced room, GroundObject object) {
        super(room, object);
        windowId = room.getHouse().getStyle().getWindowId();
    }

    @Override
    public int value() {
        return -1;
    }

    public void setValue(int value) {
    }

    @Override
    public void buildingMode(boolean building) {
        object.setId(windowId);
    }

}
