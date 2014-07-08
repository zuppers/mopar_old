package net.scapeemulator.game.model.player.skills.herblore;

import static net.scapeemulator.game.model.player.skills.herblore.Herb.*;
import static net.scapeemulator.game.model.player.skills.herblore.Secondary.*;

/**
 * @author David Insley
 */
public enum Potion {
        
    ATTACK(GUAM, EYE_OF_NEWT, 3, 25, 121),
    ANTI_POISON(MARRENTILL, UNICORN_HORN, 5, 37.5, 175),
    // Relicym's balm
    STRENGTH_POTION(TARROMIN, LIMPWURT, 12, 50, 115),
    RESTORE(HARRALANDER, SPIDERS_EGGS, 22, 62.5, 127),
    // GUTHIX_BALANCE(harr, red eggs, garlic, silver dust, 22, 50, 7662)
    BLAMISH_OIL(1581, HARRALANDER.getCleanId(), 25, 80, 1582),
    ENERGY(HARRALANDER, CHOCOLATE, 26, 67.5, 3010),
    DEFENCE(RANARR, WHITE_BERRIES, 30, 75, 133),
    SUPER_FISHING_EXPLOSIVE(GUAM, RUBIUM, 31, 55, 12633),
    AGILITY(TOADFLAX, TOAD_LEGS, 34, 80, 3034),
    COMBAT(HARRALANDER, GOAT_HORN, 36, 84, 9741),
    PRAYER(RANARR, SNAPE_GRASS, 38, 87.5, 139),
    SUMMONING(SPIRIT_WEED, COCKATRICE_EGG, 40, 92, 12142),
    SUPER_ATTACK(IRIT, EYE_OF_NEWT, 45, 100, 145),
    SUPER_ANTI_POISON(IRIT, UNICORN_HORN, 48, 106.3, 181),
    FISHING(AVANTOE, SNAPE_GRASS, 50, 112.5, 181),
    SUPER_ENERGY(AVANTOE, FUNGI, 52, 117.5, 3018),
    HUNTER(AVANTOE, KEBBIT_TEETH, 53, 120, 10000),
    SUPER_STRENGTH(KWUARM, LIMPWURT, 55, 125, 157),
    MAGIC_ESSENCE(STAR_FLOWER, GORAK_CLAW, 57, 130, 9022),
    POISON(KWUARM, BLUE_DRAGON_SCALE, 60, 137.5, 187),
    SUPER_RESTORE(SNAPDRAGON, SPIDERS_EGGS, 63, 142.5, 3026),
    // SANFEW_SERUM()
    SUPER_DEFENCE(CADANTINE, WHITE_BERRIES, 66, 150, 163),
    ANTI_POISON_P(TOADFLAX, YEW_ROOTS, 68, 155, 5945),
    ANTIFIRE(LANTADYME, BLUE_DRAGON_SCALE, 69, 157.5, 2454),
    RANGING(DWARF_WEED, WINE_OF_ZAMORAK, 72, 162.5, 169),
    // WEAPON_POISON_P(coco, cactus spine, SPIDERS_EGGS, 73, 165, 5937
    MAGIC(LANTADYME, POTATO_CACTUS, 76, 172.5, 3042),
    ZAMORAK_BREW(TORSTOL, JANGERBERRIES, 78, 175, 189),
    // ANTI_POISON_PP(coco, irit, magic roots, 79, 177.5, 5954)
    SARADOMIN_BREW(TOADFLAX, BIRDS_NEST, 81, 180, 6687);
    // WEAPON_POISON_PP(coco, nightshade, poison ivy, 82, 190, 5940)
    private final int unfinishedId;
    private final int secondary;
    private final int level;  
    private final double xp;
    private final int potionId;
    
    private Potion(Herb herb, Secondary secondary, int level, double xp, int potionId) {
        this(herb.getUnfinishedId(), secondary.getItemId(), level, xp, potionId);
    }
    
    private Potion(int unfinishedId, int secondary, int level, double xp, int potionId) {
        this.unfinishedId = unfinishedId;
        this.secondary = secondary;
        this.level = level;
        this.xp = xp;      
        this.potionId = potionId;
    }
    
    public int getUnfinishedId() {
        return unfinishedId;
    }   
    
    public int getSecondary() {
        return secondary;
    }
    
    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public int getPotionId() {
        return potionId;
    }

    
}
