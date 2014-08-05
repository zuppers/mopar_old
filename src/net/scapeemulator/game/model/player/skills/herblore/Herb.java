package net.scapeemulator.game.model.player.skills.herblore;

/**
 * @author David Insley
 */
public enum Herb {

    GUAM(199, 249, 3, 2.5, 91),
    MARRENTILL(201, 251, 5, 3.8, 93),
    TARROMIN(203, 253, 11, 5, 95),
    HARRALANDER(205, 255, 20, 6.3, 97),
    RANARR(207, 257, 25, 7.5, 99),
    TOADFLAX(3049, 2998, 30, 8, 3002),
    SPIRIT_WEED(12174, 12172, 35, 7.8, 12181),
    IRIT(209, 259, 40, 8.8, 101),
    AVANTOE(211, 261, 48, 10, 103),
    KWUARM(213, 263, 54, 11.3, 105),
    SNAPDRAGON(3051, 3000, 59, 11.8, 3004),
    CADANTINE(215, 265, 65, 12.5, 107),
    LANTADYME(2485, 2481, 67, 13.1, 2483),
    DWARF_WEED(217, 267, 70, 13.8, 109),
    TORSTOL(219, 269, 75, 15, 111),
    STAR_FLOWER(-1, 9017, 57, 0, 9019);

    private final int grimyId;
    private final int cleanId;
    private final int level;
    private final double xp;
    private final int unfinishedId;

    private Herb(int grimyId, int cleanId, int level, double xp, int unfinishedId) {
        this.grimyId = grimyId;
        this.cleanId = cleanId;
        this.level = level;
        this.xp = xp;
        this.unfinishedId = unfinishedId;
    }

    public static Herb forGrimyId(int grimyId) {
        for (Herb herb : values()) {
            if (herb.grimyId == grimyId && herb.grimyId > -1) {
                return herb;
            }
        }
        return null;
    }

    public int getGrimyId() {
        return grimyId;
    }

    public int getCleanId() {
        return cleanId;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public int getUnfinishedId() {
        return unfinishedId;
    }

}
