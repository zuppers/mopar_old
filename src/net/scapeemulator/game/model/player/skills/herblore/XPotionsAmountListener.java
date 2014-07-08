package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.model.player.IntegerScriptInputListener;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class XPotionsAmountListener extends IntegerScriptInputListener {

    private final Player player;
    private final HerbloreRecipe recipe;

    public XPotionsAmountListener(Player player, HerbloreRecipe recipe) {
        this.player = player;
        this.recipe = recipe;
    }

    @Override
    public void inputReceived(int value) {
        player.startAction(new HerbloreAction(player, recipe, value));
        player.getScriptInput().reset();
    }

}
