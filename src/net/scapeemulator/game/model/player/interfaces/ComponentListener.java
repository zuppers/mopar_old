package net.scapeemulator.game.model.player.interfaces;

import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;


/**
 * Written by Hadyn Richard
 */
public abstract class ComponentListener {
    
    public abstract void inputPressed(Component component, int componentId, int dynamicId);

    public abstract void componentClosed(Component component);
}
