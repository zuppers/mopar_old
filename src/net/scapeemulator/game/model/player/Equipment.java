package net.scapeemulator.game.model.player;

import net.scapeemulator.game.model.player.inventory.Inventory;

public final class Equipment {

	public static final int HEAD = 0;
	public static final int CAPE = 1;
	public static final int NECK = 2;
	public static final int WEAPON = 3;
	public static final int BODY = 4;
	public static final int SHIELD = 5;
	public static final int LEGS = 7;
	public static final int HANDS = 9;
	public static final int FEET = 10;
	public static final int RING = 12;
	public static final int AMMO = 13;

	public static final int[] TIARAS = { 5527, 5529, 5531, 5535, 5537, 5533, 5539, 5543, 5541, 5545, 5547, 5549 };

	public static void remove(Player player, int slot) {
		Inventory inventory = player.getInventory();
		Inventory equipment = player.getEquipment();

		Item item = equipment.get(slot);
		if (item == null)
			return;

		Item remaining = inventory.add(item);
		equipment.set(slot, remaining);

		if (remaining == null) {
			if (slot == WEAPON) {
				player.getPlayerCombatHandler().weaponChanged();
			}
			player.calculateEquipmentBonuses();
		}
	}

	public static void equip(Player player, int slot) {
		Inventory inventory = player.getInventory();
		Inventory equipment = player.getEquipment();
		Item originalWeapon = equipment.get(WEAPON);

		Item item = inventory.get(slot);
		if (item == null)
			return;

		EquipmentDefinition def = item.getEquipmentDefinition();
		if (def == null)
			return;

		if(!def.getRequirements().hasRequirementsDisplayAll(player)) {
			return;
		}
		def.getRequirements().fulfillAll(player);
		
		int targetSlot = def.getSlot();
		boolean unequipShield = def.getSlot() == WEAPON && def.isTwoHanded() && equipment.get(SHIELD) != null;
		boolean unequipWeapon = targetSlot == SHIELD && equipment.get(WEAPON) != null && equipment.get(WEAPON).getEquipmentDefinition().isTwoHanded();
		boolean topUpStack = item.getDefinition().isStackable() && (equipment.get(targetSlot) != null && item.getId() == equipment.get(targetSlot).getId());
		boolean drainStack = equipment.get(targetSlot) != null && equipment.get(targetSlot).getDefinition().isStackable() && inventory.contains(equipment.get(targetSlot).getId());

		if ((unequipShield || unequipWeapon) && inventory.freeSlots() == 0) {
			inventory.fireCapacityExceeded();
			return;
		}

		if (topUpStack) {
			Item remaining = equipment.add(item);
			inventory.set(slot, remaining);
		} else {
			if (drainStack) {
				Item remaining = inventory.add(equipment.get(targetSlot));
				equipment.set(targetSlot, remaining);
				if (remaining != null)
					return;
			}
			inventory.remove(item, slot);

			Item other = equipment.get(targetSlot);
			if (other != null) {
				inventory.add(other);
			}

			equipment.set(targetSlot, item);
			equipmentChanged(player, targetSlot, other, item);
		}

		if (unequipShield) {
			Item remaining = inventory.add(equipment.get(SHIELD));
			equipment.set(SHIELD, remaining);
		} else if (unequipWeapon) {
			Item remaining = inventory.add(equipment.get(WEAPON));
			equipment.set(WEAPON, remaining);
		}

		Item weapon = equipment.get(WEAPON);
		boolean weaponChanged = false;
		if (originalWeapon == null && weapon != null)
			weaponChanged = true;
		else if (weapon == null && originalWeapon != null)
			weaponChanged = true;
		else if (originalWeapon != null && weapon != null && originalWeapon.getId() != weapon.getId())
			weaponChanged = true;

		if (weaponChanged) {
			player.getPlayerCombatHandler().weaponChanged();
		}
		player.calculateEquipmentBonuses();
	}

	private static void equipmentChanged(Player p, int slot, Item oldI, Item newI) {
		if (slot == HEAD) {
			for (int i = 0; i < TIARAS.length; i++) {
				if (oldI != null) {
					if (oldI.getId() == TIARAS[i]) {
						// p.getStateSet().setState((int) Math.pow(2, i), 0);
						p.getStateSet().setBitState(607 + i, 0);
					}
				}
				if (newI.getId() == TIARAS[i]) {
					// p.getStateSet().setState((int) Math.pow(2, i), 1);
					p.getStateSet().setBitState(607 + i, 1);
				}
			}
		}
	}

	public static void showEquipmentInterface(Player player) {
		player.getInterfaceSet().openWindow(667);
		player.sendEquipmentBonuses();
	}
	
	private Equipment() {
		/* empty */
	}

}
