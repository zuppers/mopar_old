package net.scapeemulator.game.model.player.skills.construction;

/**
 * @author David
 *
 */
public enum Furniture {

    /* @formatter:off */
    // GARDEN
    
    // Centrepiece
    EXIT_PORTAL(13405),
    GAZEBO(13477),
    DUNGEON_ENTRANCE(13409),
    DECORATIVE_ROCK(13406),
    POND(13407),
    IMP_STATUE(13408),
    
    // Garden tree small
    DEAD_TREE(13418),
    NICE_TREE(13419),
    OAK_TREE(13420),
    WILLOW_TREE(13421),
    MAPLE_TREE(13423),
    YEW_TREE(13422),
    MAGIC_TREE(13424),
    
    // Garden tree big
    DEAD_TREE_BIG(13411),
    NICE_TREE_BIG(13412),
    OAK_TREE_BIG(13413),
    WILLOW_TREE_BIG(13414),
    MAPLE_TREE_BIG(13415),
    YEW_TREE_BIG(13416),
    MAGIC_TREE_BIG(13417),  
    
    // Garden small plant 1
    PLANT(13431),
    SMALL_FERN(13432),
    FERN(13433),
    
    // Garden small plant 2
    DOCK_LEAF(13434),
    THISTLE(13435),
    REEDS(13436),
    
    // Garden big plant 1
    FERN_BIG(13425),
    BUSH(13426),
    TALL_PLANT(13427),
    
    // Garden big plant 2
    SHORT_PLANT(13428),
    LARGE_LEAF_BUSH(13429),
    HUGE_PLANT(13430),
    
    // PARLOUR
    
    // Chairs
    CRUDE_CHAIR(13581),
    WOODEN_CHAIR(13582),
    ROCKING_CHAIR(13583),
    OAK_CHAIR(13584),
    OAK_ARMCHAIR(13585),
    TEAK_ARMCHAIR(13586),
    MAHOGANY_ARMCHAIR(13587),
    
    // Fireplaces
    CLAY_FIREPLACE(13609),
    LIMESTONE_FIREPLACE(13611),
    MARBLE_FIREPLACE(13613),
    
    // Bookcases
    WOODEN_BOOKCASE(13597),
    OAK_BOOKCASE(13598),
    MAHOGANY_BOOKCASE(13599),
    
    // Curtains
    TORN_CURTAINS(13603),
    CURTAINS(13604),
    OPULENT_CURTAINS(13605),
    
    // KITCHEN
    
    // Tables
    KITCHEN_TABLE(13577),
    OAK_KITCHEN_TABLE(13578),
    TEAK_KITCHEN_TABLE(13579),
    
    // Pet blankets
    PET_BLANKET(13574),
    PET_BASKET(13575),
    CUSHIONED_BASKET(13576),
    
    // Stoves
    FIREPIT(13528),
    FIREPIT_WITH_HOOK(13529),
    FIREPIT_WITH_POT(13531),
    SMALL_OVEN(13533),
    LARGE_OVEN(13536),
    STEEL_RANGE(13539),
    FANCY_RANGE(13542),
    
    // Shelves
    SHELF(13545),
    SHELF_2(13546),
    SHELF_3(13547),
    OAK_SHELF(13548),
    OAK_SHELF_2(13549),
    TEAK_SHELF(13550),
    TEAK_SHELF_2(13551),
    SHELF_DISHES(13552),
    SHELF_2_DISHES(13553),
    SHELF_3_DISHES(13554),
    OAK_SHELF_DISHES(13555),
    OAK_SHELF_2_DISHES(13556),
    TEAK_SHELF_DISHES(13557),
    TEAK_SHELF_2_DISHES(13558),
    
    // Larders
    LARDER(13565),
    OAK_LARDER(13566),
    TEAK_LARDER(13567),
    
    // Barrels 
    BEER(13568),
    CIDER(13569),
    ASGARNIAN(13570),
    GREENMANS(13571),
    DRAGON_BITTER(13572),
    CHEFS_DELIGHT(13573),
    
    // Sinks
    PUMP_AND_DRAIN(13559),
    PUMP_AND_TUB(13561),
    SINK(13563),
    
    // Windows (do not change names, used in WindowHotspot)
    BASIC_STONE_WINDOW(13091),
    BASIC_WOOD_WINDOW(13099),
    WHITEWASHED_STONE_WINDOW(13005),
    FREMENNIK_WOOD_WINDOW(13112),
    TROPICAL_WOOD_WINDOW(10816),
    FANCY_STONE_WINDOW(13117);
    /* @formatter:on */

    private final int objectId;
    private final int level;

    private Furniture(int objectId) {
        this.objectId = objectId;
        this.level = 1;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getLevelReq() {
        return level;
    }

}
