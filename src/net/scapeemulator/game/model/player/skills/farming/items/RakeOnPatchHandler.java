package net.scapeemulator.game.model.player.skills.farming.items;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.farming.PatchState;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;
import net.scapeemulator.game.model.player.skills.farming.plant.WeedsPlant;

public class RakeOnPatchHandler extends ItemOnObjectHandler {

    private static final Animation RAKE_ANIM = new Animation(2273);
    private static final Item WEEDS = new Item(6055);
    private static final int RAKE = 5341;

    private final IFarmPatch patch;

    public RakeOnPatchHandler(IFarmPatch patch) {
        super(RAKE, patch.getObjectId());
        this.patch = patch;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new RakePatchAction(player, object));
    }

    private class RakePatchAction extends ReachObjectAction {

        private boolean first = true;

        public RakePatchAction(Player player, GroundObject object) {
            super(1, false, player, object, 1, true);
        }

        @Override
        public void executeAction() {
            if (first) {
                mob.turnToPosition(bounds.center());
                setDelay(5);
            }
            
            if (!mob.getInventory().contains(RAKE)) {
                mob.sendMessage("You seem to have lost your rake!");
                stop();
                return;
            }
            
            PatchState state = mob.getFarms().getPatchState(patch);
            IPlant plant = state.getPlant();
            if (plant instanceof WeedsPlant) {
                if (plant == WeedsPlant.EMPTY) {
                    mob.sendMessage("The patch is empty of weeds.");
                    stop();
                    return;
                } else {
                    mob.playAnimation(RAKE_ANIM);
                    if (first) {
                        first = false;
                        return;
                    }

                    // the add method returns null if the item is completely added
                    if (mob.getInventory().add(WEEDS) != null) {
                        World.getWorld().getGroundItems().add(WEEDS.getId(), WEEDS.getAmount(), mob.getPosition(), mob);
                    }
                    mob.getSkillSet().addExperience(Skill.FARMING, 4.0);
                    state.setPlant(((WeedsPlant) plant).next(false));
                    mob.getFarms().sendUpdates(patch);
                    return;
                }
            } else {
                mob.sendMessage("You don't think you can rake that up!");
                stop();
            }
        }

        @Override
        public void stop() {
            mob.cancelAnimation();
            super.stop();
        }

    }
}
