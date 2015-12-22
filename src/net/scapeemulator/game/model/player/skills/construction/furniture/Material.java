package net.scapeemulator.game.model.player.skills.construction.furniture;

import net.scapeemulator.game.model.player.skills.magic.Rune;

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
    GOLD(2357, 7),
    LIMESTONE(3420, 20),
    CLOCKWORK(8792, 22),
    UNP_ORB(567, 10),
    ROPE(954, 4),
    GLASS(1775, 1),
    CANDLE(36, 1),
    LIT_CANDLE(33, 1),
    LIT_TORCH(594, 1),
    BONE(526, 0),
    SKULL(964, 1),
    RED_DYE(1763, 1),
    
    CIDER(5763, 0),
    ASG_ALE(1905, 0.5),
    GREEN_ALE(1909, 0.5),
    DRAG_BITTER(1911, 0.5),
    CHEF_DELIGH(5755, 0.5),
    
    BAGGED_DEAD(8417, 31),
    BAGGED_NICE(8419, 44),
    BAGGED_OAK(8421, 70),
    BAGGED_WILLOW(8423, 100),
    BAGGED_MAPLE(8425, 122),
    BAGGED_YEW(8427, 141),
    BAGGED_MAGIC(8429, 223),
    PLANT1(8431, 30),
    PLANT2(8433, 70),
    PLANT3(8435, 100),
    THORNY_HEDGE(8437, 70),
    NICE_HEDGE(8439, 100),
    BOX_HEDGE(8441, 122),
    TOPIARY_HEDGE(8443, 141),
    FANCY_HEDGE(8445, 158),
    TALL_FANCY_HEDGE(8447, 223),
    TALL_BOX_HEDGE(8449, 316),
    ROSEMARY(8451, 70),
    DAFFODILS(8453, 100),
    BLUEBELLS(8455, 122),
    SUNFLOWER(8457, 70),
    MARIGOLDS(8459, 100),
    ROSES(8461, 122),
    
    STUFFED_HAND(7982, 30),
    STUFFED_COCKATRICE(7983, 40),
    STUFFED_BASILISK(7894, 50),
    STUFFED_KURASK(7985, 80),
    STUFFED_ABYSSAL(7986, 100),
    STUFFED_KBD(7987, 700),
    STUFFED_KQ(7988, 700),
    
    STUFFED_BASS(7990, 30),
    STUFFED_SWORDFISH(7992, 50),
    STUFFED_SHARK(7994, 70),
    
    ARTHUR(7995, 31),
    ELENA(7996, 31),
    GIANT_DWARF(7997, 31),
    MISCELLANIANS(7998, 31),
    
    SMALL_MAP(8004, 31),
    MED_MAP(8005, 31),
    LARGE_MAP(8006, 31),
    
    LUMBRIDGE(8002, 44),
    DESERT(7999, 44),
    MORYTANIA(8003, 44),
    KARAMJA(8001, 44),
    ISAFDAR(8000, 44),
    
    AIR_RUNE(Rune.AIR.getItemId(), 0.05),
    WATER_RUNE(Rune.WATER.getItemId(), AIR_RUNE.xp),
    EARTH_RUNE(Rune.EARTH.getItemId(), AIR_RUNE.xp),
    FIRE_RUNE(Rune.FIRE.getItemId(), AIR_RUNE.xp);

    /* @formatter:on */

    private final int itemId;
    private final double xp;

    private Material(int itemId, double xp) {
        this.itemId = itemId;
        this.xp = xp;
    }

    public int getItemId() {
        return itemId;
    }

    public double getXp() {
        return xp;
    }

    public MaterialRequirement req() {
        return req(1);
    }

    public MaterialRequirement req(int amt) {
        return new MaterialRequirement(this, amt);
    }

}
