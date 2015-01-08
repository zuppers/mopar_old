package net.scapeemulator.game.model.player.skills.construction.furniture;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.construction.Construction;
import static net.scapeemulator.game.model.player.skills.construction.furniture.Material.*;

/**
 * @author David
 *
 */
public enum Furniture {

    /* @formatter:off */
    // GARDEN
    
    // Centrepiece
    EXIT_PORTAL(1, 13405, 8168, IRON.req(10)),
    DECORATIVE_ROCK(5, 13406, 8169, LIMESTONE.req(5)),
    POND(10, 13407, 8170, CLAY.req(10)),
    IMP_STATUE(15, 13408, 8171, LIMESTONE.req(5), CLAY.req(5)),
    GAZEBO(65, 13477, 8192, MAHOGANY.req(8), STEEL.req(4)),
    DUNGEON_ENTRANCE(70, 13409, 8172, MARBLE.req()),
    SMALL_FOUNTAIN(71, 13478, 8193, MARBLE.req()),
    LARGE_FOUNTAIN(75, 13479, 8194, MARBLE.req(2)),
    POSH_FOUNTAIN(81, 13480, 8195, MARBLE.req(3)),
    
    // Garden tree small
    DEAD_TREE(5, 13418, 8173, BAGGED_DEAD.req()),
    NICE_TREE(10, 13419, 8174, BAGGED_NICE.req()),
    OAK_TREE(15, 13420, 8175, BAGGED_OAK.req()),
    WILLOW_TREE(30, 13421, 8176, BAGGED_WILLOW.req()),
    MAPLE_TREE(45, 13423, 8177, BAGGED_MAPLE.req()),
    YEW_TREE(60, 13422, 8178, BAGGED_YEW.req()),
    MAGIC_TREE(75, 13424, 8179, BAGGED_MAGIC.req()),
    
    // Garden tree big
    DEAD_TREE_BIG(5, 13411, 8173, BAGGED_DEAD.req()),
    NICE_TREE_BIG(10, 13412, 8174, BAGGED_NICE.req()),
    OAK_TREE_BIG(15, 13413, 8175, BAGGED_OAK.req()),
    WILLOW_TREE_BIG(30, 13414, 8176, BAGGED_WILLOW.req()),
    MAPLE_TREE_BIG(45, 13415, 8177, BAGGED_MAPLE.req()),
    YEW_TREE_BIG(60, 13416, 8178, BAGGED_YEW.req()),
    MAGIC_TREE_BIG(75, 13417, 8179, BAGGED_MAGIC.req()),  
    
    // Garden small plant 1
    PLANT(1, 13431, 8180, PLANT1.req()),
    SMALL_FERN(6, 13432, 8181, PLANT2.req()),
    FERN(12, 13433, 8182, PLANT3.req()),
    
    // Garden small plant 2
    DOCK_LEAF(1, 13434, 8183, PLANT1.req()),
    THISTLE(6, 13435, 8184, PLANT2.req()),
    REEDS(12, 13436, 8185, PLANT3.req()),
    
    // Garden big plant 1
    FERN_BIG(1, 13425, 8186, PLANT1.req()),
    BUSH(6, 13426, 8187, PLANT2.req()),
    TALL_PLANT(12, 13427, 8188, PLANT3.req()),
    
    // Garden big plant 2
    SHORT_PLANT(1, 13428, 8189, PLANT1.req()),
    LARGE_LEAF_BUSH(6, 13429, 8190, PLANT2.req()),
    HUGE_PLANT(12, 13430, 8191, PLANT3.req()),
    
    // PARLOUR
    
    // Chairs
    CRUDE_CHAIR(1, 13581, 8309, PLANK.req(2)),
    WOODEN_CHAIR(8, 13582, 8310, PLANK.req(3)),
    ROCKING_CHAIR(14, 13583, 8311, PLANK.req(3)),
    OAK_CHAIR(19, 13584, 8312, OAK.req(2)),
    OAK_ARMCHAIR(26, 13585, 8313, OAK.req(3)),
    TEAK_ARMCHAIR(35, 13586, 8314, TEAK.req(2)),
    MAHOGANY_ARMCHAIR(50, 13587, 8315, MAHOGANY.req(2)),
    
    // Fireplaces
    CLAY_FIREPLACE(3, 13609, 8325, CLAY.req(3)),
    LIMESTONE_FIREPLACE(33, 13611, 8326, LIMESTONE.req(2)),
    MARBLE_FIREPLACE(63, 13613, 8327, MARBLE.req()),
    
    // Bookcases
    WOODEN_BOOKCASE(4, 13597, 8319, PLANK.req(4)),
    OAK_BOOKCASE(29, 13598, 8320, OAK.req(3)),
    MAHOGANY_BOOKCASE(40, 13599, 8321, MAHOGANY.req(3)),
    
    // Curtains
    TORN_CURTAINS(2, 13603, 8322, PLANK.req(3), CLOTH.req(3)),
    CURTAINS(18, 13604, 8323, OAK.req(3), CLOTH.req(3)),
    OPULENT_CURTAINS(40, 13605, 8324, TEAK.req(3), CLOTH.req(3)),
    
    // Rugs
    BROWN_RUG(1, 8136, CLOTH.req(2)),
    RUG(1, 8137, CLOTH.req(4)),
    OPULENT_RUG(1, 8138, CLOTH.req(4), GOLD_LEAF.req()),
    
    // KITCHEN
    
    // Tables
    KITCHEN_TABLE(12, 13577, 8246, PLANK.req(3)),
    OAK_KITCHEN_TABLE(32, 13578, 8247, OAK.req(3)),
    TEAK_KITCHEN_TABLE(52, 13579, 8248, TEAK.req(3)),
    
    // Pet blankets
    CAT_BLANKET(5, 13574, 8236, CLOTH.req()),
    CAT_BASKET(19, 13575, 8237, PLANK.req(2)),
    CUSHIONED_BASKET(33, 13576, 8238, PLANK.req(2), WOOL.req(2)),
    
    // Stoves
    FIREPIT(5, 13528, 8216, STEEL.req(), CLAY.req(2)),
    FIREPIT_WITH_HOOK(11, 13529, 8217, STEEL.req(2), CLAY.req(2)),
    FIREPIT_WITH_POT(17, 13531, 8218, STEEL.req(3), CLAY.req(2)),
    SMALL_OVEN(24, 13533, 8219, STEEL.req(4)),
    LARGE_OVEN(29, 13536, 8220, STEEL.req(5)),
    STEEL_RANGE(34, 13539, 8221, STEEL.req(6)),
    FANCY_RANGE(42, 13542, 8222, STEEL.req(8)),
    
    // Shelves
    SHELF(13545, 8223),
    SHELF_2(13546, 8224),
    SHELF_3(13547, 8225),
    OAK_SHELF(13548, 8226),
    OAK_SHELF_2(13549, 8227),
    TEAK_SHELF(13550, 8228),
    TEAK_SHELF_2(13551, 8229),
    
    SHELF_DISHES(13552, 8223),
    SHELF_2_DISHES(13553, 8224),
    SHELF_3_DISHES(13554, 8225),
    OAK_SHELF_DISHES(13555, 8226),
    OAK_SHELF_2_DISHES(13556, 8227),
    TEAK_SHELF_DISHES(13557, 8228),
    TEAK_SHELF_2_DISHES(13558, 8229),
    
    // Larders
    LARDER(9, 13565, 8233, PLANK.req(8)),
    OAK_LARDER(33, 13566, 8234, OAK.req(8)),
    TEAK_LARDER(43, 13567, 8235, TEAK.req(8), CLOTH.req(2)),
    
    // Barrels 
    BEER(7, 13568, 8239, PLANK.req(3)),
    CIDER(12, 13569, 8240),
    ASGARNIAN(18, 13570, 8241),
    GREENMANS(26, 13571, 8242),
    DRAGON_BITTER(36, 13572, 8243),
    CHEFS_DELIGHT(48, 13573, 8244),
    
    // Sinks
    PUMP_AND_DRAIN(7, 13559, 8230, STEEL.req(5)),
    PUMP_AND_TUB(27, 13561, 8231, STEEL.req(10)),
    SINK(47, 13563, 8232, STEEL.req(15));
    
    // STUDY
    
    /* @formatter:on */

    private final int level;
    private final int objectId;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;

    private Furniture(int objectId) {
        this(objectId, -1);
    }

    private Furniture(int level, int objectId, int itemId, MaterialRequirement... mats) {
        this.level = level;
        this.objectId = objectId;
        this.itemId = itemId;
        this.mats = mats;
        reqs = new Requirements();
        reqs.addRequirement(new SkillRequirement(Skill.CONSTRUCTION, level, true, "build that"));
        reqs.addRequirements(Construction.HAMMER_REQ, Construction.SAW_REQ);
        reqs.addRequirements(mats);
    }

    private Furniture(int objectId, int itemId, MaterialRequirement... mats) {
        this(1, objectId, itemId, mats);
    }

    public int getLevel() {
        return level;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getItemId() {
        if (itemId < 0) {
            throw new RuntimeException("attempting to build illegal furniture " + name());
        }
        return itemId;
    }

    public Requirements getRequirements() {
        return reqs;
    }

    public MaterialRequirement material(int index) {
        try {
            return mats[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public double getXp() {
        double xp = 0;
        for (MaterialRequirement mat : mats) {
            xp += mat.getAmount() * mat.getMaterial().getXp();
        }
        return xp;
    }
}
