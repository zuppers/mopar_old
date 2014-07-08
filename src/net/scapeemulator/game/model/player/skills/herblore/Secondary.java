package net.scapeemulator.game.model.player.skills.herblore;

/**
 * @author David Insley
 */
public enum Secondary {

    EYE_OF_NEWT(221),
    UNICORN_HORN(235, 237),
    LIMPWURT(225),
    SPIDERS_EGGS(223),
    CHOCOLATE(1975, 1973),
    WHITE_BERRIES(239),
    TOAD_LEGS(2152),
    RUBIUM(12630),
    GOAT_HORN(9736, 9735),
    SNAPE_GRASS(231),
    COCKATRICE_EGG(12109),
    FUNGI(2970),
    KEBBIT_TEETH(10111, 10109),
    GORAK_CLAW(9018, 9016),
    BLUE_DRAGON_SCALE(241, 243),
    WINE_OF_ZAMORAK(245),
    YEW_ROOTS(6049),
    POTATO_CACTUS(3138),
    JANGERBERRIES(247),
    BIRDS_NEST(6693, 5075);
    
    private final int itemId;
    private final int ungroundId;
    
    private Secondary(int itemId) {
        this(itemId, -1);
    }
    
    private Secondary(int itemId, int ungroundId) {
        this.itemId = itemId;
        this.ungroundId = ungroundId;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public int getUngroundId() {
        return ungroundId;
    }
}
