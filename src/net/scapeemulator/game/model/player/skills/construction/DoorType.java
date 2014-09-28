package net.scapeemulator.game.model.player.skills.construction;

/**
 * @author David Insley
 */
public enum DoorType {
    /* @formatter:off */
    BASIC_WOOD_1(15313, 13100),
    BASIC_WOOD_2(15314, 13101),

    BASIC_STONE_1(15307, 13094),
    BASIC_STONE_2(15308, 13095),

    WHITEWASHED_STONE_1(15309, 13006),
    WHITEWASHED_STONE_2(15310, 13007),
    
    FREMENNIK_WOOD_1(15311, 13107),
    FREMENNIK_WOOD_2(15312, 13108),

    TROPICAL_WOOD_1(15305, 13015),
    TROPICAL_WOOD_2(15306, 13016),

    FANCY_STONE_1(15315, 13120, 13119),
    FANCY_STONE_2(15316, 13121, 13118);
    /* @formatter:on */

    private final int hotspot;
    private final int closed;
    private final int open;
    private final int underground;

    private DoorType(int hotspot, int open) {
        this(hotspot, open, -1);
    }

    private DoorType(int hotspot, int open, int closed) {
        this.hotspot = hotspot;
        this.closed = closed;
        this.open = open;
        underground = -1;
    }

    public static DoorType forHotspot(int hotspot) {
        for (DoorType type : values()) {
            if (type.hotspot == hotspot) {
                return type;
            }
        }
        return null;
    }

    public int getHotspotId() {
        return hotspot;
    }

    public int getClosedId() {
        return closed;
    }

    public int getOpenId() {
        return open;
    }

}
