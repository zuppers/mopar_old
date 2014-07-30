package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;

/**
 * Written by Hadyn Richard
 */
public final class CloseOnInputComponentListener extends ComponentListenerAdapter {

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
        component.removeListener();
        component.reset();
    }
}
