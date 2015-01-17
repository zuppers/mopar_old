package net.scapeemulator.game.model.player.consumable;

import net.scapeemulator.game.model.player.skills.cooking.RawFood;

/**
 * Normal food that only heals health, no extra effects.
 * 
 * @author David Insley
 */
public enum Food {
    CRAYFISH(1, 13433),
    ANCHOVIES(1, RawFood.ANCHOVY.getCookedId()),
    SHRIMP(3, RawFood.SHRIMP.getCookedId()),
    CHICKEN(3, RawFood.CHICKEN.getCookedId()),
    MEAT(3, RawFood.BEEF.getCookedId()),
    CAKE(4, 1891, 1893, 1895),
    BREAD(5, RawFood.BREAD.getCookedId()),
    HERRING(5, RawFood.HERRING.getCookedId()),
    MACKEREL(6, RawFood.MACKEREL.getCookedId()),
    PLAIN_PIZZA(7, 2289, 2291),
    TROUT(7, RawFood.TROUT.getCookedId()),
    PIKE(8, RawFood.PIKE.getCookedId()),
    LOBSTER(12, RawFood.LOBSTER.getCookedId());

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
