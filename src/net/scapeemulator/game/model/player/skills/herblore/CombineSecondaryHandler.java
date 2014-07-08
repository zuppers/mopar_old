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
public class CombineSecondaryHandler extends ItemOnItemHandler {

    private static final Animation MIXING_ANIMATION = new Animation(363);

    private final HerbloreRecipe recipe;

    public CombineSecondaryHandler(Potion potion) {
        super(potion.getUnfinishedId(), potion.getSecondary());
        String message = "You mix the " + ItemDefinitions.forId(potion.getSecondary()).getName().toLowerCase() + " into your potion.";
        recipe = new HerbloreRecipe(potion.getUnfinishedId(), potion.getSecondary(), potion.getLevel(), potion.getXp(), potion.getPotionId(), message, MIXING_ANIMATION, 3);
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