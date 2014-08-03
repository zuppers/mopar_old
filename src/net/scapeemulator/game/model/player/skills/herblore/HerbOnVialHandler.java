package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.dispatcher.item.ItemOnItemHandler;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemMessage;

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
    public void handle(Player player, Inventory inventoryOne, Inventory inventoryTwo, SlottedItem itemOne, SlottedItem itemTwo) {
        if (inventoryOne != player.getInventory() || inventoryTwo != player.getInventory()) {
            return;
        }
        if (!recipe.getRequirements().hasRequirementsDisplayOne(player)) {
            return;
        }
        if (player.getInventory().getAmount(itemOne.getItem().getId()) < 2 || player.getInventory().getAmount(itemTwo.getItem().getId()) < 2) {
            player.startAction(new HerbloreAction(player, recipe, 1));
            return;
        }
        player.send(new InterfaceItemMessage(309, 2, 200, recipe.getProduct()));
        player.setInterfaceText(309, 6, "<br><br><br><br><br>" + ItemDefinitions.forId(recipe.getProduct()).getName());
        player.getInterfaceSet().openChatbox(309);
        player.getInterfaceSet().getChatbox().setListener(new HerbloreInterfaceListener(player, recipe));
    }

}