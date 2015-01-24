package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.Door;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.player.Player;

/**
 * @author Hadyn Richard
 */
public final class OpenDoorAction extends ReachDistancedAction {

    private final Door door;
    private final boolean open;
    private final Player player;
    private final Position position;
    
    private boolean executed;

    public OpenDoorAction(Player player, Position position, Door door, boolean open) {
        super(1, true, player, position, 1);
        this.player = player;
        this.position = position;
        this.door = door;
        this.open = open;
    }

    @Override
    public void executeAction() {

        /* If the player is still walking, dont execute the action and assume that next time he will be done wallking */
        if(!executed && !player.getWalkingQueue().isEmpty()) {
            executed = true;
            return;
        }
        
        player.turnToPosition(position);

        if(open) {
            door.open();
        } else {
            door.close();
        }
        
        stop();
    }
}
