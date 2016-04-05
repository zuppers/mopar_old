package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public interface IFarmPatch {

    IPlant[] getValidPlants();
    
    int getObjectId();
    
    int getGuideIndex();
}
