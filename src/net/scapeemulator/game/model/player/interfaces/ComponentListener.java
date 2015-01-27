package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;

/**
 * @author Hadyn Richard
 */
public abstract class ComponentListener {

    public abstract void inputPressed(Component component, int componentId, int dynamicId);

    public abstract void componentClosed(Component component);

    public abstract void componentChanged(Component component, int oldId);
}
