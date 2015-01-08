package net.scapeemulator.game.model.player.consumable;

/**
 * Normal food that only heals health, no extra effects.
 * 
 * @author David Insley
 */
public enum Food {
    CRAYFISH(1, 13433),
    ANCHOVIES(1, 0),
    SHRIMP(3, 0),
    CHICKEN(3, 0),
    MEAT(3, 0),
    CAKE(4, 1891, 1893, 1895),
    BREAD(5, 2309),
    HERRING(5, 0),
    MACKEREL(6, 0),
    PLAIN_PIZZA(7, 2289, 2291),
    TROUT(7, 0),
    PIKE(8, 0),
    LOBSTER(12, 0);

    private final int heal;
    private final int[] bites;
    
    private Food(int heal, int... bites) {
        this.heal = heal;
        this.bites = bites;
    }

    public static Food forId(int biteId) {
        for (Food food : values()) {
            for (int bite : food.bites) {
                if (bite == biteId) {
                    return food;
                }
            }
        }
        return null;
    }

    public int getNextBite(int bite) {
        for (int i = 0; i < bites.length - 1; i++) {
            if (bites[i] == bite) {
                return bites[i + 1];
            }
        }
        return -1;
    }

    public int getHeal() {
        return heal;
    }

}
