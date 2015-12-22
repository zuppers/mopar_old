package net.scapeemulator.game.model.player.skills.construction.furniture;

import java.util.List;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspotType;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import static net.scapeemulator.game.model.player.skills.construction.furniture.Material.*;

/**
 * @author David Insley
 */
public enum GodFurniture implements Furniture {

    /* @formatter:off */ 
           
    OAK_ALTAR(45, 13179, 8062, OAK.req(4)),
    TEAK_ALTAR(50, 13182, 8063, TEAK.req(4)),
    CLOTH_ALTAR(56, 13185, 8064, TEAK.req(4), CLOTH.req(2)),
    MAHOG_ALTAR(60, 13188, 8065, MAHOGANY.req(4), CLOTH.req(2)),
    STONE_ALTAR(64, 13191, 8066, MAHOGANY.req(6), CLOTH.req(2), LIMESTONE.req(2)),
    MARBLE_ALTAR(70, 13194, 8067, MARBLE.req(2), CLOTH.req(2)),
    GILDED_ALTAR(75, 13197, 8068, MARBLE.req(2), CLOTH.req(2), GOLD_LEAF.req(4)),

    SMALL_STATUE(49, 13271, 8082, LIMESTONE.req(2)),
    MED_STATUE(69, 13272, 8083, MARBLE.req()),
    LARGE_STATUE(89, 13273, 8084, MARBLE.req(3));
    
    /* @formatter:on */

    private final int level;
    private final int baseObjectId;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;

    private enum God {

        SARA(0, 0), ZAM(1, 2), GUTH(2, 1), BOB(0, 3);

        private final int altarOffset;
        private final int statueOffset;

        private God(int altarOffset, int statueOffset) {
            this.altarOffset = altarOffset;
            this.statueOffset = statueOffset;
        }
    }

    private GodFurniture(int level, int baseObjectId, int itemId, MaterialRequirement... mats) {
        this.level = level;
        this.baseObjectId = baseObjectId;
        this.itemId = itemId;
        this.mats = mats;
        reqs = new Requirements();
        reqs.addRequirement(new SkillRequirement(Skill.CONSTRUCTION, level, true, "build that"));
        reqs.addRequirements(Construction.HAMMER_REQ, Construction.SAW_REQ);
        reqs.addRequirements(mats);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getObjectId(RoomPlaced room) {
        FurnitureHotspot iconSpot = room.getFurnitureHotspot(FurnitureHotspotType.CHAPEL_ICON);
        Furniture iconFurn = FurnitureHotspotType.CHAPEL_ICON.getFurniture(iconSpot.getFurnIndex().value());

        God god = God.SARA;
        if (iconFurn != null) {
            switch ((BasicFurniture) iconFurn) {
            case ZAMO_SYMBOL:
            case ZAMO_ICON:
                god = God.ZAM;
                break;
            case GUTH_SYMBOL:
            case GUTH_ICON:
                god = God.GUTH;
                break;
            case BOB_ICON:
                god = God.BOB;
                break;
            default:
                god = God.SARA;
            }
        }

        switch (this) {
        case SMALL_STATUE:
        case MED_STATUE:
        case LARGE_STATUE:
            return baseObjectId + (god.statueOffset * 3);
        default:
            return baseObjectId + god.altarOffset;

        }
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public Requirements getRequirements() {
        return reqs;
    }

    @Override
    public MaterialRequirement material(int index) {
        try {
            return mats[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public double getXp() {
        double xp = 0;
        for (MaterialRequirement mat : mats) {
            xp += mat.getXp();
        }
        return xp;
    }

    @Override
    public List<Item> getReturnedItems() {
        return null;
    }
}
