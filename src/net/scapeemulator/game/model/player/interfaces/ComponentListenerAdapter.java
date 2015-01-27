package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;

/**
 * @author Hadyn Richard
 */
public abstract class ComponentListenerAdapter extends ComponentListener {

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
    }

    @Override
    public void componentClosed(Component component) {
    }

    @Override
    public void componentChanged(Component component, int oldId) {
    }

}
