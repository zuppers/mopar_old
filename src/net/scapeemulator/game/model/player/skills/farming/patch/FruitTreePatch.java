package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public enum FruitTreePatch implements IFarmPatch {

    GNOME_STRONGHOLD(-1),
    CATHERBY(-1),
    GNOME_VILLAGE(-1),
    BRIMHAVEN(-1),
    LLETYA(-1);
    
    private final int objectId;

    private FruitTreePatch(int objectId) {
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
        return 3;
    }

}
