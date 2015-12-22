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
public enum BasicFurniture implements Furniture {

    // ========= GARDEN =========

    // Centerpiece
    EXIT_PORTAL(1, 13405, 8168, IRON.req(10)),
    DECORATIVE_ROCK(5, 13406, 8169, LIMESTONE.req(5)),
    POND(10, 13407, 8170, CLAY.req(10)),
    IMP_STATUE(15, 13408, 8171, LIMESTONE.req(5), CLAY.req(5)),
    DUNGEON_ENTRANCE(70, 13409, 8172, MARBLE.req()),

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

    // ========= PARLOUR =========

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
    BROWN_RUG_CENTER(2, 13590, 8316, CLOTH.req(2)),
    RUG_CENTER(13, 13593, 8317, CLOTH.req(4)),
    OPULENT_RUG_CENTER(65, 13596, 8318, CLOTH.req(4), GOLD_LEAF.req()),

    BROWN_RUG_CORNER(BROWN_RUG_CENTER, 13588),
    RUG_CORNER(RUG_CENTER, 13591),
    OPULENT_RUG_CORNER(OPULENT_RUG_CENTER, 13594),

    BROWN_RUG_SIDE(BROWN_RUG_CENTER, 13589),
    RUG_SIDE(RUG_CENTER, 13592),
    OPULENT_RUG_SIDE(OPULENT_RUG_CENTER, 13595),

    // ========= KITCHEN =========

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
    SHELF(6, 13545, 8223, PLANK.req(3)),
    SHELF_2(12, 13546, 8224, PLANK.req(3), CLAY.req(6)),
    SHELF_3(23, 13547, 8225, PLANK.req(3), CLAY.req(6)),
    OAK_SHELF(34, 13548, 8226, OAK.req(3), CLAY.req(6)),
    OAK_SHELF_2(45, 13549, 8227, OAK.req(3), CLAY.req(6)),
    TEAK_SHELF(56, 13550, 8228, TEAK.req(2), CLAY.req(6)),
    TEAK_SHELF_2(67, 13551, 8229, TEAK.req(3), CLAY.req(6), GOLD_LEAF.req(2)),

    SHELF_DISHES(SHELF, 13552),
    SHELF_2_DISHES(SHELF_2, 13553),
    SHELF_3_DISHES(SHELF_3, 13554),
    OAK_SHELF_DISHES(OAK_SHELF, 13555),
    OAK_SHELF_2_DISHES(OAK_SHELF_2, 13556),
    TEAK_SHELF_DISHES(TEAK_SHELF, 13557),
    TEAK_SHELF_2_DISHES(TEAK_SHELF_2, 13558),

    // Larders
    LARDER(9, 13565, 8233, PLANK.req(8)),
    OAK_LARDER(33, 13566, 8234, OAK.req(8)),
    TEAK_LARDER(43, 13567, 8235, TEAK.req(8), CLOTH.req(2)),

    // Barrels
    BEER(7, 13568, 8239, PLANK.req(3)),
    CIDER(12, 13569, 8240, PLANK.req(3), Material.CIDER.req(8)),
    ASGARNIAN(18, 13570, 8241, OAK.req(3), ASG_ALE.req(8)),
    GREENMANS(26, 13571, 8242, OAK.req(3), GREEN_ALE.req(8)),
    DRAGON_BITTER(36, 13572, 8243, OAK.req(3), DRAG_BITTER.req(8), STEEL.req(2)),
    CHEFS_DELIGHT(48, 13573, 8244, OAK.req(3), CHEF_DELIGH.req(8), STEEL.req(2)),

    // Sinks
    PUMP_AND_DRAIN(7, 13559, 8230, STEEL.req(5)),
    PUMP_AND_TUB(27, 13561, 8231, STEEL.req(10)),
    SINK(47, 13563, 8232, STEEL.req(15)),

    // ========= Dining room =========

    // Dining tables
    WOOD_DINE_TABLE(10, 13293, 8115, PLANK.req(4)),
    OAK_DINE_TABLE(22, 13294, 8116, OAK.req(4)),
    C_OAK_DINE_TABLE(31, 13295, 8117, OAK.req(6)),
    TEAK_DINE_TABLE(38, 13296, 8118, TEAK.req(4)),
    C_TEAK_DINE_TABLE(45, 13297, 8819, TEAK.req(6), CLOTH.req(4)),
    MAHOG_DINE_TABLE(52, 13298, 8120, MAHOGANY.req(6)),
    OPULENT_TABLE(72, 13299, 8121, MAHOGANY.req(6), CLOTH.req(4), GOLD_LEAF.req(4), MARBLE.req(2)),

    // Seating
    WOODEN_BENCH(10, 13300, 8108, PLANK.req(4)),
    OAK_BENCH(22, 13301, 8109, OAK.req(4)),
    CARVED_OAK_BENCH(31, 13302, 8110, OAK.req(4)),
    TEAK_BENCH(38, 13303, 8111, TEAK.req(4)),
    CARVED_TEAK_BENCH(44, 13304, 8112, TEAK.req(4)),
    MAHOG_BENCH(52, 13305, 8113, MAHOGANY.req(4)),
    GILDED_BENCH(61, 13306, 8114, MAHOGANY.req(4), GOLD_LEAF.req(4)),

    // Decorations are in crested furniture

    // Bell pulls
    ROPE_BELL_PULL(26, 13307, 8099, OAK.req(), ROPE.req()),
    BELL_PULL(37, 13308, 8100, TEAK.req(), CLOTH.req(2)),
    POSH_BELL_PULL(60, 13309, 8101, TEAK.req(), CLOTH.req(2), GOLD_LEAF.req(1)),

    // ========= WORKSHOP =========

    // ========= BEDROOM =========

    // Beds
    WOODEN_BED(20, 13148, 8031, PLANK.req(3), CLOTH.req(2)),
    OAK_BED(30, 13149, 8032, OAK.req(3), CLOTH.req(2)),
    LARGE_OAK_BED(34, 13150, 8033, OAK.req(5), CLOTH.req(2)),
    TEAK_BED(40, 13151, 8034, TEAK.req(3), CLOTH.req(2)),
    LARGE_TEAK_BED(45, 13152, 8035, TEAK.req(5), CLOTH.req(2)),
    FOUR_POSTER(53, 13153, 8036, MAHOGANY.req(3), CLOTH.req(2)),
    GILDED_FOUR_POSTER(60, 13154, 8037, MAHOGANY.req(5), CLOTH.req(2), GOLD_LEAF.req(2)),

    // Wardrobes
    SHOE_BOX(20, 13155, 8038, PLANK.req(2)),
    OAK_DRAWERS(27, 13156, 8039, OAK.req(2)),
    OAK_WARDROBE(39, 13157, 8040, OAK.req(3)),
    TEAK_DRAWERS(51, 13158, 8041, TEAK.req(2)),
    TEAK_WARDROBE(63, 13159, 8042, TEAK.req(3)),
    MAHOG_WARDROBE(75, 13160, 8043, MAHOGANY.req(3)),
    GILDED_WARDROBE(87, 13161, 8044, MAHOGANY.req(3), GOLD_LEAF.req()),

    // Dressers
    SHAVER(21, 13162, 8045, PLANK.req(), GLASS.req()),
    OAK_SHAVER(29, 13163, 8046, OAK.req(), GLASS.req()),
    OAK_DRESSER(37, 13164, 8047, OAK.req(2), GLASS.req()),
    TEAK_DRESSER(46, 13165, 8048, TEAK.req(2), GLASS.req()),
    FANCY_TEAK_DRESSER(56, 13166, 8049, TEAK.req(2), GLASS.req(2)),
    MAHOG_DRESSER(64, 13167, 8050, MAHOGANY.req(2), GLASS.req()),
    GILDED_DRESSER(74, 13168, 8051, MAHOGANY.req(2), GLASS.req(2), GOLD_LEAF.req()),

    // Clocks
    OAK_CLOCK(25, 13169, 8052, OAK.req(2), CLOCKWORK.req()),
    TEAK_CLOCK(55, 13170, 8053, TEAK.req(2), CLOCKWORK.req()),
    GILDED_CLOCK(85, 13171, 8054, MAHOGANY.req(2), CLOCKWORK.req(), GOLD_LEAF.req()),

    // ========= SKILL HALL =========

    OAK_STAIRCASE(27, 14497, 8249, OAK.req(10), STEEL.req(4)),
    OAK_STAIRCASE_DOWN(OAK_STAIRCASE, 13498),
    TEAK_STAIRCASE(47, 13499, 8252, TEAK.req(10), STEEL.req(4)),
    TEAK_STAIRCASE_DOWN(TEAK_STAIRCASE, 13500),
    SPIRAL_STAIRCASE(67, 13503, 8258, TEAK.req(10), LIMESTONE.req(7)),
    SPIRAL_STAIRCASE_DOWN(SPIRAL_STAIRCASE, 13504),
    MARBLE_STAIRCASE(82, 13501, 8255, MAHOGANY.req(5), MARBLE.req(5)),
    MARBLE_STAIRCASE_DOWN(MARBLE_STAIRCASE, 13502),
    MARBLE_SPIRAL(97, 13505, 8259, TEAK.req(10), MARBLE.req(7)),
    MARBLE_SPIRAL_DOWN(MARBLE_SPIRAL, 13506),
    
    // Head trophies TODO maybe make these a special type, they give
    // slayer/combat xp
    CRAWLING_HAND(38, 13481, 8260, TEAK.req(2), STUFFED_HAND.req()),
    COCKATRICE_HEAD(38, 13482, 8261, TEAK.req(2), STUFFED_COCKATRICE.req()),
    BASILISK_HEAD(38, 13483, 8262, TEAK.req(2), STUFFED_BASILISK.req()),
    KURASK_HEAD(58, 13484, 8263, MAHOGANY.req(2), STUFFED_KURASK.req()),
    ABYSSAL_HEAD(58, 13485, 8264, MAHOGANY.req(2), STUFFED_ABYSSAL.req()),
    KBD_HEAD(78, 13486, 8265, MAHOGANY.req(2), GOLD_LEAF.req(2), STUFFED_KBD.req()),
    KQ_HEAD(78, 13487, 8266, MAHOGANY.req(2), GOLD_LEAF.req(2), STUFFED_KQ.req()),

    // Fishing trophies
    MOUNTED_BASS(36, 13488, 8267, OAK.req(2), STUFFED_BASS.req()),
    MOUNTED_SWORDFISH(56, 13489, 8268, TEAK.req(2), STUFFED_SWORDFISH.req()),
    MOUNTED_SHARK(76, 13490, 8269, MAHOGANY.req(2), STUFFED_SHARK.req()),

    // ========= GAMES ROOM =========

    // Prize chests
    OAK_CHEST(34, 13385, 8149, OAK.req(4)),
    TEAK_CHEST(44, 13387, 8150, TEAK.req(4), GOLD_LEAF.req()),
    MAHOG_CHEST(54, 13389, 8151, MAHOGANY.req(4), GOLD_LEAF.req()),

    // Attack stones
    CLAY_STONE(39, 13392, 8153, CLAY.req(10)),
    LIMESTONE_STONE(59, 13393, 8154, LIMESTONE.req(10)),
    MARBLE_STONE(79, 13394, 8155, MARBLE.req(4)),

    // Balances
    LESSER_BALANCE(37, 13395, 8156, AIR_RUNE.req(500), WATER_RUNE.req(500), EARTH_RUNE.req(500), FIRE_RUNE.req(500)),
    MEDIUM_BALANCE(57, 13396, 8157, AIR_RUNE.req(1000), WATER_RUNE.req(1000), EARTH_RUNE.req(1000), FIRE_RUNE.req(1000)),
    GREATER_BALANCE(77, 13397, 8158, AIR_RUNE.req(2000), WATER_RUNE.req(2000), EARTH_RUNE.req(2000), FIRE_RUNE.req(2000)),

    // Games
    JESTER(39, 13390, 8159, TEAK.req(4)),
    TREASURE_HUNT(49, 13379, 8160, TEAK.req(8), STEEL.req(4)),
    HANGMAN(59, 13403, 8161, TEAK.req(12), STEEL.req(6)),

    // Range games
    HOOP_AND_STICK(30, 13398, 8162, OAK.req(2)),
    DARTBOARD(54, 13400, 8163, TEAK.req(3), STEEL.req()),
    ARCHERY_TARGET(81, 13402, 8164, TEAK.req(6), STEEL.req(3)),

    // ========= COMBAT ROOM =========
    GLOVE_RACK(34, 13381, 8028, OAK.req(2)),
    WEAPON_RACK(44, 13382, 8029, TEAK.req(2)),
    EXTRA_RACK(54, 13383, 8030, TEAK.req(4), STEEL.req(4)),

    // ========= QUEST HALL =========

    // Portraits
    ARTHUR(35, 13510, 8285, TEAK.req(2), Material.ARTHUR.req()),
    ELENA(35, 13511, 8286, TEAK.req(2), Material.ELENA.req()),
    GIANT_DWARF(35, 13512, 8287, TEAK.req(2), Material.GIANT_DWARF.req()),
    MISCELLANIANS(55, 13513, 8288, MAHOGANY.req(2), Material.MISCELLANIANS.req()),

    // Maps
    SMALL_MAP(38, 13525, 8294, TEAK.req(2), Material.SMALL_MAP.req()),
    MED_MAP(58, 13526, 8295, MAHOGANY.req(3), Material.MED_MAP.req()),
    LARGE_MAP(78, 13527, 8296, MAHOGANY.req(4), Material.LARGE_MAP.req()),

    // Landscapes
    LUMBRIDGE(44, 13517, 8289, TEAK.req(3), Material.LUMBRIDGE.req()),
    DESERT(44, 13514, 8290, TEAK.req(3), Material.DESERT.req()),
    MORYTANIA(44, 13518, 8291, TEAK.req(3), Material.MORYTANIA.req()),
    KARAMJA(65, 13516, 8292, MAHOGANY.req(3), Material.KARAMJA.req()),
    ISAFDAR(65, 13515, 8293, MAHOGANY.req(3), Material.ISAFDAR.req()),

    // ========= STUDY =========

    // Lecterns
    OAK_LECTERN(40, 13642, 8334, OAK.req()),
    EAGLE_LECTERN(47, 13643, 8335, OAK.req(2)),
    DEMON_LECTERN(47, 13644, 8336, OAK.req(2)),
    TEAK_EAGLE(57, 13645, 8337, TEAK.req(2)),
    TEAK_DEMON(57, 13646, 8338, TEAK.req(2)),
    MAHOG_EAGLE(67, 13647, 8339, MAHOGANY.req(2), GOLD_LEAF.req()),
    MAHOG_DEMON(67, 13648, 8340, MAHOGANY.req(2), GOLD_LEAF.req()),

    // Globes
    GLOBE(41, 13649, 8341, OAK.req(3)),
    ORN_GLOBE(50, 13650, 8342, TEAK.req(3)),
    LUNAR_GLOBE(59, 13651, 8343, TEAK.req(3), GOLD_LEAF.req()),
    CELEST_GLOBE(68, 13652, 8344, TEAK.req(3), GOLD_LEAF.req()),
    ARMILLARY(77, 13653, 8345, MAHOGANY.req(2), GOLD_LEAF.req(2), STEEL.req(4)),
    SM_ORRERY(86, 13654, 8346, MAHOGANY.req(3), GOLD_LEAF.req(3)),
    LG_ORRERY(95, 13655, 8347, MAHOGANY.req(3), GOLD_LEAF.req(3)),

    // Crystal balls
    CRYSTAL_BALL(42, 13659, 8351, TEAK.req(3), UNP_ORB.req()),
    ELE_SPHERE(54, 14660, 8352, TEAK.req(3), UNP_ORB.req(), GOLD_LEAF.req()),
    CRYSTAL_OF_POWER(66, 13661, 8353, MAHOGANY.req(2), UNP_ORB.req(), GOLD_LEAF.req(2)),

    // Wall charts
    ALCH_CHART(43, 13662, 8354, CLOTH.req(2)),
    ASTRO_CHART(63, 13663, 8355, CLOTH.req(3)),
    INFERN_CHART(83, 13664, 8356, CLOTH.req(4)),

    // Telescopes
    WOOD_TELESCOPE(44, 13656, 8348, OAK.req(2), GLASS.req()),
    TEAK_TELESCOPE(64, 13657, 8349, TEAK.req(2), GLASS.req()),
    MAHOG_TELESCOPE(84, 13658, 8350, MAHOGANY.req(2), GLASS.req()),

    // ========= COSTUME ROOM =========

    // Armour case
    OAK_ARMOR_CASE(46, 18778, 9826, OAK.req(3)),
    TEAK_ARMOR_CASE(64, 18780, 9827, TEAK.req(3)),
    MAHOG_ARMOR_CASE(82, 18782, 9828, MAHOGANY.req(3)),

    // Magic wardrobe
    MAGIC_OAK_WDROBE(42, 18784, 9829, OAK.req(4)),
    MAGIC_C_OAK_WDROBE(51, 18786, 9830, OAK.req(6)),
    MAGIC_TEAK_WDROBE(60, 18788, 9831, TEAK.req(4)),
    MAGIC_C_TEAK_WDROBE(69, 18790, 9832, TEAK.req(6)),
    MAGIC_MAHOG_WDROBE(78, 18792, 9833, MAHOGANY.req(4)),
    MAGIC_GILDED_WDROBE(87, 18794, 9834, MAHOGANY.req(4), GOLD_LEAF.req()),
    MAGIC_MARBLE_WDROBE(96, 18796, 9835, MARBLE.req()),

    // Toy box
    OAK_TOY_BOX(50, 18798, 9836, OAK.req(2)),
    TEAK_TOY_BOX(68, 18800, 9837, TEAK.req(2)),
    MAHOGANY_TOY_BOX(86, 18802, 9838, MAHOGANY.req(2)),

    // Treasure chest
    OAK_TREASURE_CHEST(48, 18804, 9839, OAK.req(2)),
    TEAK_TREASURE_CHEST(66, 18806, 9840, TEAK.req(2)),
    MAHOG_TREASURE_CHEST(84, 18808, 9841, MAHOGANY.req(2)),

    // Cape rack
    OAK_RACK(54, 18766, 9817, OAK.req(4)),
    TEAK_RACK(63, 18767, 9818, TEAK.req(4)),
    MAHOGANY_RACK(72, 18768, 9819, MAHOGANY.req(4)),
    GILDED_RACK(81, 18769, 9820, MAHOGANY.req(4), GOLD_LEAF.req()),
    MARBLE_RACK(90, 18770, 9821, MARBLE.req()),
    MAGIC_RACK(99, 18771, 9822, MAGIC_STONE.req()),

    // Costume box
    OAK_COSTUME_BOX(44, 18772, 9823, OAK.req(2)),
    TEAK_COSTUME_BOX(62, 18774, 9824, TEAK.req(2)),
    MAHOG_COSTUME_BOX(80, 18776, 9825, MAHOGANY.req(2)),

    // ========= CHAPEL =========

    // Altars/statues/windows are GodFurniture

    // Icon
    SARA_SYMBOL(48, 13172, 8055, OAK.req(2)),
    ZAMO_SYMBOL(48, 13173, 8056, OAK.req(2)),
    GUTH_SYMBOL(48, 13174, 8057, OAK.req(2)),
    SARA_ICON(59, 13175, 8058, TEAK.req(4), GOLD_LEAF.req(2)),
    ZAMO_ICON(59, 13176, 8059, TEAK.req(4), GOLD_LEAF.req(2)),
    GUTH_ICON(59, 13177, 8060, TEAK.req(4), GOLD_LEAF.req(2)),
    BOB_ICON(71, 13178, 8061, MAHOGANY.req(4), GOLD_LEAF.req(2)),

    // Burners
    WOOD_TORCH(45, 13200, 8069, PLANK.req(2)),
    STEEL_TORCH(49, 13202, 8070, STEEL.req(2)),
    STEEL_CNDL(53, 13204, 8071, STEEL.req(6), CANDLE.req(6)),
    GOLD_CNDL(57, 13206, 8072, GOLD.req(6), CANDLE.req(6)),
    INCENSE_BURNER(61, 13208, 8073, OAK.req(2), STEEL.req(2)),
    MAHOG_BURNER(65, 13210, 8074, MAHOGANY.req(4), STEEL.req(2)),
    MARBLE_BURNER(69, 13212, 8075, MARBLE.req(2), STEEL.req(2)),

    // Instruments
    WINDCHIMES(49, 13214, 8079, OAK.req(4), STEEL.req(4)),
    BELLS(58, 13215, 8080, TEAK.req(4), STEEL.req(6)),
    ORGAN(69, 13216, 8081, MAHOGANY.req(4), STEEL.req(6)),

    // =========PORTAL CHAMBER =========

    // =========FORMAL GARDEN =========

    // Plant
    SUNFLOWER(66, 13443, 8213, Material.SUNFLOWER.req()),
    SMALL_SUNFLOWER(SUNFLOWER, 13446),
    MARIGOLD(71, 13444, 8214, Material.MARIGOLDS.req()),
    SMALL_MARIGOLD(MARIGOLD, 13447),
    ROSES(76, 13445, 8215, Material.ROSES.req()),
    SMALL_ROSES(ROSES, 13448),

    // Plant 2
    ROSEMARY(66, 13437, 8210, Material.ROSEMARY.req()),
    SMALL_ROSEMARY(ROSEMARY, 13440),
    DAFFODILS(71, 13438, 8211, Material.DAFFODILS.req()),
    SMALL_DAFFODILS(DAFFODILS, 13441),
    BLUEBELLS(76, 13439, 8212, Material.BLUEBELLS.req()),
    SMALL_BLUEBELLS(BLUEBELLS, 13442),

    // Centerpiece
    GAZEBO(65, 13477, 8192, MAHOGANY.req(8), STEEL.req(4)),
    SMALL_FOUNTAIN(71, 13478, 8193, MARBLE.req()),
    LARGE_FOUNTAIN(75, 13479, 8194, MARBLE.req(2)),
    POSH_FOUNTAIN(81, 13480, 8195, MARBLE.req(3)),

    // Fencing
    BOUNDARY_STONES(55, 13449, 8196, CLAY.req(10)),
    WOOD_FENCE(59, 13450, 8197, PLANK.req(10)),
    STONE_WALL(63, 13451, 8198, LIMESTONE.req(10)),
    IRON_RAIL(67, 13452, 8199, IRON.req(10), LIMESTONE.req(6)),
    PICKET_FENCE(71, 13453, 8200, OAK.req(10), STEEL.req(2)),
    GARDEN_FENCE(75, 13454, 8201, TEAK.req(10), STEEL.req(2)),
    MARBLE_WALL(79, 13455, 8202, MARBLE.req(8)),

    // Hedges
    THORNY_END(56, 8203, 13456, Material.THORNY_HEDGE.req()),
    THORNY_CORNER(THORNY_END, 13457),
    THORNY_CENTER(THORNY_END, 13458),
    NICE_END(60, 8204, 13459, Material.NICE_HEDGE.req()),
    NICE_CORNER(NICE_END, 13460),
    NICE_CENTER(NICE_END, 13461),
    BOX_END(64, 8205, 13462, Material.BOX_HEDGE.req()),
    BOX_CORNER(BOX_END, 13463),
    BOX_CENTER(BOX_END, 13464),
    TOPIARY_END(68, 8206, 13465, Material.TOPIARY_HEDGE.req()),
    TOPIARY_CORNER(TOPIARY_END, 13466),
    TOPIARY_CENTER(TOPIARY_END, 13467),
    FANCY_END(72, 8207, 13468, Material.FANCY_HEDGE.req()),
    FANCY_CORNER(FANCY_END, 13469),
    FANCY_CENTER(FANCY_END, 13470),
    TALL_FANCY_END(76, 8208, 13471, Material.TALL_FANCY_HEDGE.req()),
    TALL_FANCY_CORNER(TALL_FANCY_END, 13472),
    TALL_FANCY_CENTER(TALL_FANCY_END, 13473),
    TALL_BOX_END(80, 8209, 13474, Material.TALL_BOX_HEDGE.req()),
    TALL_BOX_CORNER(TALL_BOX_END, 13475),
    TALL_BOX_CENTER(TALL_BOX_END, 13476),

    // ========= THRONE ROOM =========

    // Thrones
    OAK_THRONE(60, 13665, 8357, OAK.req(5), MARBLE.req()),
    TEAK_THRONE(67, 13666, 8358, TEAK.req(5), MARBLE.req(2)),
    MAHOGANY_THRONE(74, 13667, 8359, MAHOGANY.req(5), MARBLE.req(3)),
    GILDED_THRONE(81, 13668, 8360, MAHOGANY.req(5), MARBLE.req(2), GOLD_LEAF.req(3)),
    SKELETON_THRONE(88, 13669, 8361, MAGIC_STONE.req(5), MARBLE.req(4), BONE.req(5), SKULL.req(2)),
    CRYSTAL_THRONE(95, 13670, 8362, MAGIC_STONE.req(15)),
    DEMON_THRONE(99, 13671, 8363, MAGIC_STONE.req(25)),

    // Levers
    OAK_LEVER(68, 13674, 8364, OAK.req(5)),
    TEAK_LEVER(78, 13672, 8365, TEAK.req(5)),
    MAHOG_LEVER(88, 13673, 8366, MAHOGANY.req(5)),

    // Wall decorations are all in CrestedFurniture

    // Trapdoors
    OAK_TRAPDOOR(68, 13675, 8367, OAK.req(5)),
    TEAK_TRAPDOOR(78, 13676, 8368, TEAK.req(5)),
    MAHOG_TRAPDOOR(88, 13677, 8369, MAHOGANY.req(5)),

    // Floor
    // FLOOR_DECORATION (styled furniture)
    // STEEL_CAGE(68, 13681, MAHOGANY.req(5), STEEL.req(20)),
    // TRAPDOORS (styled furniture)
    // MAGIC_CAGE(82, 13682, MAHOGANY.req(4), MAGIC_STONE.req(2)),
    // GREATER_MAGIC_CAGE(89, 13683, MAHOGANY.req(5), MAGIC_STONE.req(4)),

    // ========= OUBLIETTE =========

    // Decoration
    DECORATIVE_BLOOD(72, 13312, 8125, RED_DYE.req(4)),
    DECORATIVE_PIPE(83, 13311, 8126, STEEL.req(6)),
    HANGING_SKELETON(94, 13310, 8127, SKULL.req(2), BONE.req(6)),

    // Lighting
    LIGHT_CANDLE(72, 13342, 8128, OAK.req(4), LIT_CANDLE.req(4)),
    LIGHT_TORCH(84, 13341, 8129, OAK.req(4), LIT_TORCH.req(4)),
    SKULL_TORCH(94, 13343, 8130, OAK.req(4), LIT_TORCH.req(4), SKULL.req(4)),

    // Cages
    OAK_CAGE(65, 13313, 8297, OAK.req(10), STEEL.req(2)),
    OAK_CAGE_DOOR(OAK_CAGE, 13314),
    OAK_STEEL_CAGE(70, 13316, 8298, OAK.req(10), STEEL.req(10)),
    OAK_STEEL_CAGE_DOOR(OAK_STEEL_CAGE, 13317),
    STEEL_CAGE(75, 13319, 8299, STEEL.req(20)),
    STEEL_CAGE_DOOR(STEEL_CAGE, 13320),
    SPIKED_CAGE(80, 13322, 8300, STEEL.req(25)),
    SPIKED_CAGE_DOOR(SPIKED_CAGE, 13323),
    BONE_CAGE(85, 13325, 8301, OAK.req(10), BONE.req(10)),
    BONE_CAGE_DOOR(BONE_CAGE, 13326),
      
    //13373   Rocnar      len/width:2/2   solid/impen:false/true  anim:3707   opts:[null, null, null, null, Remove]

    // Guards
    SKELE_GUARD(70, 13366, 8131, new CustomMaterialRequirement("50,000 coins", 223, new Item(995, 50000))),
    DOG_GUARD(74, 13367, 8132, new CustomMaterialRequirement("75,000 coins", 273, new Item(995, 75000))),
    HOBGOB_GUARD(78, 13368, 8133, new CustomMaterialRequirement("100,000 coins", 316, new Item(995, 100000))),
    BABY_DRAG_GUARD(82, 13372, 8134, new CustomMaterialRequirement("150,000 coins", 387, new Item(995, 150000))),
    SPIDER_GUARD(86, 13370, 8135, new CustomMaterialRequirement("200,000 coins", 447, new Item(995, 200000))),
    TROLL_GUARD(90, 13369, 8136, new CustomMaterialRequirement("1,000,000 coins", 1000, new Item(995, 1000000))),
    HELLHOUND_GUARD(94, 2715, 8137, new CustomMaterialRequirement("5,000,000 coins", 2236, new Item(995, 5000000))),
    
    // TREASURE ROOM
    
    // Teasure chest
    WOOD_CRATE(75, 13283, 8148, PLANK.req(5)),
    MAGIC_CHEST(91, 13291, 8152, MAGIC_STONE.req()),   
    
    // Guardian
    DEMON(75, 13378, 8138, new CustomMaterialRequirement("500,000 coins", 707, new Item(995, 500000))),
    KALPHITE(80, 13374, 8139, new CustomMaterialRequirement("750,000 coins", 866, new Item(995, 750000))),
    TOK_XIL(85, 13377, 8140, new CustomMaterialRequirement("5,000,000 coins", 2236, new Item(995, 5000000))),
    DAGANNOTH(90, 13376, 8141, new CustomMaterialRequirement("7,500,000 coins", 2738, new Item(995, 7500000))),
    STEEL_DRAG(95, 13375, 8142, new CustomMaterialRequirement("10,000,000 coins", 3162, new Item(995, 10000000)));
            
    private final int level;
    private final int objectId;
    private final int itemId;
    private final Requirements reqs;
    private final MaterialRequirement[] mats;

    private BasicFurniture(BasicFurniture other, int objectId) {
        this(other.level, objectId, other.itemId, other.mats);
    }

    private BasicFurniture(int level, int objectId, int itemId, MaterialRequirement... mats) {
        this.level = level;
        this.objectId = objectId;
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
