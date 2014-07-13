package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class OneOfRequirement extends Requirement {

    private final Requirement[] requirements;
    private final String error;

    public OneOfRequirement(Requirement... requirements) {
        this(null, requirements);
    }

    public OneOfRequirement(String error, Requirement... requirements) {
        this.error = error;
        this.requirements = requirements;
    }

    @Override
    public boolean hasRequirement(Player player) {
        for (Requirement requirement : requirements) {
            if (requirement.hasRequirement(player)) {
                return true;
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
        for (Requirement requirement : requirements) {
            if (requirement.hasRequirement(player)) {
                requirement.fulfill(player);
                return;
            }
        }
    }

}
