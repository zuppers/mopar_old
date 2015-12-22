package net.scapeemulator.game.model.player.skills.construction.furniture;

import java.util.List;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import static net.scapeemulator.game.model.player.skills.construction.furniture.Material.*;

/**
 * @author David Insley
 */
public enum StyledFurniture implements Furniture {

    /* @formatter:off */ 
       
    FLOOR_DECORATION(61, new int[]{ 13689, 13686, 13687, 13688, 13684, 13685 }, 8370, MAHOGANY.req(5)),
    TRAPDOORS(68, FLOOR_DECORATION.objectIds, 8372, MAHOGANY.req(5), CLOCKWORK.req(10));

    /* @formatter:on */

    private final int level;
    private final int[] objectIds;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;

    private StyledFurniture(int level, int[] objectIds, int itemId, MaterialRequirement... mats) {
        this.level = level;
        this.objectIds = objectIds;
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
        return objectIds[room.getHouse().getStyle().getId()];
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
