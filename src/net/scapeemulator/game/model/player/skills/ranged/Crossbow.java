package net.scapeemulator.game.model.player.skills.ranged;

import static net.scapeemulator.game.model.player.skills.ranged.Bolt.*;

public enum Crossbow {

	CROSSBOW(837, OPAL_E),
	BRONZE(9174, OPAL_E),
	BLURITE(9176, JADE_E),
	IRON(9177, PEARL_E),
	DORGESHUUN(8880, PEARL_E),
	STEEL(9179, TOPAZ_E),
	BLACK_CB(13081, BLACK),
	MITHRIL(9181, EMERALD_E),
	ADAMANT(9183, DIAMOND_E),
	RUNE(9185, ONYX_E),
	KARILS(4734, BOLT_RACK);
	
	private final int itemId;
	private final Bolt maxBolt;
	
	private Crossbow(int itemId, Bolt maxBolt) {
		this.itemId = itemId;
		this.maxBolt = maxBolt;
	}
	
	public static Crossbow forId(int itemId) {
		if(itemId >= 4934 && itemId <= 4937) {
			return KARILS;
		}
		for(Crossbow crossbow : values()) {
			if(crossbow.itemId == itemId) {
				return crossbow;
			}
		}
		return null;
	}
	
	public boolean validAmmo(Bolt bolt) {
		if(bolt == null) {
			return false;
		}
		if(this == KARILS) {
			return bolt == BOLT_RACK;
		}
		return bolt.ordinal() <= maxBolt.ordinal();
	}
}
