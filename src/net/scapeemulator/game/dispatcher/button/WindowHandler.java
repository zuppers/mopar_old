package net.scapeemulator.game.dispatcher.button;

import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.Player;

/**
 * Handles all of the buttons on a specific interface when there are too many to bind to individual
 * button handlers.
 * 
 * @author David Insley
 */
public abstract class WindowHandler {

    private final int[] windowIds;

    public WindowHandler(int... windowIds) {
        if (windowIds.length < 1) {
            throw new IllegalArgumentException("Window handler must have at least one window ID");
        }
        this.windowIds = windowIds;
    }

    public int[] getWindowIds() {
        return windowIds;
    }

    /**
     * Handles a component action on one of the handled windows.
     * 
     * @param player the player that sent the action
     * @param child the child id of the component action
     * @param option the option for the window action
     * @param dyn dynamic variable
     * @return true if the button was handled, false if the dispatcher should search for a button
     *         handler for this action
     */
    public abstract boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn);
}
