package net.scapeemulator.game.model.player.skills.prayer;

/**
 * @author David Insley
 */
public enum Bone {

    BONES(4.5, 526),
    WOLF_BONES(4.5, 2859),
    BURNT_BONES(4.5, 528),
    MONKEY_BONES(5, 3183),
    BAT_BONES(5.3, 530),
    BIG_BONES(15, 532),
    JOGRE_BONES(15, 3125),
    ZOGRE_BONES(22.5, 4812),
    SHAIKAHAN_BONES(25, 3123),
    BABY_DRAGON_BONES(30, 534),
    WYVERN_BONES(50, 6812),
    DRAGON_BONES(72, 536),
    FAYRG_BONES(84, 4830),
    RAURG_BONES(96, 4832),
    DAGANNOTH_BONES(125, 6729),
    OURG_BONES(140, 4834);

    private final double xp;
    private final int itemId;

    private Bone(double xp, int itemId) {
        this.xp = xp;
        this.itemId = itemId;
    }

    public double getXp() {
        return xp;
    }

    public int getItemId() {
        return itemId;
    }

}
