package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public abstract class ComponentListener {

    public abstract void inputPressed(Component component, int componentId, int dynamicId);

    /**
     * Alerts that the component has been closed in the interface set. The listener is removed from the component after this alert resolves.
     * 
     * @param component the component that was closed
     */
    public abstract void componentClosed(Component component);

    /**
     * Alerts that the component interface id has been changed in the interface set. This method should return false if the listener is no longer
     * relevant to the new id.
     * 
     * @param component the component that had its id changed
     * @param oldId the id the component was at before being changed
     * @return whether or not this listener should stay bound to the component
     */
    public abstract boolean componentChanged(Component component, int oldId);
}
