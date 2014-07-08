package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.item.ItemOnItemHandler;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemMessage;

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
        recipe = new HerbloreRecipe(secondary.getUngroundId(), true, PESTLE_AND_MORTAR, false, 0, 0.0, secondary.getItemId(), message, GRINDING_ANIMATION, 2);
    }

    @Override
    public void handle(Player player, Inventory inventoryOne, Inventory inventoryTwo, SlottedItem itemOne, SlottedItem itemTwo) {
        if (inventoryOne != player.getInventory() || inventoryTwo != player.getInventory()) {
            return;
        }
        if (!recipe.getRequirements().hasRequirementsDisplayOne(player)) {
            return;
        }
        SlottedItem secondary = itemOne.getItem().getId() == PESTLE_AND_MORTAR ? itemTwo : itemOne;
        if (player.getInventory().getAmount(secondary.getItem().getId()) < 2) {
            player.startAction(new HerbloreAction(player, recipe, 1));
            return;
        }
        player.send(new InterfaceItemMessage(309, 2, 200, recipe.getProduct()));
        player.setInterfaceText(309, 6, "<br><br><br><br><br>" + ItemDefinitions.forId(recipe.getProduct()).getName());
        player.getInterfaceSet().openChatbox(309);
        player.getInterfaceSet().getChatbox().setListener(new HerbloreInterfaceListener(player, recipe));
    }

}