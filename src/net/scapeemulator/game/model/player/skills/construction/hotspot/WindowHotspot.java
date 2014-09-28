package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.Furniture;
import net.scapeemulator.game.model.player.skills.construction.HouseStyle;

/**
 * Represents an instance of a house window that never converts back to hotspot form.
 * 
 * @author David
 */
public class WindowHotspot extends Hotspot {

    private final int windowId;

    public WindowHotspot(HouseStyle houseStyle, GroundObject object) {
        super(object);
        windowId = Furniture.valueOf(houseStyle.name() + "_WINDOW").getObjectId();
    }

    @Override
    public void buildingMode(boolean building) {
        object.setId(windowId);
    }

    @Override
    public int getHotspotId() {
        return HotspotType.WINDOW.getHotspotId();
    }
}
