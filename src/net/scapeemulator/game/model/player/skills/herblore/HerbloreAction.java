package net.scapeemulator.game.model.player.skills.herblore;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.task.Action;

/**
 * @author David Insley
 */
public class HerbloreAction extends Action<Player> {

    private final HerbloreRecipe recipe;
    private final int amount;

    private boolean started;
    private int count;

    public HerbloreAction(Player player, HerbloreRecipe recipe, int amount) {
        super(player, recipe.getDelay(), true);
        this.recipe = recipe;
        this.amount = amount;
    }

    @Override
    public void execute() {
        if (!started) {
            mob.getWalkingQueue().reset();
        }
        if (!recipe.getRequirements().hasRequirementsDisplayOne(mob)) {
            stop();
            return;
        }
        recipe.getRequirements().fulfillAll(mob);
        mob.getInventory().add(new Item(recipe.getProduct()));
        mob.getSkillSet().addExperience(Skill.HERBLORE, recipe.getXp());
        mob.sendMessage(recipe.getMessage());
        if (recipe.getAnimation() != null) {
            mob.playAnimation(recipe.getAnimation());
        }
        if (++count == amount) {
            stop();
        }
    }

}
