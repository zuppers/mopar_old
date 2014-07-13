package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;

public class EquipmentRequirement extends Requirement {

    private final int slot;
    private final int[] validEquipment;
    private final String error;

    public EquipmentRequirement(int slot, int... validEquipment) {
        this(slot, null, validEquipment);
    }

    public EquipmentRequirement(int slot, String error, int... validEquipment) {
        this.slot = slot;
        this.validEquipment = validEquipment;
        this.error = error;
    }

    @Override
    public boolean hasRequirement(Player player) {
        Item equipped = player.getEquipment().get(slot);
        if (equipped != null) {
            for (int validEquipped : validEquipment) {
                if (equipped.getId() == validEquipped) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void displayErrorMessage(Player player) {
        if (error != null) {
            player.sendMessage(error);
        }
    }

    @Override
    public void fulfill(Player player) {
    }

}
