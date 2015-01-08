package net.scapeemulator.game.model.player.skills.construction.furniture;

/**
 * @author David Insley
 */
public enum Material {

    /* @formatter:off */
    PLANK(960, 30),
    OAK(8778, 60),
    TEAK(8780, 90),
    MAHOGANY(8782, 140),
    GOLD_LEAF(8784, 300),
    MARBLE(8786, 500),
    MAGIC_STONE(8788, 1000),
    CLAY(1761, 10),
    IRON(2351, 10),
    WOOL(1737, 0),
    CLOTH(8790, 15),
    STEEL(2353, 20),
    LIMESTONE(3420, 20),
    BAGGED_DEAD(8417, 31),
    BAGGED_NICE(8419, 44),
    BAGGED_OAK(8421, 70),
    BAGGED_WILLOW(8423, 100),
    BAGGED_MAPLE(8425, 122),
    BAGGED_YEW(8427, 141),
    BAGGED_MAGIC(8429, 223),
    PLANT1(8431, 31),
    PLANT2(8433, 70),
    PLANT3(8435, 100);
    /* @formatter:on */

    private final int itemId;
    private final int xp;

    private Material(int itemId, int xp) {
        this.itemId = itemId;
        this.xp = xp;
    }

    public int getItemId() {
        return itemId;
    }

    public int getXp() {
        return xp;
    }

    public MaterialRequirement req() {
        return req(1);
    }

    public MaterialRequirement req(int amt) {
        return new MaterialRequirement(this, amt);
    }

}
