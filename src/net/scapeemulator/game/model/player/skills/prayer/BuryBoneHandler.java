package net.scapeemulator.game.model.player.skills.prayer;

import net.scapeemulator.game.item.ItemInteractHandler;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.task.Action;

/**
 * @author David Insley
 */
public class BuryBoneHandler extends ItemInteractHandler {

    private static final Animation BURY_ANIMATION = new Animation(827);

    private final Bone bone;

    public BuryBoneHandler(Bone bone) {
        super(bone.getItemId());
        this.bone = bone;
    }

    @Override
    public void handle(Player player, SlottedItem item) {
        player.getWalkingQueue().reset();
        player.setActionsBlocked(true);
        World.getWorld().getTaskScheduler().schedule(new Action<Player>(player, 3, false) {
            public void execute() {
                mob.sendMessage("You bury the bones.");
                mob.setActionsBlocked(false);
                stop();
            }
        });
        player.getInventory().remove(item);
        player.sendMessage("You dig a hole in the ground...");
        player.playAnimation(BURY_ANIMATION);
        player.getSkillSet().addExperience(Skill.PRAYER, bone.getXp());
    }

}