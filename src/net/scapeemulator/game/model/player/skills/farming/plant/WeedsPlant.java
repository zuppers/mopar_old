package net.scapeemulator.game.model.player.skills.farming.plant;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.FarmingCycle;
import net.scapeemulator.game.model.player.skills.farming.PlantState;

public enum WeedsPlant implements IPlant {

    FULL_WEEDS(0x0),
    TWO_LEFT(0x1),
    ONE_LEFT(0x2),
    EMPTY(0x3);

    private final int bit;

    private WeedsPlant(int bit) {
        this.bit = bit;
    }

    @Override
    public int getStageCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getVarbit(int stage, PlantState state) {
        return bit;
    }

    @Override
    public FarmingCycle getCycle() {
        return FarmingCycle.FIVE;
    }

    @Override
    public int getSeedId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Requirements getRequirements() {
        throw new UnsupportedOperationException();
    }

    public WeedsPlant next(boolean grow) {
        switch (this) {
            case EMPTY:
                return grow ? ONE_LEFT : this;
            case ONE_LEFT:
                return grow ? TWO_LEFT : EMPTY;
            case TWO_LEFT:
                return grow ? FULL_WEEDS : ONE_LEFT;
            case FULL_WEEDS:
                return grow ? this : TWO_LEFT;
        }
        throw new RuntimeException();
    }

    @Override
    public CureType getCureType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canWater() {
        return false;
    }
}
