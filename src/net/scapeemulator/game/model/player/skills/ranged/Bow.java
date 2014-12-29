package net.scapeemulator.game.model.player.skills.ranged;

import static net.scapeemulator.game.model.player.skills.ranged.Arrow.*;

public enum Bow {

	SHORTBOW(841, IRON_PPP),
	LONGBOW(839, IRON_PPP),
	OAK_SHORTBOW(843, STEEL_PPP),
	OAK_LONGBOW(845, STEEL_PPP),
	WILLOW_SHORTBOW(849, MITHRIL_PPP),
	WILLOW_LONGBOW(847, MITHRIL_PPP),
	MAPLE_SHORTBOW(853, ADAMANT_PPP),
	MAPLE_LONGBOW(851, ADAMANT_PPP),
	YEW_SHORTBOW(857, RUNE_PPP),
	YEW_LONGBOW(855, RUNE_PPP),
	MAGIC_SHORTBOW(861, RUNE_PPP),
	MAGIC_LONGBOW(859, RUNE_PPP),
	SEERCUL(6724, RUNE_PPP),
	DARK_BOW(11235, DRAGON_PPP),
	CRYSTAL_BOW(4212, null);
	
	// 0 = normal bow, 1 = crossbow, 2 = karils, 3 = crystal bow, 4 = obsidian ring, 5 = dark bow
	
	private final int itemId;
	private final Arrow maxArrow;
	
	private Bow(int itemId, Arrow maxArrow) {
		this.itemId = itemId;
		this.maxArrow = maxArrow;
	}
	
	public int getBowId() {
	    return itemId;
	}
	
	public static Bow forId(int itemId) {
		if(itemId >= 4214 && itemId <= 4223) {
			return CRYSTAL_BOW;
		}
		for(Bow bow : values()) {
			if(bow.itemId == itemId) {
				return bow;
			}
		}
		return null;
	}
	
	public boolean validAmmo(Arrow arrow) {
		if(this == CRYSTAL_BOW) {
			return true;
		}
		if(arrow == null) {
			return false;
		}
		return arrow.ordinal() <= maxArrow.ordinal();
	}
	
}
