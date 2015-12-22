package net.scapeemulator.game.model.player.skills.construction.furniture;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.requirement.Requirement;

/**
 * @author David Insley
 */
public class MaterialRequirement extends Requirement {

    private final Material material;
    private final int amount;

    public MaterialRequirement(Material material) {
        this(material, 1);
    }

    public MaterialRequirement(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public double getXp() {
        return material.getXp() * amount;
    }

    @Override
    public boolean hasRequirement(Player player) {
        return player.getInventory().getAmount(material.getItemId()) >= amount;
    }

    @Override
    public void displayErrorMessage(Player player) {
        player.sendMessage("You do not have the materials to make that.");
    }

    @Override
    public void fulfill(Player player) {
        player.getInventory().remove(new Item(material.getItemId(), amount));
    }

    @Override
    public String toString() {
        return ItemDefinitions.name(material.getItemId()) + ": " + amount;
    }
}
