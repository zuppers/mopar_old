package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.dispatcher.item.ItemOnItemHandler;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.skills.MakeItemInterface;
import net.scapeemulator.game.model.player.skills.MakeItemInterface.MakeItemInterfaceListener;

/**
 * @author David Insley
 */
public class CombineSecondaryHandler extends ItemOnItemHandler {

    private static final Animation MIXING_ANIMATION = new Animation(363);

    private final HerbloreRecipe recipe;

    public CombineSecondaryHandler(Potion potion) {
        super(potion.getUnfinishedId(), potion.getSecondary());
        String message = "You mix the " + ItemDefinitions.forId(potion.getSecondary()).getName().toLowerCase() + " into your potion.";
        recipe = new HerbloreRecipe(potion.getUnfinishedId(), potion.getSecondary(), potion.getLevel(), potion.getXp(), potion.getPotionId(),
                message, MIXING_ANIMATION, 3);
    }

    @Override
    public void handle(Player player, SlottedItem itemOne, SlottedItem itemTwo) {
        if (!recipe.getRequirements().hasRequirementsDisplayOne(player)) {
            return;
        }
        if (player.getInventory().getAmount(itemOne.getItem().getId()) < 2 || player.getInventory().getAmount(itemTwo.getItem().getId()) < 2) {
            player.startAction(new HerbloreAction(player, recipe, 1));
            return;
        }
        MakeItemInterface.showMakeItemInterface(player, new MakeItemInterfaceListener() {
            @Override
            public void makeAllSelected() {
                makeAmountSelected(28);
            }

            @Override
            public void makeAmountSelected(int amount) {
                player.startAction(new HerbloreAction(player, recipe, amount));
            }

            @Override
            public void cancelled() {
            }

        }, new Item(recipe.getProduct()), false);
    }

}