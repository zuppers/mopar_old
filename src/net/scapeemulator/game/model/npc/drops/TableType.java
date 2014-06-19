package net.scapeemulator.game.model.npc.drops;

/**
 * @author David
 */
public enum TableType {

    ALWAYS(-1.0), EXTRA(-1.0), COMMON(1.0), UNCOMMON(0.15), RARE(0.03), VERY_RARE(0.005);

    private double defaultChance;

    private TableType(double chance) {
        this.defaultChance = chance;
    }

    public double getDefaultChance() {
        return defaultChance;
    }

}