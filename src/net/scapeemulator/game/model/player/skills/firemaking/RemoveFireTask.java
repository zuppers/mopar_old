package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.task.Task;

/**
 * @author David Insley
 */
class RemoveFireTask extends Task {

    private static final int ASHES = 592;

    private final GroundObject fire;

    RemoveFireTask(GroundObject fire) {
        super((int) (Math.random() * (40)) + 30, false);
        this.fire = fire;
    }

    @Override
    public void execute() {
        World.getWorld().getGroundItems().add(ASHES, 1, fire.getPosition(), null);
        if (World.getWorld().getGroundObjects().contains(fire.getId(), fire.getPosition())) {
            World.getWorld().getGroundObjects().remove(fire.getPosition(), fire.getType().getObjectGroup());
        }
        stop();
    }
}
