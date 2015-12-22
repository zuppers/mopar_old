package net.scapeemulator.game.model.player.skills.construction.furniture;

import java.util.Arrays;
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
public enum ArmorDisplay implements Furniture {

    /* @formatter:off */        
    MITH(28, 68, 13491, 8270,  new CustomMaterialRequirement("Mithril full helm, plateskirt,<br>and platebody", 15, 1159, 1085, 1121), OAK.req(2)),
    ADDY(28, 88, 13492, 8271,  new CustomMaterialRequirement("Adamant full helm, plateskirt,<br>and platebody", 30, 1161, 1091, 1123), OAK.req(2)),
    RUNE(28, 99, 13493, 8272,  new CustomMaterialRequirement("Rune full helm, plateskirt,<br>and platebody", 45, 1163, 1093, 1127),  OAK.req(2)),
    
    CW_1(28, 0, 13494, 8273,  new CustomMaterialRequirement("Red decorative helm, shield,<br>and body", 15, 4071, 4072, 4069), OAK.req(2)),
    CW_2(28, 0, 13495, 8274, new CustomMaterialRequirement("White decorative helm, shield,<br>and body", 30, 4506, 4507, 4504), OAK.req(2)),
    CW_3(28, 0, 13496, 8275, new CustomMaterialRequirement("Gold decorative helm, shield,<br>and body", 45, 4511, 4512, 4509), OAK.req(2)),
    
    ANTI_SHIELD(47, 0, 13522, 8282,  new CustomMaterialRequirement("Anti-dragon shield", 10, 1540), TEAK.req(3)),
    MOUNTED_GLORY(47, 0, 13523, 8283,  new CustomMaterialRequirement("Amulet of Glory (Uncharged)", 20, 1704), TEAK.req(3)),
    LEGEND_CAPE(47, 0, 13524, 8284,  new CustomMaterialRequirement("Cape of Legends", 30, 1052), TEAK.req(3)),
    
    SILVERLIGHT(42, 0, 13519, 8279,  new CustomMaterialRequirement("Silverlight", 7, 2402), TEAK.req(2)),
    EXCALIBUR(42, 0, 13421, 8280,  new CustomMaterialRequirement("Excalibur", 22, 35), TEAK.req(2)),
    DARKLIGHT(42, 0, 13520, 8281,  new CustomMaterialRequirement("Darklight", 14, 6746), TEAK.req(2));   
    /* @formatter:on */   

    private final int conLvl;
    private final int objectId;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;
    private final CustomMaterialRequirement armor;

    private ArmorDisplay(int conLvl, int smithLvl, int objectId, int itemId, CustomMaterialRequirement armor, MaterialRequirement... mats) {
        this.conLvl = conLvl;
        this.objectId = objectId;
        this.itemId = itemId;
        this.mats = mats;
        this.armor = armor;
        reqs = new Requirements();
        reqs.addRequirement(new SkillRequirement(Skill.CONSTRUCTION, conLvl, true, "build that"));
        if(smithLvl > 0) {
            reqs.addRequirement(new SkillRequirement(Skill.SMITHING, smithLvl, true, "build that", 25));
        }
        reqs.addRequirements(Construction.HAMMER_REQ, Construction.SAW_REQ);
        reqs.addRequirements(mats);
        reqs.addRequirement(armor);
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
            return index == mats.length ? armor : mats[index];
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
        xp += armor.getXp();
        return xp;
    }

    @Override
    public List<Item> getReturnedItems() {
        return Arrays.asList(armor.getItems());
    }
}
