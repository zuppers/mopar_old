package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.definition.ItemDefinitions;

public enum Rune {
	
    AIR(556, 1381, 1397, 1405),
    WATER(555, 1383, 1395, 1403, 6562, 11736, 6563, 11738),
    EARTH(557, 1385, 1399, 1407, 3053, 6562, 3054, 6563),
    FIRE(554, 1387, 1393, 1401, 3053, 11736, 3054, 11738),
    MIND(558),
    CHAOS(562),
    DEATH(560),
    BLOOD(565),
    SOUL(566),
    BODY(559),
    COSMIC(564),
    LAW(563),
    NATURE(561),
    ASTRAL(9075);
	
	private final int itemId;
	private final int[] validStaves;
	private final String name;
	
	private Rune(int itemId, int ... validStaves) {
		this.itemId = itemId;
		this.validStaves = validStaves;
		name = ItemDefinitions.forId(itemId).getName();
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public int[] getValidStaves() {
		return validStaves;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
