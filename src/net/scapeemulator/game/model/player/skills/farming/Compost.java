package net.scapeemulator.game.model.player.skills.farming;

public enum Compost {

    NONE(0.5, -1),
    REGULAR(0.3, 6032),
    SUPER(0.1, 6034);

    private final double diseaseChance;
    private final int itemId;
    
    private Compost(double diseaseChance, int itemId) {
        this.diseaseChance = diseaseChance;
        this.itemId = itemId;
    }
    
    public static Compost forItemId(int itemId) {
        if(itemId == NONE.itemId) {
            return null;
        }
        return null;
    }
    public double getDiseaseChance() {
        return diseaseChance;
    }
}
