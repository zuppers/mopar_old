package net.scapeemulator.game.model.player.skills.construction.furniture;

import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;

/**
 * @author David Insley
 */
public class MaterialRequirement extends ItemRequirement {

    private final Material material;
    private final int amount;

    public MaterialRequirement(Material material) {
        this(material, 1);
    }

    public MaterialRequirement(Material material, int amount) {
        super(material.getItemId(), amount, true, "You do not have the materials to make that.");
        this.material = material;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return ItemDefinitions.name(material.getItemId()) + ": " + amount;
    }
}
