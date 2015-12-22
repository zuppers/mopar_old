package net.scapeemulator.game.model.player.skills.construction.hotspot;

import java.util.List;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.player.skills.construction.furniture.Furniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import net.scapeemulator.game.util.math.MutableInt;

/**
 * Represents a basic (no special attributes) single hotspot in a house.
 * 
 * @author David Insley
 */
public class FurnitureHotspot extends BuildableHotspot {

    protected final FurnitureHotspotType type;
    protected final MutableInt furnitureIndex;

    public FurnitureHotspot(RoomPlaced room, FurnitureHotspotType type, GroundObject object) {
        super(room, object);
        this.type = type;
        this.furnitureIndex = new MutableInt(-1);
    }

    @Override
    public void handleBuildInterface(BuildingSession session, int itemIndex) {
        session.getBuilder().getInterfaceSet().closeWindow();
        FurnitureInterface inter = type.getInterface();
        int furnIdx = inter.getFurnitureIndex(itemIndex);
        Furniture furniture = type.getFurniture(furnIdx);
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

    public MutableInt getFurnIndex() {
        return furnitureIndex;
    }

    public FurnitureHotspotType getType() {
        return type;
    }

    @Override
    public void handleBuildOption(BuildingSession session) {
        if (furnitureIndex.value() != -1) {
            session.initFurnitureRemove(this);
        } else {
            type.showFurnitureInterface(session.getBuilder());
            session.setFurniturePlaceholder(this);
        }
    }

    @Override
    public void finishRemove(BuildingSession session) {
        Furniture furniture = type.getFurniture(furnitureIndex.value());
        List<Item> returnedItems = furniture.getReturnedItems();
        if (returnedItems != null) {
            if (session.getBuilder().getInventory().freeSlots() < returnedItems.size()) {
                session.getBuilder().sendMessage("You do not have enough free inventory space to remove that.");
                return;
            }
            for (Item item : returnedItems) {
                session.getBuilder().getInventory().add(item);
            }
        }
        session.getBuilder().playAnimation(Construction.REMOVE_ANIM);
        furnitureIndex.set(-1);
        object.setId(type.getHotspotId());
    }

    @Override
    public void buildingMode(boolean building) {
        if (furnitureIndex.value() == -1) {
            if (!building) {
                object.hide();
            } else {
                object.setId(type.getHotspotId());
                object.reveal();
            }
        } else {
            object.setId(type.getFurniture(furnitureIndex.value()).getObjectId(room));
            object.reveal();
        }
    }

    @Override
    public void setValue(int value) {
        furnitureIndex.set(value);
    }

    @Override
    public int value() {
        return furnitureIndex.value();
    }
}
