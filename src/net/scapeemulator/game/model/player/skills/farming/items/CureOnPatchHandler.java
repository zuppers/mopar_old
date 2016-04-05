package net.scapeemulator.game.model.player.skills.farming.items;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.farming.CureType;
import net.scapeemulator.game.model.player.skills.farming.PlantState;
import net.scapeemulator.game.model.player.skills.farming.PatchState;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;

public class CureOnPatchHandler extends ItemOnObjectHandler {

    private static final Item EMPTY_VIAL = new Item(229);

    private final IFarmPatch patch;

    public CureOnPatchHandler(IFarmPatch patch) {
        super(CureType.PLANT_CURE.getItemId(), patch.getObjectId());
        this.patch = patch;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new CurePatchAction(player, object, item));
    }

    private class CurePatchAction extends ReachObjectAction {

        private final SlottedItem itemUsed;

        public CurePatchAction(Player player, GroundObject object, SlottedItem itemUsed) {
            super(1, false, player, object, 1, true);
            this.itemUsed = itemUsed;
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

            if (state.getPlant().getCureType() != CureType.PLANT_CURE) {
                mob.sendMessage("That doesn't look like it would be very useful on that plant.");
                stop();
                return;
            }

            if (state.getPlantState() == PlantState.DISEASED) {
                Item removed = mob.getInventory().remove(itemUsed);
                if (removed != null) {
                    mob.getInventory().add(EMPTY_VIAL, itemUsed.getSlot());
                    mob.playAnimation(CureType.PLANT_CURE.getAnimation());
                    mob.sendMessage("You empty the vial on the diseased plant and it starts to look healthier.");
                    state.setPlantState(PlantState.GROWING);
                    mob.getFarms().sendUpdates(patch);
                } else {
                    mob.sendMessage("You seem to have lost your plant cure!");
                }
            } else {
                mob.sendMessage("There's nothing in that patch that looks diseased.");
            }
            stop();
        }

    }
}
