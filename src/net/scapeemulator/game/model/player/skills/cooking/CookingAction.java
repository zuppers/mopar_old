package net.scapeemulator.game.model.player.skills.cooking;

import java.util.Random;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.GroundObjectListenerAdapter;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.model.player.skills.MakeItemInterface;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.MakeItemInterface.MakeItemInterfaceListener;

/**
 * @author David Insley
 */
public class CookingAction extends ReachObjectAction {

    private static final Random random = new Random();

    private enum State {
        INIT,
        WALKING,
        START,
        WAITING,
        COOKING
    }

    private final SlottedItem item;
    private final HeatSource heatSource;
    private final RawFood food;
    private final FireObjectListener listener;
    private final GroundObject object;
    private State state;
    private int amount;

    public CookingAction(Player player, HeatSource heatSource, RawFood food, SlottedItem item, GroundObject object) {
        super(1, true, player, object, 1);
        this.heatSource = heatSource;
        this.food = food;
        this.item = item;
        this.object = object;
        listener = new FireObjectListener();
        state = State.INIT;
    }

    @Override
    public void executeAction() {
        switch (state) {
            case INIT:
                World.getWorld().getGroundObjects().addListener(listener);
                state = State.WALKING;
                executeAction();
                return;
            case WALKING:
                if (mob.getWalkingQueue().isEmpty()) {
                    state = State.START;
                    mob.turnToPosition(object.getCenterPosition());
                }
                return;
            case START:
                MakeItemInterface.showMakeItemInterface(mob, new CookItemInterfaceListener(), new Item(food.getRawId()), true);
                state = State.WAITING;
                return;
            case WAITING:
                return;
            case COOKING:
                if (amount-- < 1) {
                    stop();
                    return;
                }
                if (!food.getRequirements().hasRequirementsDisplayOne(mob)) {
                    stop();
                    return;
                }

                mob.playAnimation(heatSource.getAnimation());
                mob.getInventory().remove(item);

                /* - Credits to Abyssal Noob - */
                int lvlReq = food.getLevelReq();
                double burnChance = (55.0 - heatSource.getCookingBonus());
                int stopBurn = food.getStopBurn();
                if (mob.getEquipment().get(Equipment.HANDS) != null) {
                    stopBurn -= mob.getEquipment().get(Equipment.HANDS).getId() == Cooking.COOKING_GAUNTLETS ? food.getGauntletMod() : 0;
                }
                int burnLvlDelta = (stopBurn - lvlReq);
                double burnDecrease = (burnChance / burnLvlDelta);
                int lvlReqDelta = (mob.getSkillSet().getCurrentLevel(Skill.COOKING) - lvlReq);
                burnChance -= (lvlReqDelta * burnDecrease);
                double randNum = random.nextDouble() * 100.0;
                /* --------------------------- */

                String name = ItemDefinitions.forId(food.getCookedId()).getName().toLowerCase();
                if (burnChance <= randNum) {
                    mob.getInventory().add(new Item(food.getCookedId()), item.getSlot());
                    mob.sendMessage("You successfully cook some " + name + ".");
                    mob.getSkillSet().addExperience(Skill.COOKING, food.getXp());
                } else {
                    mob.getInventory().add(new Item(food.getBurnedId()), item.getSlot());
                    mob.sendMessage("You accidentally burn the " + name + ".");
                }

                food.getRequirements().fulfillAll(mob);
                return;
        }

    }

    @Override
    public void stop() {
        World.getWorld().getGroundObjects().removeListener(listener);
        mob.cancelAnimation();
        super.stop();
    }

    private class CookItemInterfaceListener extends MakeItemInterfaceListener {

        @Override
        public void makeAllSelected() {
            start(mob.getInventory().getAmount(food.getRawId()));
        }

        @Override
        public void makeAmountSelected(int amt) {
            start(amt);
        }

        @Override
        public void cancelled() {
            stop();
        }

        private void start(int amt) {
            amount = amt;
            setDelay(4);
            state = State.COOKING;
        }

    }

    private class FireObjectListener extends GroundObjectListenerAdapter {

        @Override
        public void groundObjectIdUpdated(GroundObject updated, int oldId) {
            if (object == updated) {
                fireOut();
            }
        }

        @Override
        public void groundObjectRemoved(GroundObject removed) {
            if (object == removed) {
                fireOut();
            }
        }

        private void fireOut() {
            mob.sendMessage("Your fire has gone out.");
            stop();
        }
    }
}