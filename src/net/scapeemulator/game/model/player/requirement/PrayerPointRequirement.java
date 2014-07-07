package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Player;

public class PrayerPointRequirement extends Requirement {

    public static final PrayerPointRequirement NON_ZERO_POINTS = new PrayerPointRequirement(1);
    
    private final int amount;
    private final boolean remove;
    private final String message;
    
    public PrayerPointRequirement(int amount) {
        this(amount, false);
    }

    public PrayerPointRequirement(int amount, boolean remove) {
        this(amount, remove, null);
    }
    
    public PrayerPointRequirement(int amount, boolean remove, String message) {
        this.amount = amount;
        this.remove = remove;
        this.message = message;
    }
    
    @Override
    public boolean hasRequirement(Player player) {
        return player.getPrayerPoints() >= amount;
    }

    @Override
    public void displayErrorMessage(Player player) {
        if(message != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public void fulfill(Player player) {
        if (remove) {
            player.reducePrayerPoints(amount);
        }
    }

}
