package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class ItemRequirement extends Requirement {

    private final Item item;
    private final boolean remove;
    private final String error;

    public ItemRequirement(int itemId, boolean remove) {
        this(new Item(itemId, 1), remove, null);
    }

    public ItemRequirement(int itemId, int amount, boolean remove) {
        this(new Item(itemId, amount), remove, null);
    }

    public ItemRequirement(Item item, boolean remove) {
        this(item, remove, null);
    }

    public ItemRequirement(Item item, boolean remove, String error) {
        this.item = item;
        this.remove = remove;
        this.error = error;
    }

    @Override
    public boolean hasRequirement(Player player) {
        return player.getInventory().getAmount(item.getId()) >= item.getAmount();
    }

    @Override
    public void displayErrorMessage(Player player) {
        if (error != null) {
            player.sendMessage(error);
        }
    }

    @Override
    public void fulfill(Player player) {
        if (remove) {
            player.getInventory().remove(item);
        }
    }

}
