package net.scapeemulator.game.model.player.skills.farming.plant;

import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.FarmingCycle;
import net.scapeemulator.game.model.player.skills.farming.PlantState;

public interface IPlant {

    int getSeedId();
    
    int getStageCount();

    int getVarbit(int stage, PlantState state);

    CureType getCureType();
    
    boolean canWater();
    
    FarmingCycle getCycle();

    Requirements getRequirements();

}
