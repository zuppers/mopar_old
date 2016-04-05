package net.scapeemulator.game.model.player.skills.farming.plant;

import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.Farming;
import net.scapeemulator.game.model.player.skills.farming.FarmingCycle;
import net.scapeemulator.game.model.player.skills.farming.PlantState;

public enum AllotmentPlant implements IPlant {

    POTATO(1, 8.0, 5318, 4, 0x6),
    ONION(5, 9.5, 5319, 4, 0xD),
    CABBAGE(7, 10.0, 5324, 4, 0x14),
    TOMATO(12, 12.5, 5322, 4, 0x1B),
    SWEETCORN(20, 17.0, 5320, 5, 0x22),
    STRAWBERRY(31, 26.0, 5323, 6, 0x2B),
    WATERMELON(47, 48.5, 5321, 8, 0x34);

    private final int seedId;
    private final int baseBit;
    private final int stages;
    private final Requirements requirements;

    private AllotmentPlant(int level, double plantXp, int seedId, int stages, int baseBit) {
        this.seedId = seedId;
        this.stages = stages;
        this.baseBit = baseBit;
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.FARMING, level, true, "plant that", plantXp));
        requirements.addRequirement(Farming.DIBBER_REQ);
        requirements.addRequirement(new ItemRequirement(seedId, 3, true, "You do not have enough seeds to plant that."));
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
        if (stage > stages) {
            stage = stages;
        }
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
                throw new IllegalStateException("Bad allotment plant state");
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
