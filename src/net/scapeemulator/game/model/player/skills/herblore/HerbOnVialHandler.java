package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.dispatcher.item.ItemOnItemHandler;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.skills.MakeItemInterface;
import net.scapeemulator.game.model.player.skills.MakeItemInterface.MakeItemInterfaceListener;

/**
 * @author David Insley
 */
public class HerbOnVialHandler extends ItemOnItemHandler {

    private static final int VIAL_OF_WATER = 227;

    private final HerbloreRecipe recipe;

    public HerbOnVialHandler(Herb herb) {
        super(herb.getCleanId(), VIAL_OF_WATER);
        String message = "You put the " + ItemDefinitions.forId(herb.getCleanId()).getName() + " into the vial of water.";
        recipe = new HerbloreRecipe(herb.getCleanId(), VIAL_OF_WATER, herb.getLevel(), 0.0, herb.getUnfinishedId(), message, null, 1);
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