package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public enum HopsPatch implements IFarmPatch {

    YANILLE(8173),
    ENTRANA(8174),
    LUMBRIDGE(8175),
    MCGRUBORS_WOOD(8176);

    private final int objectId;

    private HopsPatch(int objectId) {
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
        return 1;
    }

}
