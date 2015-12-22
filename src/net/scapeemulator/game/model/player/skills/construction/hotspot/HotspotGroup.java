package net.scapeemulator.game.model.player.skills.construction.hotspot;

import java.util.ArrayList;
import java.util.List;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.player.skills.construction.furniture.Furniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import net.scapeemulator.game.util.math.MutableInt;

/**
 * @author David Insley
 */
public class HotspotGroup extends BuildableHotspot {

    private final HotspotGroupType type;
    private final List<FurnitureHotspot> hotspots;
    private final MutableInt furnitureIndex;

    public HotspotGroup(RoomPlaced room, HotspotGroupType type) {
        super(room, null);
        this.type = type;
        hotspots = new ArrayList<>();
        furnitureIndex = new MutableInt(-1);
    }

    public void addHotspot(FurnitureHotspot hotspot) {
        hotspots.add(hotspot);
    }

    @Override
    public boolean matchesObject(GroundObject object) {
        for (FurnitureHotspot hotspot : hotspots) {
            if (hotspot.matchesObject(object)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleBuildInterface(BuildingSession session, int itemIndex) {
        session.getBuilder().getInterfaceSet().closeWindow();
        FurnitureInterface inter = type.getSubType().getInterface();
        int furnIdx = inter.getFurnitureIndex(itemIndex);
        Furniture furniture = type.getSubType().getFurniture(furnIdx);
        if (furniture != null) {
            if (furniture.getRequirements().hasRequirementsDisplayOne(session.getBuilder())) {
                furnitureIndex.set(furnIdx);
                furniture.getRequirements().fulfillAll(session.getBuilder());
                session.getBuilder().playAnimation(Construction.BUILD_ANIM);
                session.getBuilder().getSkillSet().addExperience(Skill.CONSTRUCTION, furniture.getXp());
            } else {
                furnitureIndex.set(-1);
            }
        } else {
            furnitureIndex.set(-1);
        }

        session.delayReveal(this);
    }

    @Override
    public void handleBuildOption(BuildingSession session) {
        if (furnitureIndex.value() != -1) {
            session.initFurnitureRemove(this);
        } else {
            type.getSubType().showFurnitureInterface(session.getBuilder());
            session.setFurniturePlaceholder(this);
        }
    }

    @Override
    public void finishRemove(BuildingSession session) {
        session.getBuilder().playAnimation(Construction.REMOVE_ANIM);
        furnitureIndex.set(-1);
        for (FurnitureHotspot hotspot : hotspots) {
            hotspot.object.setId(hotspot.type.getHotspotId());
        }
    }

    @Override
    public int value() {
        return furnitureIndex.value();
    }

    @Override
    public void setValue(int value) {
        furnitureIndex.set(value);
    }

    @Override
    public void buildingMode(boolean building) {
        for (FurnitureHotspot hotspot : hotspots) {
            if (furnitureIndex.value() == -1) {
                if (!building) {
                    hotspot.object.hide();
                } else {
                    hotspot.object.setId(hotspot.type.getHotspotId());
                    hotspot.object.reveal();
                }
            } else {
                hotspot.object.setId(hotspot.type.getFurniture(furnitureIndex.value()).getObjectId(room));
                hotspot.object.reveal();
            }
        }
    }

}
