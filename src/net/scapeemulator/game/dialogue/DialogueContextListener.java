package net.scapeemulator.game.dialogue;

import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class DialogueContextListener extends ComponentListener {

    private final DialogueContext context;

    public DialogueContextListener(DialogueContext context) {
        this.context = context;
    }

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
        context.handleInput(component.getCurrentId(), componentId);
    }

    @Override
    public void componentClosed(Component component) {
        context.stop();
    }

    @Override
    public boolean componentChanged(Component component, int oldId) {
        return context.getInterfaceId() == component.getCurrentId();
    }
}