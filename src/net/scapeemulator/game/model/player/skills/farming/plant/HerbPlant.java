package net.scapeemulator.game.model.player.skills.farming.plant;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.FarmingCycle;
import net.scapeemulator.game.model.player.skills.farming.PlantState;
import net.scapeemulator.game.model.player.skills.herblore.Herb;

public enum HerbPlant implements IPlant {

    GUAM(9, 11, 5291, 0x4),
    MARRENTILL(14, 13.5, 5292, 0xB),
    TARROMIN(19, 16, 5293, 0x12),
    HARRALANDER(26, 21.5, 5294, 0x19),
    GOUTWEED(29, 105.0, 6311, 0xC0),
    RANAAR(32, 27.0, 5295, 0x20),
    //SPIRIT_WEED(36, 32.0, 12176), // TODO find shit
    TOADFLAX(38, 34.0, 5296, 0x27),
    IRIT(44, 43.0, 5297, 0x2E),
    AVANTOE(50, 54.5, 5298, 0x35),
    KWUARM(56, 69.0, 5299, 0x44),
    SNAPDRAGON(62, 87.5, 5300, 0x4B),
    CADANTINE(67, 106.5, 5301, 0x52),
    LANTADYME(73, 134.5, 5302, 0x59),
    DWARF_WEED(79, 170.5, 5303, 0x60),
    TORSTOL(85, 199.5, 5304, 0x67);

    private final int level;
    private final double plantXp;
    private final int seedId;
    private final int baseBit;
    private final int grimyId;

    private HerbPlant(int level, double plantXp, int seedId, int baseBit) {
        this.level = level;
        this.plantXp = plantXp;
        this.seedId = seedId;
        this.baseBit = baseBit;
        grimyId = 0;//this == GOUTWEED ? 0 : Herb.valueOf(name()).getGrimyId();
    }

    @Override
    public int getStageCount() {
        return 4;
    }

    @Override
    public int getVarbit(int stage, PlantState state) {
        switch (state) {
            case GROWN:
            case GROWING:
                return baseBit + stage;
            case DISEASED:
                return (this == GOUTWEED ? 0xC5 : (0x6D + 3 * 333)) + stage; // TODO find formula..
            case DEAD:
                return (this == GOUTWEED ? 0xC8 : 0xA9) + stage;
            default:
                throw new IllegalStateException("illegal herb state " + state);
        }
    }

    @Override
    public FarmingCycle getCycle() {
        return FarmingCycle.TWENTY;
    }

    @Override
    public int getSeedId() {
        return seedId;
    }

    @Override
    public Requirements getRequirements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CureType getCureType() {
        return CureType.PLANT_CURE;
    }

    @Override
    public boolean canWater() {
        return false;
    }
}
