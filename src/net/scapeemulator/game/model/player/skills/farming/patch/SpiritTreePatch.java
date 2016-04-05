package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public enum SpiritTreePatch implements IFarmPatch {

    ETC(8382);

    private final int objectId;

    private SpiritTreePatch(int objectId) {
        this.objectId = objectId;
    }

    public int getObjectId() {
        return objectId;
    }

    @Override
    public IPlant[] getValidPlants() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getGuideIndex() {
        return 7;
    }

}
