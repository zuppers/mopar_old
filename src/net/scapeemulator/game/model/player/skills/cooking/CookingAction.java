package net.scapeemulator.game.model.player.skills.cooking;

import java.util.Random;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.GroundObjectListenerAdapter;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.ScriptInputListener;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemMessage;
import net.scapeemulator.game.task.DistancedAction;

/**
 * @author David Insley
 */
public class CookingAction extends DistancedAction<Player> {

    private static final int COOKING_INTERFACE = 307;
    private static final Random random = new Random();

    private enum State {
        WALKING, INIT, WAITING, COOKING
    }

    private final SlottedItem item;
    private final GroundObject object;
    private final HeatSource heatSource;
    private final RawFood food;
    private final FireObjectListener listener;
    private State state;
    private int amount;

    public CookingAction(Player player, HeatSource heatSource, RawFood food, SlottedItem item, GroundObject object) {
        super(1, true, player, object.getBounds(), 1);
        this.heatSource = heatSource;
        this.food = food;
        this.item = item;
        this.object = object;
        player.turnToPosition(object.getCenterPosition());
        listener = new FireObjectListener();
        World.getWorld().getGroundObjects().addListener(listener);
        state = State.WALKING;
    }

    @Override
    public void executeAction() {
        switch (state) {
        case WALKING:
            if (!mob.getWalkingQueue().isEmpty()) {
                return;
            }
            state = State.INIT;
            break;
        case INIT:
            mob.getInterfaceSet().openChatbox(COOKING_INTERFACE, new CookingInterfaceListener());
            mob.send(new InterfaceItemMessage(COOKING_INTERFACE, 2, 150, food.getRawId()));
            mob.setInterfaceText(COOKING_INTERFACE, 6, "<br><br><br><br>" + ItemDefinitions.forId(food.getRawId()).getName());
            state = State.WAITING;
            break;
        case WAITING:
            break;
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

            // Credits to Abyssal Noob
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
            break;
        }

    }

    private void fireOut() {
        mob.sendMessage("Your fire has gone out.");
        stop();
    }

    @Override
    public void stop() {
        World.getWorld().getGroundObjects().removeListener(listener);
        mob.cancelAnimation();
        super.stop();
    }

    private class CookingInterfaceListener extends ComponentListener {

        @Override
        public void inputPressed(Component component, int componentId, int dynamicId) {
            if (component.getCurrentId() != COOKING_INTERFACE) {
                stop();
                return;
            }
            componentId -= 3;
            switch (componentId) {
            case 0:
                amount = 28;
                state = State.COOKING;
                break;
            case 1:
                mob.getScriptInput().showIntegerScriptInput("How many would you like to cook?", new ScriptInputListener() {
                    @Override
                    public void intInputReceived(int value) {
                        amount = value;
                        state = State.COOKING;
                    }

                    @Override
                    public void usernameInputReceived(long value) {
                    }
                });
                break;
            case 2:
                amount = 5;
                state = State.COOKING;
                break;
            case 3:
                amount = 1;
                state = State.COOKING;
                break;
            }
            setDelay(4);
            component.removeListener();
            component.reset();
        }

        @Override
        public void componentClosed(Component component) {
            stop();
        }

    }

    private class FireObjectListener extends GroundObjectListenerAdapter {

        @Override
        public void groundObjectUpdated(GroundObject updated) {
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
    }
}
