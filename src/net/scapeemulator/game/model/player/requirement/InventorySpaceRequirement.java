package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class InventorySpaceRequirement extends Requirement {

    public static InventorySpaceRequirement ONE_SLOT = new InventorySpaceRequirement(1);

    private final int amount;
    private final String error;

    public InventorySpaceRequirement(int amount) {
        this(amount, "You do not have enough free inventory space.");
    }

    public InventorySpaceRequirement(int amount, String error) {
        this.amount = amount;
        this.error = error;
    }

    @Override
    public boolean hasRequirement(Player player) {
        return player.getInventory().freeSlots() >= amount;
    }

    @Override
    public void displayErrorMessage(Player player) {
        player.sendMessage(error);
    }

    @Override
    public void fulfill(Player player) {
    }

}
