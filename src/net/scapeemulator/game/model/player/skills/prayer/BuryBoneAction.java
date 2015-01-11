package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.task.Action;

/**
 * @author David Insley
 */
public class BuryBoneAction extends Action<Player> {

    private static final Animation BURY_ANIMATION = new Animation(827);

    private final Bone bone;
    private final SlottedItem item;

    private boolean started;

    public BuryBoneAction(Player player, Bone bone, SlottedItem item) {
        super(player, 3, true);
        this.bone = bone;
        this.item = item;
    }

    @Override
    public void execute() {
        if (!started) {
            mob.getWalkingQueue().reset();
            mob.setActionsBlocked(true);
            mob.getInventory().remove(item);
            mob.sendMessage("You dig a hole in the ground...");
            mob.playAnimation(BURY_ANIMATION);
            mob.getSkillSet().addExperience(Skill.PRAYER, bone.getXp());
            started = true;
        } else {
            mob.sendMessage("You bury the bones.");
            mob.setActionsBlocked(false);
            stop();
        }

    }

    @Override
    public void stop() {
        mob.setActionsBlocked(false);
        super.stop();
    }

}