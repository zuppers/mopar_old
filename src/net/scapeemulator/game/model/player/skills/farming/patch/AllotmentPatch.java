package net.scapeemulator.game.model.player.skills.farming.patch;

import net.scapeemulator.game.model.player.skills.farming.plant.AllotmentPlant;

public enum AllotmentPatch implements IFarmPatch {

    FALADOR_NW(8550),
    FALAOR_SE(8551),

    CATHERBY_N(8552),
    CATHERBY_S(8553),

    ARDOUGNE_N(8554),
    ARDOUGNE_S(8555),

    PHASMATYS_NW(8556),
    PHASMATYS_SE(8557),

    HARMONY(21950);

    private final int objectId;

    private AllotmentPatch(int objectId) {
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
        return 0;
    }

}