package net.scapeemulator.game.model.npc.drops;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.scapeemulator.game.model.player.Item;

public final class SubDropTable {

    private static final Random RANDOM = new Random();

    private final double chance;
    private final List<DropTableItem> items = new ArrayList<>();

    public SubDropTable(double chance) {
        this.chance = chance;
    }

    public void addItem(DropTableItem item) {
        items.add(item);
    }

    public void addAll(List<Item> list) {
        for (DropTableItem dti : items) {
            if (dti.getId() < 1) {
                continue;
            }
            int amt = dti.getAmount();
            if (amt > 0) {
                list.add(new Item(dti.getId(), amt));
            }
        }
    }

    public void addRandom(List<Item> list) {
        if (isEmpty()) {
            return;
        }
        DropTableItem dti = items.get(RANDOM.nextInt(items.size()));
        if (dti.getId() < 1) {
            return;
        }
        int amt = dti.getAmount();
        if (amt > 0) {
            list.add(new Item(dti.getId(), amt));
        }
    }

    public List<DropTableItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getChance() {
        return chance;
    }

}