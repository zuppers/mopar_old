package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class PickupItemAction extends ReachDistancedAction {

    private final int itemId;
    private final Position position;

    public PickupItemAction(Player player, int itemId, Position position) {
        super(1, true, player, position, 0);
        this.itemId = itemId;
        this.position = position;
    }

    @Override
    public void executeAction() {
        // Check if the item has been removed since we started the action
        GroundItem groundItem = World.getWorld().getGroundItems().get(itemId, position, mob);
        if (groundItem != null) {
            Item remaining = mob.getInventory().add(groundItem.toItem());
            if (remaining != null) {
                groundItem.setAmount(remaining.getAmount());
            } else {
                World.getWorld().getGroundItems().remove(itemId, position, mob);
            }
        }
        stop();
    }

}
