package net.scapeemulator.game.model.npc;

import java.util.ArrayList;
import java.util.Random;

import net.scapeemulator.game.model.player.Item;

public class NPCDropTable {

	private static final Random RANDOM = new Random();

	public enum TableType {
		
		ALWAYS(-1.0), SPECIAL(-1.0), COMMON(1.0), UNCOMMON(0.15), RARE(0.03), VERY_RARE(0.005);
		
		private double defaultChance;
		
		private TableType(double chance) {
			this.defaultChance = chance;
		}
		
	}

	private DropTable[] tables;

	public NPCDropTable() {
		tables = new DropTable[6];
		for (TableType type : TableType.values()) {
			tables[type.ordinal()] = new DropTable(type.defaultChance);
		}
		addItem(TableType.ALWAYS, 526);
		addItem(TableType.COMMON, 995, "5,6,7,10-20");
	}

	public void addItem(TableType type, int itemId, String amount) {
		tables[type.ordinal()].addItem(new DropTableItem(itemId, amount));
	}

	public void addItem(TableType type, int itemId, int amount) {
		tables[type.ordinal()].addItem(new DropTableItem(itemId, amount));
	}

	public void addItem(TableType type, int itemId) {
		addItem(type, itemId, 1);
	}
	
	public void setChance(TableType type, double chance) {
		if (type == TableType.ALWAYS) {
			throw new IllegalArgumentException("Cannot modify always drop chance");
		}
		tables[type.ordinal()].chance = chance;
	}

	public ArrayList<Item> getRandomDrops() {
		ArrayList<Item> items = new ArrayList<Item>();

		tables[TableType.ALWAYS.ordinal()].addAll(items);

		double roll = RANDOM.nextDouble();
		DropTable randomTable = null;
		for (int i = TableType.COMMON.ordinal(); i <= TableType.VERY_RARE.ordinal(); i++) {
			if (tables[i].chance < roll || tables[i].items.isEmpty()) {
				break;
			}
			randomTable = tables[i];
		}
		if (randomTable != null) {
			randomTable.addRandom(items);
		}

		DropTable specialDrop = tables[TableType.SPECIAL.ordinal()];
		double specialRoll = RANDOM.nextDouble();
		if (specialRoll < specialDrop.chance) {
			specialDrop.addRandom(items);
		}
		return items;
	}

	private class DropTable {

		private double chance;
		private ArrayList<DropTableItem> items = new ArrayList<DropTableItem>();

		private DropTable(double chance) {
			this.chance = chance;
		}

		private void addItem(DropTableItem item) {
			items.add(item);
		}

		private void addAll(ArrayList<Item> list) {
			for (DropTableItem dti : items) {
				if (dti.id < 1) {
					continue;
				}
				int amt = dti.getAmount();
				if (amt > 0) {
					list.add(new Item(dti.id, amt));
				}
			}
		}

		private void addRandom(ArrayList<Item> list) {
			if (items.isEmpty()) {
				return;
			}
			DropTableItem dti = items.get(RANDOM.nextInt(items.size()));
			if (dti.id < 1) {
				return;
			}
			int amt = dti.getAmount();
			if (amt > 0) {
				list.add(new Item(dti.id, amt));
			}
		}
	}

	private class DropTableItem {

		private final int id;
		private int staticAmount;
		private String specialAmount;

		private DropTableItem(int id, int amount) {
			this.id = id;
			staticAmount = amount;
		}

		private DropTableItem(int id, String amount) {
			amount = amount.replaceAll("\\s", "");
			this.id = id;
			try {
				staticAmount = Integer.parseInt(amount);
			} catch (NumberFormatException e) {
				specialAmount = amount;
			}
		}

		private int getAmount() {
			if (staticAmount > 0) {
				return staticAmount;
			} else {
				return parseSpecialAmount();
			}
		}

		// format: amt1,amt2,amt3min-amt3max,amt4
		private int parseSpecialAmount() {
			String[] amounts = specialAmount.split(",");
			if (amounts.length < 1) {
				return 0;
			}
			amounts = amounts[RANDOM.nextInt(amounts.length)].split("-");
			if (amounts.length == 1) {
				return Integer.parseInt(amounts[0]);
			} else if (amounts.length == 2) {
				int min = Integer.parseInt(amounts[0]);
				int max = Integer.parseInt(amounts[1]);
				if (max < min) {
					return 0;
				}
				return min + RANDOM.nextInt((max - min) + 1);
			} else {
				return 0;
			}

		}

	}
}
