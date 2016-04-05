package net.scapeemulator.game.model.player.skills.farming;

import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;
import net.scapeemulator.game.model.player.skills.farming.plant.WeedsPlant;

public class PatchState {

    private int stage;
    private IPlant plant = WeedsPlant.FULL_WEEDS;
    private PlantState plantState = PlantState.GROWING;
    private Compost compost = Compost.NONE;
    private boolean farmerProtected;

    public PlantState getPlantState() {
        return plantState;
    }

    public void setPlantState(PlantState plantState) {
        this.plantState = plantState;
    }

    public boolean isEmpty() {
        return plant == WeedsPlant.EMPTY;
    }

    public void setPlant(IPlant plant) {
        this.plant = plant;
        stage = 0;
        plantState = PlantState.GROWING;
    }

    public Compost getCompostType() {
        return compost;
    }

    public IPlant getPlant() {
        return plant;
    }

    public int getStage() {
        return stage;
    }

    public boolean isProtected() {
        return farmerProtected;
    }
    
    public void upStage() {
        if (++stage >= plant.getStageCount()) {
            plantState = PlantState.GROWN;
        }
    }

}
