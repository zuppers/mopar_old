package net.scapeemulator.game.model.player.skills.construction.furniture;

import java.util.List;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import net.scapeemulator.game.model.player.skills.magic.Rune;
import static net.scapeemulator.game.model.player.skills.construction.furniture.Material.*;

/**
 * @author David
 */
public enum RuneCase implements Furniture {

    /* @formatter:off */        
    CASE_1(41, 14, 13507, 8276, TEAK.req(2), GLASS.req(2), new CustomMaterialRequirement("Air, Water, Earth,<br>and Fire runes", 0, Rune.AIR.getItemId(), Rune.WATER.getItemId(), Rune.EARTH.getItemId(), Rune.FIRE.getItemId())),
    CASE_2(41, 44, 13508, 8277, TEAK.req(2), GLASS.req(2), new CustomMaterialRequirement("Body, Cosmic, Chaos,<br>and Nature runes", 0, Rune.BODY.getItemId(), Rune.COSMIC.getItemId(), Rune.CHAOS.getItemId(), Rune.NATURE.getItemId())),
    CASE_3(41, 77, 13509, 8278, TEAK.req(2), GLASS.req(2), new CustomMaterialRequirement("Law, Blood, Soul,<br>and Death runes", 0, Rune.LAW.getItemId(), Rune.BLOOD.getItemId(), Rune.SOUL.getItemId(), Rune.DEATH.getItemId()));
    /* @formatter:on */

    private final int conLvl;
    private final int objectId;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;

    private RuneCase(int conLvl, int rcLvl, int objectId, int itemId, MaterialRequirement... mats) {
        this.conLvl = conLvl;
        this.objectId = objectId;
        this.itemId = itemId;
        this.mats = mats;
        reqs = new Requirements();
        reqs.addRequirement(new SkillRequirement(Skill.CONSTRUCTION, conLvl, true, "build that"));
        reqs.addRequirement(new SkillRequirement(Skill.RUNECRAFTING, rcLvl, true, "build that", 25));
        reqs.addRequirements(Construction.HAMMER_REQ, Construction.SAW_REQ);
        reqs.addRequirements(mats);
    }

    @Override
    public int getLevel() {
        return conLvl;
    }

    @Override
    public int getObjectId(RoomPlaced room) {
        return objectId;
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
