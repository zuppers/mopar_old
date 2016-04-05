package net.scapeemulator.game.model.player.skills.farming.items;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.PlantState;
import net.scapeemulator.game.model.player.skills.farming.PatchState;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;

public class SecateursOnPatchHandler extends ItemOnObjectHandler {

    private final IFarmPatch patch;

    public SecateursOnPatchHandler(IFarmPatch patch) {
        super(CureType.PRUNING.getItemId(), patch.getObjectId());
        this.patch = patch;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new PrunePatchAction(player, object));
    }

    private class PrunePatchAction extends ReachObjectAction {

        public PrunePatchAction(Player player, GroundObject object) {
            super(1, false, player, object, 1, true);
        }

        @Override
        public void executeAction() {
            mob.turnToPosition(bounds.center());
            PatchState state = mob.getFarms().getPatchState(patch);

            if (state.getPlantState() == PlantState.DEAD) {
                mob.sendMessage("That plant is dead, you should clear it with a spade.");
                stop();
                return;
            }

            if (state.getPlant().getCureType() != CureType.PRUNING) {
                mob.sendMessage("That doesn't look like it would be very useful on that plant.");
                stop();
                return;
            }

            if (state.getPlantState() == PlantState.DISEASED) {
                state.setPlantState(PlantState.GROWING);
                mob.getFarms().sendUpdates(patch);
                mob.playAnimation(CureType.PRUNING.getAnimation());
                mob.sendMessage("You prune away the sick branches and the plant looks much healthier.");
            } else if (state.getPlantState() == PlantState.HEALTH_CHECKED) {
                // TODO willow tree branches
            } else {
                mob.sendMessage("There's nothing there that needs pruning.");
            }

            stop();
        }

    }
}
