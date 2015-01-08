package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
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
        switch (houseStyle) {
        case BASIC_STONE:
            windowId = 13091;
            break;
        case FANCY_STONE:
            windowId = 13117;
            break;
        case FREMENNIK_WOOD:
            windowId = 13112;
            break;
        case TROPICAL_WOOD:
            windowId = 10816;
            break;
        case WHITEWASHED_STONE:
            windowId = 13005;
            break;
        case BASIC_WOOD:
        default:
            windowId = 13099;
            break;
        }
    }

    @Override
    public void buildingMode(boolean building) {
        object.setId(windowId);
    }

}
