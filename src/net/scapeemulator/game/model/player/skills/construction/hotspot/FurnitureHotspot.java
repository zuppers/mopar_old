package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.construction.Furniture;

/**
 * Represents an instance of a house hotspot.
 * 
 * @author David
 */
public class FurnitureHotspot extends Hotspot {

    protected final HotspotType type;
    private final Furniture furniture;

    public FurnitureHotspot(HotspotType type, int i, GroundObject object) {
        super(object);
        this.type = type;
        furniture = (i < 0 || i >= type.getFurniture().length) ? null : type.getFurniture()[i];
    }

    @Override
    public void buildingMode(boolean building) {
        if (furniture == null) {
            if (!building) {
                object.hide();
            } else {
                object.setId(type.getHotspotId());
                object.reveal();
            }
        } else {
            object.setId(furniture.getObjectId());
            object.reveal();
        }
    }

    @Override
    public int getHotspotId() {
        return type.getHotspotId();
    }

}
