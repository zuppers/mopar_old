package net.scapeemulator.game.model.player.skills.construction.furniture;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class CustomMaterialRequirement extends MaterialRequirement {

    private final String text;
    private final double xp;
    private final Item[] items;

    public CustomMaterialRequirement(String text, double xp, Item... items) {
        super(null, 0);
        this.text = text;
        this.xp = xp;
        this.items = items;
    }

    public CustomMaterialRequirement(String text, double xp, int... itemIds) {
        super(null, 0);
        this.text = text;
        this.xp = xp;
        items = new Item[itemIds.length];
        for (int i = 0; i < itemIds.length; i++) {
            items[i] = new Item(itemIds[i]);
        }
    }

    public Item[] getItems() {
        return items;
    }

    @Override
    public double getXp() {
        return xp;
    }

    @Override
    public boolean hasRequirement(Player player) {
        for (Item item : items) {
            if (!player.getInventory().contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void fulfill(Player player) {
        for (Item item : items) {
            player.getInventory().remove(item);
        }
    }

    @Override
    public String toString() {
        return text;
    }
}
