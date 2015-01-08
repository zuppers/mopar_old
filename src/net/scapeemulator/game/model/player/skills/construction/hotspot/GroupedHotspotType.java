package net.scapeemulator.game.model.player.skills.construction.hotspot;

/**
 * @author David
 */
public enum GroupedHotspotType {

    /* @formatter:off */
    ;
    /* @formatter:on */

    private final HotspotType[] types;

    private GroupedHotspotType(HotspotType... types) {
        this.types = types;
    }

    public static GroupedHotspotType forType(HotspotType type) {
        for (GroupedHotspotType group : values()) {
            for (HotspotType typeG : group.types) {
                if (typeG == type) {
                    return group;
                }
            }
        }
        return null;
    }

}
