package net.scapeemulator.game.model.player.skills.construction.furniture;

import java.util.List;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.PlayerVariables.Variable;
import net.scapeemulator.game.model.player.requirement.Requirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import net.scapeemulator.game.model.player.skills.construction.HeraldryCrest;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import static net.scapeemulator.game.model.player.skills.construction.furniture.Material.*;

/**
 * @author David Insley
 */
public enum CrestedFurniture implements Furniture {

    /* @formatter:off */ 
           
    // Decorations
    OAK_DECORATION(16, 13798, 8102, OAK.req(2)),
    TEAK_DECORATION(36, 13814, 8103, TEAK.req(2)),
    GILDED_DECORATION(56, 13782, 8104, MAHOGANY.req(3), GOLD_LEAF.req(2)),
    ROUND_SHIELD(66, 13734, 8105, OAK.req(2)),
    SQUARE_SHIELD(76, 13766, 8106, TEAK.req(4)),
    KITE_SHIELD(86, 13750, 8107, MAHOGANY.req(3));
    
    /* @formatter:on */

    private final int level;
    private final int baseObjectId;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;

    private CrestedFurniture(int level, int baseObjectId, int itemId, MaterialRequirement... mats) {
        this.level = level;
        this.baseObjectId = baseObjectId;
        this.itemId = itemId;
        this.mats = mats;
        reqs = new Requirements();
        reqs.addRequirement(new SkillRequirement(Skill.CONSTRUCTION, level, true, "build that"));
        reqs.addRequirements(Construction.HAMMER_REQ, Construction.SAW_REQ);
        reqs.addRequirements(mats);
        reqs.addRequirement(new Requirement() {

            @Override
            public boolean hasRequirement(Player player) {
                return (HeraldryCrest.forId(player.getVariables().getVar(Variable.HERALDRY_CREST)) != null);
            }

            @Override
            public void displayErrorMessage(Player player) {
                player.sendMessage("You must get a family crest from Sir Renitee in Falador before you can build that.");
            }

            @Override
            public void fulfill(Player player) {
            }

        });
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getObjectId(RoomPlaced room) {
        HeraldryCrest crest = HeraldryCrest.forId(room.getHouse().getOwner().getVariables().getVar(Variable.HERALDRY_CREST));
        if (crest != null) {
            return baseObjectId + crest.getOffset();
        } else {
            System.out.println("Player lost crest?");
            return baseObjectId;
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
