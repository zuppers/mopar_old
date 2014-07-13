package net.scapeemulator.game.model.player.skills.woodcutting;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.task.Task;

/**
 * @author David Insley
 */
public class RegrowTask extends Task {

    private final GroundObject object;
    private final int treeId;

    public RegrowTask(int delay, GroundObject object, int treeId) {
        super(delay, false);
        this.object = object;
        this.treeId = treeId;
    }

    @Override
    public void execute() {
        object.setId(treeId);
        stop();
    }

}
