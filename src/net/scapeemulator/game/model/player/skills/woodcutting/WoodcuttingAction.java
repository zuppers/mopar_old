package net.scapeemulator.game.model.player.skills.woodcutting;

import static net.scapeemulator.game.model.player.skills.Skill.WOODCUTTING;

import java.util.Random;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachObjectAction;

/**
 * @author David Insley
 */
public class WoodcuttingAction extends ReachObjectAction {

    private enum State {
        WALKING, START, CHOPPING
    }

    private static final Random rand = new Random();

    private final TreeType type;
    private final GroundObject object;
    private final int originalId;

    private State state;
    private Hatchet hatchet;

    public WoodcuttingAction(Player player, TreeType type, GroundObject object) {
        super(2, true, player, object, 1);
        this.type = type;
        this.object = object;
        originalId = object.getId();
        state = State.WALKING;
    }

    @Override
    public void executeAction() {
        switch (state) {
        case WALKING:
            mob.getWalkingQueue().reset();
            mob.turnToPosition(object.getCenterPosition());
            state = State.START;
            return;
        case START:
            if (!check()) {
                stop();
                return;
            }
            mob.sendMessage("You swing your axe at the tree.");
            mob.playAnimation(hatchet.getAnimation());
            state = State.CHOPPING;
            break;
        case CHOPPING:
            if (!check()) {
                stop();
                return;
            }
            mob.playAnimation(hatchet.getAnimation());
            int levelDifference = wcLvl() - type.getLevel();
            // TODO actual formula
            boolean shouldGetLog = rand.nextInt(4) == 0;
            if (shouldGetLog) {
                mob.getSkillSet().addExperience(WOODCUTTING, type.getXp());
                Item log = new Item(type.getLogId());
                mob.getInventory().add(log);
                mob.sendMessage("You get some " + log.getDefinition().getName().toLowerCase() + ".");

                /*
                 * The chance of the tree depleting is determined by the players level and the
                 * average number of logs the tree should give.
                 */
                double logMultiplier = levelDifference >= 50 ? 1.99 : 1.0 + (levelDifference / 50.0);
                if (rand.nextInt((int) (type.getAverageLogs() * logMultiplier)) == 0) {
                    object.setId(type.getStumpId());
                    mob.cancelAnimation();
                    World.getWorld().getTaskScheduler().schedule(new RegrowTask(type.getRespawnTicks(), object, originalId));
                    stop();
                    return;
                }
            }
            break;
        }
    }

    /**
     * Checks to make sure the following conditions are true for the player: level requirement to
     * chop the tree, the tree still exists, level appropriate hatchet to use, and a free inventory
     * space for a log.
     * 
     * @return true if the player can chop this tree, false otherwise
     */
    private boolean check() {
        if (!type.getRequirements().hasRequirementsDisplayOne(mob) || object.getId() != originalId) {
            return false;
        }
        hatchet = findHatchet();
        if (hatchet == null) {
            mob.sendMessage("You do not have an axe which you have the woodcutting level to use.");
            return false;
        }
        if (mob.getInventory().freeSlots() < 1) {
            mob.sendMessage("Your inventory is too full to hold any more logs.");
            return false;
        }
        return true;
    }

    /**
     * Alias to get the players current Woodcutting level.
     * 
     * @return players current woodcutting level
     */
    private int wcLvl() {
        return mob.getSkillSet().getCurrentLevel(WOODCUTTING);
    }

    /**
     * Searches the players weapon and inventory for the best Hatchet they can use.
     * 
     * @return the best Hatchet the player currently has that they can use.
     */
    private Hatchet findHatchet() {
        Hatchet best = null;
        for (Hatchet hatchet : Hatchet.values()) {
            if (hatchet.getRequirements().hasRequirements(mob)) {
                if (best == null || hatchet.getSpeed() > best.getSpeed()) {
                    best = hatchet;
                }
            }
        }
        return best;
    }

}
