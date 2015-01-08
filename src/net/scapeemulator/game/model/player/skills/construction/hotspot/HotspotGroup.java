package net.scapeemulator.game.model.player.skills.construction.hotspot;

import java.util.List;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.player.skills.construction.furniture.Furniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.util.math.MutableInt;

/**
 * @author David Insley
 */
public class HotspotGroup extends BuildableHotspot {

    private final GroupedHotspotType type;
    private final List<GroupedHotspot> hotspots;
    private final MutableInt furnitureIndex;

    public HotspotGroup(GroupedHotspotType type, MutableInt furnitureIndex, GroundObject object) {
        super(null);
        hotspots = null;
        this.type = type;
        this.furnitureIndex = furnitureIndex;
    }

    @Override
    public void handleBuildInterface(BuildingSession session, int itemIndex) {
      /*  session.getBuilder().getInterfaceSet().closeWindow();
        FurnitureInterface inter = type.getInterface();
        int furnIdx = inter.getFurnitureIndex(itemIndex);
        try {
            furniture = type.getFurniture()[furnIdx];
            if (furniture.getRequirements().hasRequirementsDisplayOne(session.getBuilder())) {
                furnitureIndex.set(furnIdx);
                furniture.getRequirements().fulfillAll(session.getBuilder());
                session.getBuilder().playAnimation(Construction.BUILD_ANIM);
                session.getBuilder().getSkillSet().addExperience(Skill.CONSTRUCTION, furniture.getXp());
            } else {
                furniture = null;
            }
        } catch (IndexOutOfBoundsException e) {
            furniture = null;
        }
        buildingMode(true);*/
    }

    @Override
    public void handleBuildOption(BuildingSession session) {
       // for (FurnitureHotspot hotspot : hotspots) {
            //hotspot.buildingMode(building);
       // }
        if (furnitureIndex.value() != -1) {
            furnitureIndex.set(-1);
            for (GroupedHotspot hotspot : hotspots) {
                
              //  hotspot.setObjectId(hotspot.hotspotId);
            }
        } else {
           /// type.showFurnitureInterface(session.getBuilder());
            session.setFurniturePlaceholder(this);
        }
    }

    @Override
    public void buildingMode(boolean building) {
        for (Hotspot hotspot : hotspots) {
            hotspot.buildingMode(building);
        }
    }

}
