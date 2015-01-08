package net.scapeemulator.game.model.player.skills.construction.hotspot;

import static net.scapeemulator.game.model.player.skills.construction.furniture.Furniture.*;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.construction.furniture.Furniture;
import net.scapeemulator.game.model.player.skills.construction.furniture.FurnitureInterface;
import net.scapeemulator.game.model.player.skills.construction.furniture.MaterialRequirement;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemsMessage;

/**
 * @author David
 */
public enum HotspotType {

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
    RUG_CENTER(15413),
    RUG_SIDE(15414),
    RUG_CORNER(15415),
    CURTAINS(15419, TORN_CURTAINS, Furniture.CURTAINS, OPULENT_CURTAINS),
    BOOKCASE(15416, WOODEN_BOOKCASE, OAK_BOOKCASE, MAHOGANY_BOOKCASE), 
    FIREPLACE(15418, CLAY_FIREPLACE, LIMESTONE_FIREPLACE, MARBLE_FIREPLACE),
    CHAIR(15410, CRUDE_CHAIR, WOODEN_CHAIR, ROCKING_CHAIR, OAK_CHAIR, OAK_ARMCHAIR, TEAK_ARMCHAIR, MAHOGANY_ARMCHAIR),
    CHAIR_2(15411, CHAIR.furniture),
    CHAIR_3(15412, CHAIR.furniture),
    
    // Kitchen
    PET_BASKET(15402, CAT_BLANKET, Furniture.CAT_BASKET, CUSHIONED_BASKET),
    BARREL(15401, BEER, CIDER, ASGARNIAN, GREENMANS, DRAGON_BITTER, CHEFS_DELIGHT),
    SHELVES(15400, SHELF, SHELF_2, SHELF_3, OAK_SHELF, OAK_SHELF_2, TEAK_SHELF, TEAK_SHELF_2),
    SHELVES_DISHES(15399, SHELF_DISHES, SHELF_2_DISHES, SHELF_3_DISHES, OAK_SHELF_DISHES, OAK_SHELF_2_DISHES, TEAK_SHELF_DISHES, TEAK_SHELF_2_DISHES),
    KITCHEN_TABLE(15405, Furniture.KITCHEN_TABLE, OAK_KITCHEN_TABLE, TEAK_KITCHEN_TABLE),
    STOVE(15398, FIREPIT, FIREPIT_WITH_HOOK, FIREPIT_WITH_POT, SMALL_OVEN, LARGE_OVEN, STEEL_RANGE, FANCY_RANGE),
    LARDER(15403, Furniture.LARDER, OAK_LARDER, TEAK_LARDER),
    SINK(15404, PUMP_AND_DRAIN, PUMP_AND_TUB, Furniture.SINK),
    
    // Study
    LECTERN(15420),
    GLOBE(15421),
    CRYSTAL_BALL(15422),
    WALL_CHART(15423),
    TELESCOPE(15424),        
    BOOKCASE_STUDY(15425, BOOKCASE.furniture),
    
    // Dining room
    BELL_PULL(15304),
    CURTAINS_2(15302),
    DECORATION(15303),
    SEATING_2(15299),
    SEATING(15300),
    DINING_TABLE(15298),
    FIREPLACE_2(15301);
    /* @formatter:on */

    private int objectId;
    private final Furniture[] furniture;

    private HotspotType(int objectId, Furniture... furniture) {
        this.objectId = objectId;
        this.furniture = furniture;
    }

    public static HotspotType forObjectId(int objectId) {
        if (objectId >= 15305 && objectId <= 15316) {
            return DOOR;
        }
        for (HotspotType type : values()) {
            if (type.objectId == objectId) {
                return type;
            }
        }
        return null;
    }

    public int getHotspotId() {
        return objectId;
    }

    public Furniture[] getFurniture() {
        return furniture;
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
                builder.getStateSet().setState(261 + i, config & (1 << i));
            }
            break;
        }

        builder.send(new InterfaceItemsMessage(wid, inter.getContainerId(), 8, items));
        builder.getInterfaceSet().openWindow(wid);
    }
}
