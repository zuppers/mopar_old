package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;

/**
 * @author David Insley
 */
public class GroupedHotspot extends Hotspot {

    HotspotType type;
    private int objectId;

    public GroupedHotspot(HotspotType type, GroundObject object) {
        super(object);
        this.type = type;
    }

    public void setFurnitureIndex(int index) {
        this.objectId = objectId;
    }

    @Override
    public void buildingMode(boolean building) {
        if (objectId == -1) {
            if (!building) {
                object.hide();
            } else {
                object.setId(type.getHotspotId());
                object.reveal();
            }
        } else {
            object.setId(objectId);
            object.reveal();
        }

    }

}
