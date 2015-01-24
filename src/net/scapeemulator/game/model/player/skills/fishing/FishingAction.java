package net.scapeemulator.game.model.player.skills.fishing;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public class FishingAction extends ReachDistancedAction {

    private final FishingTool fishingTool;
    private final Fish[] fish;
    private State state;

    private enum State {
        WALKING, INIT, FISHING
    }

    public FishingAction(Player player, NPC npc, FishingSpot fishingSpot, Option option) {
        super(3, true, player, npc.getPosition(), 1);
        player.turnToTarget(npc);
        fishingTool = fishingSpot.getTool(option);
        fish = fishingSpot.getFish(option);
        state = State.WALKING;
    }

    @Override
    public void executeAction() {
        if (state == State.WALKING) {
            state = State.INIT;
            mob.sendMessage(fishingTool.getMessage());
        }
        if (mob.getInventory().freeSlot() == -1) {
            mob.sendMessage("You do not have enough inventory space to hold any more fish.");
            stop();
            return;
        }
        if (!mob.getInventory().contains(fishingTool.getToolId())) {
            mob.sendMessage("You need a " + ItemDefinitions.name(fishingTool.getToolId()).toLowerCase() + " to fish there.");
            stop();
            return;
        }
        if (fishingTool.getBaitId() != -1 && !mob.getInventory().contains(fishingTool.getBaitId())) {
            mob.sendMessage("You don't have any fishing bait left.");
            stop();
            return;
        }
        boolean hasLevelReq = false;
        int playerLevel = mob.getSkillSet().getCurrentLevel(Skill.FISHING);
        int roll = (int) (Math.random() * 100);
        Fish caught = null;
        for (Fish f : fish) {
            int dif = playerLevel - f.getLevel();
            if (dif >= 0) {
                hasLevelReq = true;
                int chance = (dif * 2) + 20;
                chance = chance > 70 ? 70 : chance;
                if (chance > roll && (caught == null || caught.getLevel() < f.getLevel())) {
                    caught = f;
                }
            }
        }

        if (!hasLevelReq) {
            mob.sendMessage("You need a higher Fishing level to fish here.");
            stop();
            return;
        }

        mob.playAnimation(fishingTool.getAnimation());

        if (state == State.INIT) {
            state = State.FISHING;
            return;
        }

        if (caught == null) {
            return;
        }

        if (fishingTool.getBaitId() != -1) {
            mob.getInventory().remove(new Item(fishingTool.getBaitId()));
        }

        Item caughtI = new Item(caught.getRawId());
        mob.getInventory().add(caughtI);
        String caughtName = caughtI.getDefinition().getName().toLowerCase();
        mob.sendMessage("You manage to catch" + (caughtName.endsWith("s") ? " some " : " a ") + caughtName + ".");
        mob.getSkillSet().addExperience(Skill.FISHING, caught.getXp());

    }

    public void stop() {
        mob.cancelAnimation();
        super.stop();
    }

}
