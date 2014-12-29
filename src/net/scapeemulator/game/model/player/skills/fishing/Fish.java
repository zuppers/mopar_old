package net.scapeemulator.game.model.player.skills.fishing;

/**
 * @author David Insley
 */
public enum Fish {
    
    SHRIMP(1, 10, 317, 315),
    SARDINE(5, 20, 327, 325),
    HERRING(10, 30, 345, 347),
    ANCHOVY(15, 40, 321, 319),
    MACKEREL(16, 20, 353, 355),
    TROUT(20, 50, 335, 333),
    COD(23, 45, 341, 339),
    PIKE(25, 60, 349, 351),
    SALMON(30, 70, 331, 329),
    TUNA(35, 80, 359, 361),
    LOBSTER(40, 90, 377, 379),
    BASS(46, 100, 363, 365),
    SWORDFISH(50, 100, 371, 373),
    MONKFISH(62, 120, 7944, 7946),
    SHARK(76, 110, 383, 385);

    private final int level;
    private final double xp;
    private final int rawId;
    private final int cookedId;

    private Fish(int level, double xp, int rawId, int cookedId) {
        this.level = level;
        this.xp = xp;
        this.rawId = rawId;
        this.cookedId = cookedId;
    }
    
    /**
     * Gets the level required to catch this fish, used in calculating how often to catch as well.
     * 
     * @return the level required to catch this fish
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Gets the xp given to the player when the fish is caught.
     * 
     * @return the xp for catching this fish
     */
    public double getXp() {
        return xp;
    }
    
    public int getRawId() {
        return rawId;
    }
    
    public int getCookedId() {
        return cookedId;
    }
    
}
