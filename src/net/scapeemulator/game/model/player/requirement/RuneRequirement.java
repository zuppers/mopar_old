/**
 * 
 */
package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.magic.Rune;

/**
 * @author David
 * 
 */
public class RuneRequirement extends Requirement {

	private final Rune rune;
	private final int amount;
	private final String error;

	// TODO combination runes

	/**
	 * A Rune requirement that takes into account infinite supply staves. If you only want to check
	 * the inventory, just use inventory.getAmount
	 * 
	 * @param rune rune
	 * @param amount number of runes required
	 */
	public RuneRequirement(Rune rune, int amount) {
		this.rune = rune;
		this.amount = amount;
		error = "You do not have enough " + rune + "s to cast that spell.";
	}

	@Override
	public boolean hasRequirement(Player player) {
		Item staff = player.getEquipment().get(Equipment.WEAPON);
		if (staff != null) {
			for (int validStaff : rune.getValidStaves()) {
				if (staff.getId() == validStaff) {
					return true;
				}
			}
		}
		if (player.getInventory().getAmount(rune.getItemId()) >= amount) {
			return true;
		}
		return false;
	}

	@Override
	public void displayErrorMessage(Player player) {
		player.sendMessage(error);
	}

	@Override
	public void fulfill(Player player) {
		int staff = player.getEquipment().get(Equipment.WEAPON).getId();
		for (int validStaff : rune.getValidStaves()) {
			if (staff == validStaff) {
				return;
			}
		}
		player.getInventory().remove(new Item(rune.getItemId(), amount));
	}

}
