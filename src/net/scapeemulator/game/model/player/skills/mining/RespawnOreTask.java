package net.scapeemulator.game.model.player.skills.mining;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.task.Task;

/**
 * @author David Insley
 */
public class RespawnOreTask extends Task {

    private final GroundObject object;
    private final int oreId;

    public RespawnOreTask(int delay, GroundObject object, int oreId) {
        super(delay, false);
        this.object = object;
        this.oreId = oreId;
    }

    @Override
    public void execute() {
        object.setId(oreId);
        stop();
    }

}
