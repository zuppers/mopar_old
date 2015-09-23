package net.scapeemulator.game.model.player.skills.construction;

public enum HeraldryCrest {

    /* @formatter:off */
    ARRAV(1),
    ASGARNIA(2),
    DORGESHUUN(3),
    DRAGON(4),
    FAIRY(5),
    GUTHIX(6),
    HAM(7),
    HORSE(8),
    JOGRE(9),
    KANDARIN(10),
    MISTHALIN(11),
    MONEY(12),
    SARADOMIN(13),
    SKULL(14),
    VARROCK(15),
    ZAMORAK(16);
    /* @formatter:on */

    private final int id;

    private HeraldryCrest(int id) {
        this.id = id;
    }

    public int getOffset() {
        return id - 1;
    }
    
    public static HeraldryCrest forId(int id) {
        for (HeraldryCrest crest : values()) {
            if (crest.id == id) {
                return crest;
            }
        }
        return null;
    }
}
