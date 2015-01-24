package net.scapeemulator.game.model.player.skills.mining;

import java.util.Random;

import static net.scapeemulator.game.model.player.skills.Skill.MINING;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;

/**
 * @author David Insley
 */
public class MiningAction extends ReachDistancedAction {

    private enum State {
        WALKING, START, MINING
    }

    private static final Random rand = new Random();

    private final RockType type;
    private final GroundObject object;
    private final int originalId;

    private State state;
    private Pickaxe pickaxe;

    public MiningAction(Player player, RockType type, GroundObject object) {
        super(1, true, player, object.getBounds(), 1);
        this.type = type;
        this.object = object;
        originalId = object.getId();
        state = State.WALKING;
    }

    @Override
    public void executeAction() {
        switch (state) {
        case WALKING:
            if (!mob.getWalkingQueue().isEmpty()) {
                return;
            }
            mob.turnToPosition(object.getCenterPosition());
            state = State.START;
            return;
        case START:
            if (!check()) {
                stop();
                return;
            }
            mob.sendMessage("You swing your pick at the rock.");
            mob.playAnimation(pickaxe.getAnimation());
            state = State.MINING;
            break;
        case MINING:
            if (!check()) {
                stop();
                return;
            }
            mob.playAnimation(pickaxe.getAnimation());
            int levelDifference = mob.getSkillSet().getCurrentLevel(MINING) - type.getLevel();
            // TODO actual formula
            boolean shouldGetOre = rand.nextInt(8) == 0;
            if (shouldGetOre) {
                mob.getSkillSet().addExperience(MINING, type.getXp());
                Item ore = new Item(type.getOreId());
                mob.getInventory().add(ore);
                mob.sendMessage("You manage to mine some " + ore.getDefinition().getName().toLowerCase() + ".");
                object.setId(Mining.getDepletedId(originalId));
                mob.cancelAnimation();
                World.getWorld().getTaskScheduler().schedule(new RespawnOreTask(type.getRespawnTicks(), object, originalId));
                stop();
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
        pickaxe = findPickaxe();
        if (pickaxe == null) {
            mob.sendMessage("You do not have a pickaxe which you have the mining level to use.");
            return false;
        }
        if (mob.getInventory().freeSlots() < 1) {
            mob.sendMessage("Your inventory is too full to hold any more ore.");
            return false;
        }
        return true;
    }

    /**
     * Searches the players weapon and inventory for the best pick they can use.
     * 
     * @return the best pick the player currently has that they can use
     */
    private Pickaxe findPickaxe() {
        Pickaxe best = null;
        for (Pickaxe hatchet : Pickaxe.values()) {
            if (hatchet.getRequirements().hasRequirements(mob)) {
                if (best == null || hatchet.getSpeed() > best.getSpeed()) {
                    best = hatchet;
                }
            }
        }
        return best;
    }

}
