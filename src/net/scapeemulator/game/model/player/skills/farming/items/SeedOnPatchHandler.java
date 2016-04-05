package net.scapeemulator.game.model.player.skills.farming.items;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.farming.PatchState;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public class SeedOnPatchHandler extends ItemOnObjectHandler {

    static final Animation PLANT_SEED = new Animation(2291);

    private final IPlant plant;
    private final IFarmPatch patch;
    private final boolean valid;

    public SeedOnPatchHandler(IPlant plant, IFarmPatch patch) {
        super(plant.getSeedId(), patch.getObjectId());
        this.plant = plant;
        this.patch = patch;
        for (IPlant validPlant : patch.getValidPlants()) {
            if (plant == validPlant) {
                valid = true;
                return;
            }
        }
        valid = false;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new SeedOnPatchAction(player, object));

    }

    private class SeedOnPatchAction extends ReachObjectAction {

        public SeedOnPatchAction(Player player, GroundObject object) {
            super(1, false, player, object, 1, true);
        }

        @Override
        public void executeAction() {
            mob.turnToPosition(bounds.center());
            PatchState state = mob.getFarms().getPatchState(patch);

            if (!valid) {
                mob.sendMessage("You can't plant that in this patch.");
                stop();
                return;
            }

            if (state.isEmpty()) {
                if (plant.getRequirements().hasRequirementsDisplayOne(mob)) {
                    plant.getRequirements().fulfillAll(mob);
                    mob.playAnimation(PLANT_SEED);
                    state.setPlant(plant);
                    mob.getFarms().sendUpdates(patch);
                }
            } else {
                mob.sendMessage("You can only plant that in an empty and weeded patch.");
            }
            stop();
        }

    }

}
