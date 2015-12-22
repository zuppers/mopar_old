package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.ConfigMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;

public class AutoCastHandler {

	// We assume our combat handler will correctly only call this method if they have a staff equipped 
	public static void openSpellSelection(Player player) {
		int weapon = player.getEquipment().get(Equipment.WEAPON).getId();
		if(player.getSpellbook() == Spellbook.ANCIENT_SPELLBOOK) {
			if(weapon != 4675) {
				player.sendMessage("You must equip the Ancient Staff to autocast while using Ancient Magicks.");
				return;
			}
		} else if(player.getSpellbook() == Spellbook.LUNAR_SPELLBOOK) {
			player.sendMessage("You cannot autocast while on Lunar Magic.");
			return;
		} else {
			if(weapon == 4170) { // Slayer staff
				player.getInterfaceSet().openAttackTab(AutoCastWindow.SLAYER_STAFF.tabId);
			} else if(weapon == 8841) {// Void mace
				player.getInterfaceSet().openAttackTab(AutoCastWindow.VOID_MACE.tabId);
			} else {
				player.getInterfaceSet().openAttackTab(AutoCastWindow.DEFAULT.tabId);
			}
		}
	}

	public static void handleSpellSelection(Player player, int widget, int child) {
		if(player.getInterfaceSet().getAttackTab().getCurrentId() != widget) {
			return;
		}
		for(AutoCastWindow acw : AutoCastWindow.values()) {
			if(acw.tabId == widget) {
				if(child >= acw.spells.length) {
					player.getPlayerCombatHandler().weaponChanged();
					return;
				} else {
					player.getPlayerCombatHandler().restoreTab();
					DamageSpell old = player.getCombatHandler().getAutoCast();
					if(old != null) {
						player.send(new InterfaceVisibleMessage(90, old.getAutoCastConfig(), false));
					}
					DamageSpell spell = acw.spells[child];
					if(spell == null) {
						return;
					}
					player.setInterfaceText(90, 11, spell.getName());
					player.send(new InterfaceVisibleMessage(90, 83, false));
					player.send(new InterfaceVisibleMessage(90, spell.getAutoCastConfig(), true));
					player.send(new ConfigMessage(43, 3));
					player.getCombatHandler().setAutoCast(spell);
					return;
				}
			}
		}
	}
	
	private enum AutoCastWindow {

		SLAYER_STAFF(310, Spellbook.NORMAL_SPELLBOOK, 22, 31, 45, 48, 52, 55), 
		VOID_MACE(406, Spellbook.NORMAL_SPELLBOOK, 22, 42, 45, 48, 52, 55), 
		ANCIENT_STAFF(388, Spellbook.ANCIENT_SPELLBOOK), // TODO copy all directly
		DEFAULT(319, Spellbook.NORMAL_SPELLBOOK, 1, 4, 6, 8, 10, 14, 17, 20, 24, 27, 33, 38, 45, 48, 52, 55);

		private final DamageSpell[] spells;
		private final int tabId;
		
		private AutoCastWindow(int tabId, Spellbook book, int... spellIndices) {
			this.tabId = tabId;
			spells = new DamageSpell[spellIndices.length];
			for (int i = 0; i < spellIndices.length; i++) {
				spells[i] = (DamageSpell) book.getSpell(spellIndices[i]);
			}
		}

	}
}
