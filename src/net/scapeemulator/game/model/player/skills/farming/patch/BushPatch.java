package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public enum BushPatch implements IFarmPatch {

    CHAMP_GUILD(7577),
    RIMMINGTON(7578),
    ETCETERIA(7579),
    SOUTH_ARDY(7580);
 
    private final int objectId;

    private BushPatch(int objectId) {
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
        return 4;
    }

}
