package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public enum TreePatch implements IFarmPatch {

    LUMBRIDGE_CASTLE(-1),
    VARROCK_CASTLE(-1),
    FALADOR_PARK(-1),
    TAVERLY(-1),
    GNOME_STRONGHOLD(-1);
    
 
    private final int objectId;

    private TreePatch(int objectId) {
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
        return 2;
    }

}
