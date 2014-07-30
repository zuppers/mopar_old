package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;

/**
 * Written by Hadyn Richard
 */
public abstract class ComponentListenerAdapter extends ComponentListener {

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {}

    @Override
    public void componentClosed(Component component) {}

}
