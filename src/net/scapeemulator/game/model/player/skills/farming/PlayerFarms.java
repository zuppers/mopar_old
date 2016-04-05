package net.scapeemulator.game.model.player.skills.farming;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.scapeemulator.game.model.definition.ObjectDefinitions;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.farming.patch.AllotmentPatch;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.model.player.skills.farming.plant.WeedsPlant;

public class PlayerFarms {

    private static final int GROWTH_TICK_TIME = 20;

    private final Player player;
    private final Map<IFarmPatch, PatchState> patches;
    private int growthTick;

    public PlayerFarms(Player player) {
        this.player = player;
        this.patches = new HashMap<>();
        for (AllotmentPatch allotment : AllotmentPatch.values()) {
            patches.put(allotment, new PatchState());
        }
    }

    public PatchState getPatchState(IFarmPatch patch) {
        return patches.get(patch);
    }

    public void tick() {
        if (++growthTick >= GROWTH_TICK_TIME) {
            growthTick = 0;
            growthTick();
        }
    }

    private void growthTick() {
        for (Entry<IFarmPatch, PatchState> entry : patches.entrySet()) {
            IFarmPatch patch = entry.getKey();
            PatchState patchState = entry.getValue();
            if (Farming.cycleActive(patchState.getPlant().getCycle())) {
                grow(patch);
            }
        }
    }

    private void grow(IFarmPatch patch) {
        PatchState state = patches.get(patch);
        switch (state.getPlantState()) {
            case GROWING:
                if (state.getPlant() instanceof WeedsPlant) {
                    state.setPlant(((WeedsPlant) state.getPlant()).next(true));
                    sendUpdates(patch);
                    return;
                }
                state.upStage();
                // TODO if not protected by farmer/flowers
                if (!state.isProtected()) {
                    double diseaseChance = state.getCompostType().getDiseaseChance();
                    if (Math.random() < diseaseChance) {
                        // TODO alert if farming/nature necklace used
                        state.setPlantState(PlantState.DISEASED);
                    }
                }
                sendUpdates(patch);
                break;

            case DISEASED:
                // should this be guaranteed instead of a chance?
                if (Math.random() < 0.5) {
                    state.setPlantState(PlantState.DEAD);
                    sendUpdates(patch);
                }
                break;
            case WATERED:
                state.setPlantState(PlantState.GROWING);
                state.upStage();
                sendUpdates(patch);
                break;
            default:
                return;
        }

    }

    public void sendUpdates(IFarmPatch patch) {
        // TODO save
        PatchState state = patches.get(patch);
        int varbitId = ObjectDefinitions.forId(patch.getObjectId()).getVarbitId();
        if (varbitId < 0) {
            return;
        }
        int value = state.getPlant().getVarbit(state.getStage(), state.getPlantState());
        if (player.getStateSet().getBitState(varbitId) != value) {
            player.getStateSet().setBitState(varbitId, value);
        }

    }

    private void water(IFarmPatch patch) {
        PatchState state = patches.get(patch);

        switch (state.getPlantState()) {
            case DEAD:
                player.sendMessage("That plant is dead, you should dig it up with a spade.");
                return;
            case DISEASED:
                player.sendMessage("That plant is diseased, you should find something to cure it with.");
                return;
            case WATERED:
                player.sendMessage("That plant is already watered. You should let it grow some more before watering it again.");
                return;
            case GROWN:
                player.sendMessage("That plant is already fully grown, you should harvest it.");
                return;

            case GROWING:

                // TODO animation, use water from watering can
                player.sendMessage("You water the plant.");
                player.playAnimation(Farming.WATERING_ANIM);
                // state.plantState = PlantState.WATERED;
                sendUpdates(patch);
                break;
        }
    }

}
