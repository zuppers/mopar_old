package net.scapeemulator.game.model.player.skills.farming.items;

import net.scapeemulator.game.dialogue.Dialogue;
import net.scapeemulator.game.dialogue.DialogueContext;
import net.scapeemulator.game.dialogue.DialogueOption;
import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.farming.PatchState;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;
import net.scapeemulator.game.model.player.skills.farming.plant.WeedsPlant;

public class SpadeOnPatchHandler extends ItemOnObjectHandler {

    private static final Animation SPADE_ANIM = new Animation(-1);
    private static final int SPADE = 952;

    private final IFarmPatch patch;

    public SpadeOnPatchHandler(IFarmPatch patch) {
        super(SPADE, patch.getObjectId());
        this.patch = patch;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new SpadePatchAction(player, object));
    }

    private class SpadePatchAction extends ReachObjectAction {

        public SpadePatchAction(Player player, GroundObject object) {
            super(1, false, player, object, 1, true);
        }

        @Override
        public void executeAction() {
            mob.turnToPosition(bounds.center());
            PatchState state = mob.getFarms().getPatchState(patch);
            IPlant plant = state.getPlant();
            if (plant instanceof WeedsPlant) {
                mob.sendMessage(plant == WeedsPlant.EMPTY ? "There's nothing there to dig up!" : "You should clear the weeds with a rake.");
                stop();
                return;
            }
            switch (state.getPlantState()) {
                case DEAD:
                    break;
                case GROWN:
                    break;
                case WATERED:
                case GROWING:
                case DISEASED:
                    new AreYouSure().displayTo(mob);
                    break;

                case HEALTH_CHECKED:
                    mob.sendMessage("");
                    break;
                case STUMP:
                    break;
                default:
                    break;

            }
            clear();
        }

        private void clear() {
            mob.playAnimation(SPADE_ANIM);
            mob.sendMessage("You clear the patch.");
            mob.getFarms().getPatchState(patch).setPlant(WeedsPlant.EMPTY);
            mob.getFarms().sendUpdates(patch);
            stop();
        }

        @SuppressWarnings("rawtypes")
        private class AreYouSure extends Dialogue {

            @Override
            public void initialize(DialogueContext ctx) {
                ctx.openTextDialogue("Are you sure you want to dig that up?", true);
            }

            @Override
            public void handleOption(DialogueContext ctx, DialogueOption opt) {
                switch (opt) {
                    case CONTINUE:
                        ctx.openOptionDialogue("Yes, I want to dig it up.", "No, I'll keep it for now.");
                        break;
                    case OPTION_1:
                        clear();
                        ctx.stop();
                        break;
                    default:
                        ctx.stop();
                        break;
                }

            }

        }

    }
}
