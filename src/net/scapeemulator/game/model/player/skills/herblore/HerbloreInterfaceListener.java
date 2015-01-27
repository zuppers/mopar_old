package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class HerbloreInterfaceListener extends ComponentListener {

    private final Player player;
    private final HerbloreRecipe recipe;

    public HerbloreInterfaceListener(Player player, HerbloreRecipe recipe) {
        this.player = player;
        this.recipe = recipe;
    }

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
        int option = 3 - ((componentId - 3) % 4);
        int amount = 1;
        switch (option) {
        case 0:
            break;
        case 1:
            amount = 5;
            break;
        case 2:
            amount = -1;
            break;
        case 3:
            amount = 28;
            break;
        }
        if (amount != -1) {
            player.getInterfaceSet().getChatbox().reset();
            player.startAction(new HerbloreAction(player, recipe, amount));
        } else {
            componentClosed(null);
            player.getScriptInput().showIntegerScriptInput(new XPotionsAmountListener(player, recipe));
        }

    }

    @Override
    public void componentClosed(Component component) {
        player.getInterfaceSet().getChatbox().removeListener();
        player.getInterfaceSet().getChatbox().reset();
    }

    @Override
    public void componentChanged(Component component, int oldId) {
        componentClosed(component);
    }
}
