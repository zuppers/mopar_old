package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.ScriptInputListenerAdapter;

/**
 * @author David Insley
 */
public class XPotionsAmountListener extends ScriptInputListenerAdapter {

    private final Player player;
    private final HerbloreRecipe recipe;

    public XPotionsAmountListener(Player player, HerbloreRecipe recipe) {
        this.player = player;
        this.recipe = recipe;
    }

    @Override
    public void intInputReceived(int value) {
        player.startAction(new HerbloreAction(player, recipe, value));
        player.getScriptInput().reset();
    }

}
