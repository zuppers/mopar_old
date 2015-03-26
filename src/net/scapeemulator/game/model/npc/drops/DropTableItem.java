package net.scapeemulator.game.model.npc.drops;

import java.util.Random;

/**
 * @author David Insley
 */
public final class DropTableItem {

    private static final Random RANDOM = new Random();

    private final int id;
    private int staticAmount;
    private String specialAmount;

    public DropTableItem(int id, int amount) {
        this.id = id;
        staticAmount = amount;
    }

    public DropTableItem(int id, String amount) {
        amount = amount.replaceAll("\\s", "");
        this.id = id;
        try {
            staticAmount = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            specialAmount = amount;
        }
    }

    public int getId() {
        return id;
    }

    public String getAmountRaw() {
        return specialAmount != null ? specialAmount : Integer.toString(staticAmount);
    }

    public int getAmount() {
        return specialAmount != null ? parseSpecialAmount() : staticAmount;
    }

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