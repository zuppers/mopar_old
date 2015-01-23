package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.task.DistancedAction;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class PickupItemAction extends DistancedAction<Player> {

    private final GroundItem groundItem;

    public PickupItemAction(Player player, GroundItem groundItem) {
        super(1, true, player, groundItem.getPosition(), 0);
        this.groundItem = groundItem;
    }

    @Override
    public void executeAction() {

        // Check if the item has been removed since we started the action
        if (!World.getWorld().getGroundItems().contains(groundItem)) {
            return;
        }
        Item remaining = mob.getInventory().add(groundItem.toItem());
        if (remaining != null) {
            groundItem.setAmount(remaining.getAmount());
        } else {
            World.getWorld().getGroundItems().remove(groundItem);
        }
        stop();
    }

}
