package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.HerbPlant;
import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public enum HerbPatch implements IFarmPatch {

    FALADOR(8150), // falador flower = 7847, compost = ??
    CATHERBY(8151), // catherby flower = 7848, catherby compost = 7837
    ARDOUGNE(8152), // ardy flower = 7849 , compost = 7839
    PHASMATYS(8153), // phas flower = 7850, compost = 7838
    TROLLHEIM(18816);

    private final int objectId;

    private HerbPatch(int objectId) {
        this.objectId = objectId;
    }

    @Override
    public int getObjectId() {
        return objectId;
    }

    @Override
    public IPlant[] getValidPlants() {
        return HerbPlant.values();
    }

    @Override
    public int getGuideIndex() {
        return 6;
    }

}
