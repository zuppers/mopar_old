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

    /**
     * The default operation of the componentChanged method will call the componentClosed method with the same argument.
     */
    @Override
    public boolean componentChanged(Component component, int oldId) {
        return false;
    }

}
