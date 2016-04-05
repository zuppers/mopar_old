package net.scapeemulator.game.model.player.skills.crafting;

public enum Leather {

    SOFT(1741, 1739),
    HARD(1743, 1739),
    GREEN(1745, 1753),
    BLUE(2505, 1751),
    RED(2507, 1749),
    BLACK(2509, 1747);

    private final int leatherId;
    private final int rawId;

    private Leather(int leatherId, int rawId) {
        this.leatherId = leatherId;
        this.rawId = rawId;
    }

    public int getLeatherId() {
        return leatherId;
    }

    public int getRawId() {
        return rawId;
    }
}
