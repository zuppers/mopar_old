package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class CombatTabHandler extends WindowHandler {

    public CombatTabHandler() {
        super(75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93);
    }

    @Override
    public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
        player.getPlayerCombatHandler().attackTabClick(windowId, child);
        return true;
    }

}
