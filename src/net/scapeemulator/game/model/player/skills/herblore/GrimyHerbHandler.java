package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.dispatcher.item.ItemHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class GrimyHerbHandler extends ItemHandler {

    public GrimyHerbHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, Inventory inventory, SlottedItem item, String optionName, HandlerContext context) {
        Herb herb = Herb.forGrimyId(item.getItem().getId());
        if (herb != null) {
            context.stop();
            if (player.getSkillSet().getCurrentLevel(Skill.HERBLORE) < herb.getLevel()) {
                player.sendMessage("You need level " + herb.getLevel() + " Herblore to clean that herb.");
                return;
            }
            player.getInventory().remove(item);
            player.getInventory().add(new Item(herb.getCleanId()));
            player.getSkillSet().addExperience(Skill.HERBLORE, herb.getXp());
            player.sendMessage("You clean dirt from the " + ItemDefinitions.forId(item.getItem().getId()).getName().toLowerCase() + ".");
        }

    }

}