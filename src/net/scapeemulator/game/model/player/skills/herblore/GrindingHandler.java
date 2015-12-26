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
public class GrindingHandler extends ItemOnItemHandler {

    private static final int PESTLE_AND_MORTAR = 233;
    private static final Animation GRINDING_ANIMATION = new Animation(364);

    private final HerbloreRecipe recipe;

    public GrindingHandler(Secondary secondary) {
        super(secondary.getUngroundId(), PESTLE_AND_MORTAR);
        String message = "You grind the " + ItemDefinitions.forId(secondary.getUngroundId()).getName().toLowerCase() + " to dust.";
        recipe = new HerbloreRecipe(secondary.getUngroundId(), true, PESTLE_AND_MORTAR, false, 0, 0.0, secondary.getItemId(), message,
                GRINDING_ANIMATION, 2);
    }

    @Override
    public void handle(Player player, SlottedItem itemOne, SlottedItem itemTwo) {
        if (!recipe.getRequirements().hasRequirementsDisplayOne(player)) {
            return;
        }
        SlottedItem secondary = itemOne.getItem().getId() == PESTLE_AND_MORTAR ? itemTwo : itemOne;
        if (player.getInventory().getAmount(secondary.getItem().getId()) < 2) {
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