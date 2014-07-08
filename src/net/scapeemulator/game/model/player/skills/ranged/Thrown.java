package net.scapeemulator.game.model.player.skills.ranged;

/**
 * @author David Insley
 */
public enum Thrown {

    ;

    private final int itemId;

    private Thrown(int itemId) {
        this.itemId = itemId;
    }

    public static Thrown forId(int itemId) {
        for (Thrown thrown : values()) {
            if (thrown.itemId == itemId) {
                return thrown;
            }
        }
        return null;
    }

}
