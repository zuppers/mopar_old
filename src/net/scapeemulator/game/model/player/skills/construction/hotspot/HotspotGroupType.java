package net.scapeemulator.game.model.player.skills.construction.hotspot;

import static net.scapeemulator.game.model.player.skills.construction.hotspot.FurnitureHotspotType.*;
/**
 * @author David
 */
public enum HotspotGroupType {

    /* @formatter:off */
    UNGROUPED,
    CURTAINS(PARLOUR_CURTAINS, DINING_CURTAINS, BEDROOM_CURTAINS),
    KITCHEN_SHELVES(SHELVES, SHELVES_DISHES),
    DINING_SEATING_GROUP(DINING_SEATING),
    THRONE_SEATING_GROUP(THRONE_SEATING),
    DINING_SEATING_GROUP_2(DINING_SEATING_2),
    THRONE_SEATING_GROUP_2(THRONE_SEATING_2),
    THRONES(THRONE),
    TRAP_FLOOR(THRONE_FLOOR),
    CHAPEL_BURNERS(BURNERS),
    CHAPEL_STATUES(STATUES),
    CHAPEL_WINDOWS(WINDOWS),
    THRONE_DECORATIONS(THRONE_DECORATION),
    DINING_DECORATIONS(DINING_DECORATION),
    FORMAL_FENCING(FENCING),
    FBP(FORMAL_BIG_PLANT),
    FBP_2(FORMAL_BIG_PLANT_2),
    FSP(FORMAL_SMALL_PLANT),
    FSP_2(FORMAL_SMALL_PLANT_2),
    OUB_DEC(OUB_DECORATION),
    OUB_LIGHT(OUB_LIGHTING),
    OUB_PRISON(PRISON, PRISON_DOOR),
    FORMAL_HEDGING(HEDGING_END, HEDGING_CENTER, HEDGING_CORNER),
    RUG(RUG_CENTER, RUG_SIDE, RUG_CORNER, RUG_2_CENTER, RUG_2_SIDE, RUG_2_CORNER, RUG_3_SIDE, RUG_3_CORNER);
    /* @formatter:on */

    private final FurnitureHotspotType[] types;

    private HotspotGroupType(FurnitureHotspotType... types) {
        this.types = types;
    }

    public FurnitureHotspotType getSubType() {
        return types[0];
    }
    
    public static HotspotGroupType forType(FurnitureHotspotType type) {
        for (HotspotGroupType group : values()) {
            for (FurnitureHotspotType typeG : group.types) {
                if (typeG == type) {
                    return group;
                }
            }
        }
        return UNGROUPED;
    }

}
