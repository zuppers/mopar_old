package net.scapeemulator.game.model.player.skills.prayer;

import static net.scapeemulator.game.model.player.skills.prayer.Prayer.*;

/**
 * @author David Insley
 */
public enum HeadIcon {
    
    /*
     *  6 = range/mage
     *  8 = summoning/melee
     *  9 = summoning/range
     *  10 = summoning/magic
     */
    
    NONE(-1, null),
    MELEE(0, PROTECT_FROM_MELEE),
    RANGED(1, PROTECT_FROM_RANGED),
    MAGIC(2, PROTECT_FROM_MAGIC),
    RETRIBUTION(3, Prayer.RETRIBUTION),
    SMITE(4, Prayer.SMITE),
    REDEMPTION(5, Prayer.REDEMPTION),
    SUMMONING(7, PROTECT_FROM_SUMMONING);
    
    private final int iconId;
    private final Prayer prayer;
    
    private HeadIcon(int iconId, Prayer prayer) {
        this.iconId = iconId;
        this.prayer = prayer;
    }
    
    public static HeadIcon forPrayer(Prayer prayer) {
        for (HeadIcon headIcon : values()) {
            if (headIcon.prayer == prayer) {
                return headIcon;
            }
        }
        return null;
    }
    
    public int getIconId() {
        return iconId;
    }
    
    public Prayer getPrayer() {
        return prayer;
    }
}
