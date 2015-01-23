package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.ObjectOrientation;
import net.scapeemulator.game.model.object.ObjectType;
import net.scapeemulator.game.model.pathfinding.Path;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.task.Action;

/**
 * @author David Insley
 */
public class FiremakingAction extends Action<Player> {

    private static final Animation ANIMATION = new Animation(733);
    private static final Direction[] PREFERRED_DIRECTIONS = { Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH };

    private final Log log;
    private final SlottedItem slottedLog;
    private int status = -2;

    public FiremakingAction(Player player, Log log, SlottedItem slottedLog) {
        super(player, 2, true);
        this.log = log;
        this.slottedLog = slottedLog;
    }

    @Override
    public void execute() {
        if (status == -2) {
            mob.getWalkingQueue().reset();
            status = -1;
        }

        if (!log.getRequirements().hasRequirementsDisplayOne(mob)) {
            stop();
            return;
        }

        // See if the ground object list has an empty space for us here.
        // TODO add areas for banks etc where you can't light fires
        if (!World.getWorld().getGroundObjects().isEmpty(mob.getPosition())) {
            mob.sendMessage("You can't light a fire here.");
            stop();
            return;
        }

        if (status == -1) {
            log.getRequirements().fulfillAll(mob);
            Item removed = mob.getInventory().remove(slottedLog.getItem(), slottedLog.getSlot());
            if (!removed.equals(slottedLog.getItem())) {
                stop();
                return;
            }
            mob.playAnimation(ANIMATION);
            int dif = mob.getSkillSet().getCurrentLevel(Skill.FIREMAKING) - log.getLevel();
            dif = dif > 15 ? 15 : dif;
            status = (int) (Math.random() * (16 - dif)) + 1;
        }

        if (!World.getWorld().getGroundItems().contains(log.getItemId(), mob.getPosition(), mob)) {
            mob.sendMessage("Your logs have disappeared!");
            stop();
            return;
        }

        status -= 1;

        if (status == 0) {
            World.getWorld().getGroundItems().remove(log.getItemId(), mob.getPosition(), mob);
            GroundObject fire = World.getWorld().getGroundObjects().put(mob.getPosition(), log.getFireId(), ObjectOrientation.WEST, ObjectType.PROP);
            if (fire != null) {
                World.getWorld().getTaskScheduler().schedule(new RemoveFireTask(fire));
            }

            mob.getSkillSet().addExperience(Skill.FIREMAKING, log.getXp());
            mob.cancelAnimation();
            mob.sendMessage("The fire catches and the logs begin to burn.");
            for (Direction direction : PREFERRED_DIRECTIONS) {
                if (mob.canTraverse(direction)) {
                    Path path = new Path();
                    path.addFirst(new Position(mob.getPosition().getX() + direction.getX(), mob.getPosition().getY() + direction.getY()));
                    mob.walkPath(path);
                    break;
                }
            }

            if (fire != null) {
                mob.turnToPosition(fire.getPosition());
            }

            stop();
        }

    }
}
