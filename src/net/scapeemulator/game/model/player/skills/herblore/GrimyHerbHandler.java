package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.item.ItemInteractHandler;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public class GrimyHerbHandler extends ItemInteractHandler {

    private final Herb herb;

    public GrimyHerbHandler(Herb herb) {
        super(herb.getGrimyId());
        this.herb = herb;
    }

    @Override
    public void handle(Player player, SlottedItem item) {
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