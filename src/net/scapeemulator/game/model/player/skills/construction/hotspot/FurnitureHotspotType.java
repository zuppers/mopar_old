package net.scapeemulator.game.model.player.skills.construction.hotspot;

import net.scapeemulator.game.model.player.skills.construction.furniture.BasicFurniture;
import static net.scapeemulator.game.model.player.skills.construction.furniture.BasicFurniture.*;
import static net.scapeemulator.game.model.player.skills.construction.furniture.GodFurniture.*;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.construction.furniture.ArmorDisplay;
import net.scapeemulator.game.model.player.skills.construction.furniture.CrestedFurniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.Furniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.model.player.skills.construction.furniture.MaterialRequirement;
import net.scapeemulator.game.model.player.skills.construction.furniture.RuneCase;
import net.scapeemulator.game.model.player.skills.construction.furniture.StyledFurniture;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemsMessage;

/**
 * @author David Insley
 */
public enum FurnitureHotspotType {

    /* @formatter:off */
    WINDOW(13830),
    DOOR(-1),
    
    // Garden
    CENTREPIECE(15361, EXIT_PORTAL, DECORATIVE_ROCK, POND, IMP_STATUE, DUNGEON_ENTRANCE),
    BIG_TREE(15362, DEAD_TREE_BIG, NICE_TREE_BIG, OAK_TREE_BIG, WILLOW_TREE_BIG, MAPLE_TREE_BIG, YEW_TREE_BIG, MAGIC_TREE_BIG),
    TREE(15363, DEAD_TREE, NICE_TREE, OAK_TREE, WILLOW_TREE, MAPLE_TREE, YEW_TREE, MAGIC_TREE),
    BIG_PLANT(15364, FERN_BIG, BUSH, TALL_PLANT),
    BIG_PLANT_2(15365, SHORT_PLANT, LARGE_LEAF_BUSH, HUGE_PLANT),
    SMALL_PLANT(15366, PLANT, SMALL_FERN, FERN),
    SMALL_PLANT_2(15367, DOCK_LEAF, THISTLE, REEDS),
    
    // Parlour    
    CHAIR(15410, CRUDE_CHAIR, WOODEN_CHAIR, ROCKING_CHAIR, OAK_CHAIR, OAK_ARMCHAIR, TEAK_ARMCHAIR, MAHOGANY_ARMCHAIR),
    CHAIR_2(15411, CHAIR.furniture),
    CHAIR_3(15412, CHAIR.furniture),
    RUG_CENTER(15413, BROWN_RUG_CENTER, BasicFurniture.RUG_CENTER, OPULENT_RUG_CENTER),
    RUG_SIDE(15414, BROWN_RUG_SIDE, BasicFurniture.RUG_SIDE, OPULENT_RUG_SIDE),
    RUG_CORNER(15415, BROWN_RUG_CORNER, BasicFurniture.RUG_CORNER, OPULENT_RUG_CORNER),
    BOOKCASE(15416, WOODEN_BOOKCASE, OAK_BOOKCASE, MAHOGANY_BOOKCASE), 
    BOOKCASE_2(15417, BOOKCASE.furniture), 
    FIREPLACE(15418, CLAY_FIREPLACE, LIMESTONE_FIREPLACE, MARBLE_FIREPLACE),
    PARLOUR_CURTAINS(15419, TORN_CURTAINS, CURTAINS, OPULENT_CURTAINS),
    
    // Kitchen
    STOVE(15398, FIREPIT, FIREPIT_WITH_HOOK, FIREPIT_WITH_POT, SMALL_OVEN, LARGE_OVEN, STEEL_RANGE, FANCY_RANGE),
    SHELVES_DISHES(15399, SHELF_DISHES, SHELF_2_DISHES, SHELF_3_DISHES, OAK_SHELF_DISHES, OAK_SHELF_2_DISHES, TEAK_SHELF_DISHES, TEAK_SHELF_2_DISHES),
    SHELVES(15400, SHELF, SHELF_2, SHELF_3, OAK_SHELF, OAK_SHELF_2, TEAK_SHELF, TEAK_SHELF_2),
    BARREL(15401, BEER, CIDER, ASGARNIAN, GREENMANS, DRAGON_BITTER, CHEFS_DELIGHT),
    PET_BASKET(15402, CAT_BLANKET, CAT_BASKET, CUSHIONED_BASKET),
    LARDER(15403, BasicFurniture.LARDER, OAK_LARDER, TEAK_LARDER),
    SINK(15404, PUMP_AND_DRAIN, PUMP_AND_TUB, BasicFurniture.SINK),
    KITCHEN_TABLE(15405, BasicFurniture.KITCHEN_TABLE, OAK_KITCHEN_TABLE, TEAK_KITCHEN_TABLE),
       
    // Dining room        
    DINING_TABLE(15298, WOOD_DINE_TABLE, OAK_DINE_TABLE, C_OAK_DINE_TABLE, TEAK_DINE_TABLE, C_TEAK_DINE_TABLE, MAHOG_DINE_TABLE, OPULENT_TABLE),
    DINING_SEATING(15299, WOODEN_BENCH, OAK_BENCH, CARVED_OAK_BENCH, TEAK_BENCH, CARVED_TEAK_BENCH, MAHOG_BENCH, GILDED_BENCH),
    DINING_SEATING_2(15300, DINING_SEATING.furniture),
    DINING_FIREPLACE(15301, FIREPLACE.furniture),
    DINING_CURTAINS(15302, PARLOUR_CURTAINS.furniture),
    DINING_DECORATION(15303, CrestedFurniture.OAK_DECORATION, CrestedFurniture.TEAK_DECORATION, CrestedFurniture.GILDED_DECORATION),
    BELL_PULL(15304, ROPE_BELL_PULL, BasicFurniture.BELL_PULL, POSH_BELL_PULL),
    
    // Workshop
    // Tool space = 15443-15447
    // Clockmaking bench 15441
    // WORKBENCH = 15439
    REPAIR_BENCH(15448),
    HERLADRY_STAND(15450),
    
    // Bedroom
    BED(15260, WOODEN_BED, OAK_BED, LARGE_OAK_BED, TEAK_BED, LARGE_TEAK_BED, FOUR_POSTER, GILDED_FOUR_POSTER),
    WARDROBE(15261, SHOE_BOX, OAK_DRAWERS, OAK_WARDROBE, TEAK_DRAWERS, TEAK_WARDROBE, MAHOG_WARDROBE, GILDED_WARDROBE),
    DRESSER(15262, SHAVER, OAK_SHAVER, OAK_DRESSER, TEAK_DRESSER, FANCY_TEAK_DRESSER, MAHOG_DRESSER, GILDED_DRESSER),
    BEDROOM_CURTAINS(15263, PARLOUR_CURTAINS.furniture),
    RUG_2_CENTER(15264, RUG_CENTER.furniture),
    RUG_2_SIDE(15265, RUG_SIDE.furniture),
    RUG_2_CORNER(15266, RUG_CORNER.furniture),
    BEDROOM_FIREPLACE(15267, FIREPLACE.furniture),
    CLOCK(15268, OAK_CLOCK, TEAK_CLOCK, GILDED_CLOCK),
    
    // Skill Hall
    HEAD_TROPHY(15382, CRAWLING_HAND, COCKATRICE_HEAD, BASILISK_HEAD, KURASK_HEAD, ABYSSAL_HEAD, KBD_HEAD, KQ_HEAD),
    FISHING_TROPHY(15383, MOUNTED_BASS, MOUNTED_SWORDFISH, MOUNTED_SHARK),
    METAL_ARMOR(15384, ArmorDisplay.MITH, ArmorDisplay.ADDY, ArmorDisplay.RUNE),
    CW_ARMOR(15385, ArmorDisplay.CW_1, ArmorDisplay.CW_2, ArmorDisplay.CW_3),
    RUNE_CASE(15386, RuneCase.CASE_1, RuneCase.CASE_2, RuneCase.CASE_3),
    
    // Games room
    GAME(15342, JESTER, TREASURE_HUNT, HANGMAN),
    PRIZE_CHEST(15343, OAK_CHEST, TEAK_CHEST, MAHOG_CHEST),
    ATTACK_STONE(15344, CLAY_STONE, LIMESTONE_STONE, MARBLE_STONE),
    ELEMENTAL_BALANCE(15345, LESSER_BALANCE, MEDIUM_BALANCE, GREATER_BALANCE),
    RANGING_GAME(15346, HOOP_AND_STICK, DARTBOARD, ARCHERY_TARGET),
    
    // Combat room
    RING(15277),
    RACK(15296, GLOVE_RACK, WEAPON_RACK, EXTRA_RACK),
    COMBAT_DECORATION(15297, DINING_DECORATION.furniture),
    
    // Quest hall
    PORTRAIT(15392, ARTHUR, ELENA, GIANT_DWARF, MISCELLANIANS),
    LANDSCAPE(15393, LUMBRIDGE, DESERT, MORYTANIA, KARAMJA, ISAFDAR),
    GUILD_TROPHY(15394, ArmorDisplay.ANTI_SHIELD, ArmorDisplay.MOUNTED_GLORY, ArmorDisplay.LEGEND_CAPE),
    SWORD(15395, ArmorDisplay.SILVERLIGHT, ArmorDisplay.EXCALIBUR, ArmorDisplay.DARKLIGHT),
    MAP(15396, SMALL_MAP, MED_MAP, LARGE_MAP),
    BOOKCASE_QUEST(15397, BOOKCASE.furniture),

    // Study
    LECTERN(15420, OAK_LECTERN, EAGLE_LECTERN, DEMON_LECTERN, TEAK_EAGLE, TEAK_DEMON, MAHOG_EAGLE, MAHOG_DEMON),
    GLOBE(15421, BasicFurniture.GLOBE, ORN_GLOBE, LUNAR_GLOBE, CELEST_GLOBE, ARMILLARY, SM_ORRERY, LG_ORRERY),
    CRYSTAL_BALL(15422, BasicFurniture.CRYSTAL_BALL, ELE_SPHERE, CRYSTAL_OF_POWER),
    WALL_CHART(15423, ALCH_CHART, ASTRO_CHART, INFERN_CHART),
    TELESCOPE(15424, WOOD_TELESCOPE, TEAK_TELESCOPE, MAHOG_TELESCOPE),        
    BOOKCASE_STUDY(15425, BOOKCASE.furniture),
    
    // Costume room
    CAPE_RACK(18810, OAK_RACK, TEAK_RACK, MAHOGANY_RACK, GILDED_RACK, MARBLE_RACK, MAGIC_RACK),
    MAGIC_WARDROBE(18811, MAGIC_OAK_WDROBE, MAGIC_C_OAK_WDROBE, MAGIC_TEAK_WDROBE, MAGIC_C_TEAK_WDROBE, MAGIC_MAHOG_WDROBE, MAGIC_GILDED_WDROBE, MAGIC_MARBLE_WDROBE),
    TOY_BOX(18812, OAK_TOY_BOX, TEAK_TOY_BOX, MAHOGANY_TOY_BOX),
    COST_TREASURE_CHEST(18813, OAK_TREASURE_CHEST, TEAK_TREASURE_CHEST, MAHOG_TREASURE_CHEST),
    COSTUME_BOX(18814, OAK_COSTUME_BOX, TEAK_COSTUME_BOX, MAHOG_COSTUME_BOX),
    ARMOR_CASE(18815, OAK_ARMOR_CASE, TEAK_ARMOR_CASE, MAHOG_ARMOR_CASE),
    
    // Chapel
    CHAPEL_ICON(15269, SARA_SYMBOL, ZAMO_SYMBOL, GUTH_SYMBOL, SARA_ICON, ZAMO_ICON, GUTH_ICON, BOB_ICON),
    ALTAR(15270, OAK_ALTAR, TEAK_ALTAR, CLOTH_ALTAR, MAHOG_ALTAR, STONE_ALTAR, MARBLE_ALTAR, GILDED_ALTAR),
    BURNERS(15271, WOOD_TORCH, STEEL_TORCH, STEEL_CNDL, GOLD_CNDL, INCENSE_BURNER, MAHOG_BURNER, MARBLE_BURNER),
    RUG_3_SIDE(15273, RUG_SIDE.furniture),
    RUG_3_CORNER(15264, RUG_CORNER.furniture),
    STATUES(15275, SMALL_STATUE, MED_STATUE, LARGE_STATUE),
    INSTRUMENT(15276, WINDCHIMES, BELLS, ORGAN),
    WINDOWS(13730),
    
    // Portal chamber
    PORTAL_1(15406),
    PORTAL_2(15407),
    PORTAL_3(15408),
    PORTAL_FOCUS(15409),
    
    // Formal garden
    FORMAL_CENTERPIECE(15368, EXIT_PORTAL, GAZEBO, DUNGEON_ENTRANCE, SMALL_FOUNTAIN, LARGE_FOUNTAIN, POSH_FOUNTAIN),
    FENCING(15369, BOUNDARY_STONES, WOOD_FENCE, STONE_WALL, IRON_RAIL, PICKET_FENCE, GARDEN_FENCE, MARBLE_WALL),

    HEDGING_END(15370, THORNY_END, NICE_END, BOX_END, TOPIARY_END, FANCY_END, TALL_FANCY_END, TALL_BOX_END),
    HEDGING_CENTER(15371, THORNY_CENTER, NICE_CENTER, BOX_CENTER, TOPIARY_CENTER, FANCY_CENTER, TALL_FANCY_CENTER, TALL_BOX_CENTER),
    HEDGING_CORNER(15372, THORNY_CORNER, NICE_CORNER, BOX_CORNER, TOPIARY_CORNER, FANCY_CORNER, TALL_FANCY_CORNER, TALL_BOX_CORNER),
    
    FORMAL_BIG_PLANT(15373, SUNFLOWER, MARIGOLD, ROSES),
    FORMAL_BIG_PLANT_2(15374, ROSEMARY, DAFFODILS, BLUEBELLS),
    FORMAL_SMALL_PLANT(15375, SMALL_SUNFLOWER, SMALL_MARIGOLD, SMALL_ROSES),
    FORMAL_SMALL_PLANT_2(15376, SMALL_ROSEMARY, SMALL_DAFFODILS, SMALL_BLUEBELLS),

    // Throne room
    THRONE_DECORATION(15434, CrestedFurniture.OAK_DECORATION, CrestedFurniture.TEAK_DECORATION, CrestedFurniture.GILDED_DECORATION, CrestedFurniture.ROUND_SHIELD, CrestedFurniture.SQUARE_SHIELD, CrestedFurniture.KITE_SHIELD),
    THRONE(15426, OAK_THRONE, TEAK_THRONE, MAHOGANY_THRONE, GILDED_THRONE, SKELETON_THRONE, CRYSTAL_THRONE),
    THRONE_FLOOR(15427, StyledFurniture.FLOOR_DECORATION, StyledFurniture.TRAPDOORS),
    LEVER(15435, OAK_LEVER, TEAK_LEVER, MAHOG_LEVER),
    THRONE_SEATING(15436, CARVED_TEAK_BENCH, MAHOG_BENCH, GILDED_BENCH),
    THRONE_SEATING_2(15437, THRONE_SEATING.furniture),
    TRAPDOOR(15438, OAK_TRAPDOOR, TEAK_TRAPDOOR, MAHOG_TRAPDOOR),
    
    // Oubliette
    OUB_DECORATION(15331, DECORATIVE_BLOOD, DECORATIVE_PIPE, HANGING_SKELETON),
    PRISON(15352, OAK_CAGE, OAK_STEEL_CAGE, STEEL_CAGE, SPIKED_CAGE, BONE_CAGE),
    PRISON_DOOR(15353, OAK_CAGE_DOOR, OAK_STEEL_CAGE_DOOR, STEEL_CAGE_DOOR, SPIKED_CAGE_DOOR, BONE_CAGE_DOOR),
    OUB_GUARD(15354),
    OUB_LIGHTING(15355, LIGHT_CANDLE, LIGHT_TORCH, SKULL_TORCH),
    OUB_LADDER(15356),

    // Center group descriptor
    STAIRWAY_DESCRIPTOR(-1, BasicFurniture.RUG_CENTER, OAK_STAIRCASE, TEAK_STAIRCASE, OPULENT_RUG_CENTER, SPIRAL_STAIRCASE, MARBLE_STAIRCASE, MARBLE_SPIRAL),
    
    // Skill hall
    SKILL_RUG_CENTER(15377, BasicFurniture.RUG_CENTER, null, null, OPULENT_RUG_CENTER, null, null, null),
    SKILL_RUG_SIDE(15378, BasicFurniture.RUG_SIDE, null, null, OPULENT_RUG_SIDE, null, null, null),
    SKILL_RUG_CORNER(15379, BasicFurniture.RUG_CORNER, null, null, OPULENT_RUG_CORNER, null, null, null),
    SKILL_STAIR(15380, null, OAK_STAIRCASE, TEAK_STAIRCASE, null, SPIRAL_STAIRCASE, MARBLE_STAIRCASE, MARBLE_SPIRAL),
    SKILL_STAIR_DOWN(15381, null, OAK_STAIRCASE_DOWN, TEAK_STAIRCASE_DOWN, null, SPIRAL_STAIRCASE_DOWN, MARBLE_STAIRCASE_DOWN, MARBLE_SPIRAL_DOWN),

    // Quest hall
    QUEST_RUG_CENTER(15387, SKILL_RUG_CENTER.furniture),
    QUEST_RUG_SIDE(15388, SKILL_RUG_SIDE.furniture),
    QUEST_RUG_CORNER(15389, SKILL_RUG_CORNER.furniture),
    QUEST_STAIR(15390, SKILL_STAIR.furniture),
    QUEST_STAIR_DOWN(15391, SKILL_STAIR_DOWN.furniture),
        
    // Dungeons
    DUNG_LIGHTING(15330, OUB_LIGHTING.furniture),
    
    // Treasure room   
    TREASURE_CHEST(15256, WOOD_CRATE, OAK_CHEST, TEAK_CHEST, MAHOG_CHEST, MAGIC_CHEST),
    TREASURE_GUARD(15257, DEMON, KALPHITE, TOK_XIL, DAGANNOTH, STEEL_DRAG);
    
    /* @formatter:on */

    private int objectId;
    private final Furniture[] furniture;

    private FurnitureHotspotType(int objectId, Furniture... furniture) {
        this.objectId = objectId;
        this.furniture = furniture;
    }

    public static FurnitureHotspotType forObjectId(int objectId) {
        if (objectId >= 15305 && objectId <= 15316) {
            return DOOR;
        }
        for (FurnitureHotspotType type : values()) {
            if (type.objectId == objectId) {
                return type;
            }
        }
        return null;
    }

    public int getHotspotId() {
        return objectId;
    }

    public Furniture getFurniture(int index) {
        try {
            return furniture[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public FurnitureInterface getInterface() {
        return furniture.length > FurnitureInterface.NORMAL.getSize() ? FurnitureInterface.EXTENDED : FurnitureInterface.NORMAL;
    }

    public void showFurnitureInterface(Player builder) {
        FurnitureInterface inter = getInterface();
        int wid = inter.getWindowId();
        Item[] items = new Item[inter.getSize()];
        int config = 0;
        for (int i = 0; i < inter.getSize(); i++) {
            int textId = inter.getTextOffset() + (5 * i);
            int itemIndex = inter.getItemIndex(i);
            try {
                Furniture f = furniture[i];
                items[itemIndex] = new Item(f.getItemId());
                builder.setInterfaceText(wid, textId++, ItemDefinitions.name(f.getItemId()));
                builder.setInterfaceText(wid, inter.getLevelOffset() + i, "Lvl " + f.getLevel());
                for (int matI = 0; matI < 4; matI++) {
                    MaterialRequirement req = f.material(matI);
                    if (req != null) {
                        builder.setInterfaceText(wid, textId + matI, req.toString());
                    } else {
                        builder.setInterfaceText(wid, textId + matI, "");
                    }
                }
                if (f.getRequirements().hasRequirements(builder)) {
                    config |= 1 << (i + 1);
                }
            } catch (IndexOutOfBoundsException e) {
                items[itemIndex] = null;
                builder.setInterfaceText(wid, inter.getLevelOffset() + i, "");
                for (int j = 0; j < 5; j++) {
                    builder.setInterfaceText(wid, textId + j, "");
                }
                config |= 1 << (i + 1);
            }
        }
        switch (inter) {
        case EXTENDED:
            builder.getStateSet().setState(261, config);
            break;
        case NORMAL:
            for (int i = 0; i < inter.getSize(); i++) {
                builder.getStateSet().setState(261 + i, config >> (i + 1) & 0x1);
            }
            break;
        }

        builder.send(new InterfaceItemsMessage(wid, inter.getContainerId(), 8, items));
        builder.getInterfaceSet().openWindow(wid);
    }
}
