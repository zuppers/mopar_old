package net.scapeemulator.game.model.player.skills.farming.plant;

import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.Farming;
import net.scapeemulator.game.model.player.skills.farming.FarmingCycle;
import net.scapeemulator.game.model.player.skills.farming.PlantState;

public enum HopsPlant implements IPlant {

    BARLEY(3, 8.5, 5305, 4, 0x31),
    HAMMERSTONE(4, 9.0, 5307, 4, 0x4),
    ASGARNIAN(8, 10.9, 5308, 5, 0xB),
    JUTE(13, 13.0, 5306, 5, 0x38),
    YANILLIAN(16, 14.5, 5309, 6, 0x13),
    KRANDORIAN(21, 17.5, 5310, 7, 0x1C),
    WILDBLOOD(28, 23.0, 5311, 8, 0x26);

    private final int seedId;
    private final int baseBit;
    private final int stages;
    private final Requirements requirements;

    private HopsPlant(int level, double plantXp, int seedId, int stages, int baseBit) {
        this.seedId = seedId;
        this.stages = stages;
        this.baseBit = baseBit;
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.FARMING, level, true, "plant that", plantXp));
        requirements.addRequirement(Farming.DIBBER_REQ);
        requirements.addRequirement(new ItemRequirement(seedId, 4, true, "You do not have enough seeds to plant that."));
    }

    @Override
    public int getSeedId() {
        return seedId;
    }

    @Override
    public int getStageCount() {
        return stages;
    }

    @Override
    public FarmingCycle getCycle() {
        return FarmingCycle.TEN;
    }

    @Override
    public int getVarbit(int stage, PlantState state) {
        int value = baseBit + stage;
        switch (state) {
            case GROWING:
            case GROWN:
                return value;
            case WATERED:
                return (0x01 << 6) | value;
            case DISEASED:
                return (0x02 << 6) | value;
            case DEAD:
                return (0x03 << 6) | value;
            default:
                throw new IllegalStateException("Bad hops plant state");
        }
    }

    @Override
    public Requirements getRequirements() {
        return requirements;
    }

    @Override
    public boolean canWater() {
        return true;
    }

    @Override
    public CureType getCureType() {
        return CureType.PLANT_CURE;
    }

}
