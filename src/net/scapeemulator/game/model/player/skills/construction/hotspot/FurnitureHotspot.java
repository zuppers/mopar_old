package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.player.skills.construction.furniture.Furniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.util.math.MutableInt;

/**
 * Represents a single furniture hotspot in a POH.
 * 
 * @author David Insley
 */
public class FurnitureHotspot extends BuildableHotspot {

    private final HotspotType type;
    private final MutableInt furnitureIndex;
    private Furniture furniture;

    public FurnitureHotspot(HotspotType type, MutableInt furnitureIndex, GroundObject object) {
        super(object);
        this.type = type;
        this.furnitureIndex = furnitureIndex;
        try {
            furniture = type.getFurniture()[furnitureIndex.value()];
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void handleBuildInterface(BuildingSession session, int itemIndex) {
        session.getBuilder().getInterfaceSet().closeWindow();
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
        buildingMode(true);
    }

    @Override
    public void handleBuildOption(BuildingSession session) {
        if (furniture != null) {
            furniture = null;
            furnitureIndex.set(-1);
            object.setId(type.getHotspotId());
        } else {
            type.showFurnitureInterface(session.getBuilder());
            session.setFurniturePlaceholder(this);
        }
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

}
