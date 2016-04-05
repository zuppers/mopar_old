package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.AllotmentPlant;

public enum FlowerPatch implements IFarmPatch {

    FALADOR(7847),
    CATHERBY(-1),
    ARDOUGNE(-1),
    PHASMATYS(-1);

    private final int objectId;

    private FlowerPatch(int objectId) {
        this.objectId = objectId;
    }

    @Override
    public int getObjectId() {
        return objectId;
    }

    @Override
    public AllotmentPlant[] getValidPlants() {
        return AllotmentPlant.values();
    }

    @Override
    public int getGuideIndex() {
        return 5;
    }

}