package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;
import net.scapeemulator.game.model.player.skills.Skill;

/**
 * @author David Insley
 */
public class RunecraftingAction extends ReachDistancedAction {

    private final RCRune runeType;
    private final GroundObject object;

    public RunecraftingAction(Player player, RCRune runeType, GroundObject object) {
        super(1, false, player, object.getBounds(), 1);
        this.runeType = runeType;
        this.object = object;
    }

    @Override
    public void executeAction() {
        if (!mob.notWalking()) {
            return;
        }

        mob.turnToPosition(object.getCenterPosition());

        if (!runeType.getRequirements().hasRequirementsDisplayOne(mob)) {
            stop();
            return;
        }

        Item removed = null;

        // If pure ess isn't required, check for rune ess
        if (!runeType.pureReq()) {
            removed = mob.getInventory().remove(new Item(Runecrafting.RUNE_ESS, 28));
        }

        // If we couldn't use rune ess (either because it isn't allowed or we
        // didnt have it), check for pure ess
        if (removed == null) {
            removed = mob.getInventory().remove(new Item(Runecrafting.PURE_ESS, 28));
        }

        if (removed == null) {
            mob.sendMessage("You don't have enough essence to craft any runes.");
            stop();
            return;
        }

        int amount = removed.getAmount();
        Item runes = new Item(runeType.getRune().getItemId(), amount * runeType.getMultiplier(mob.getSkillSet().getCurrentLevel(Skill.RUNECRAFTING)));
        mob.sendMessage("You bind the altars power into some " + runes.getDefinition().getName().toLowerCase() + "s.");
        mob.getSkillSet().addExperience(Skill.RUNECRAFTING, runeType.getXP() * amount);
        mob.getInventory().add(runes);
        mob.playAnimation(Runecrafting.CRAFT_ANIMATION);
        mob.playSpotAnimation(Runecrafting.CRAFT_GFX);
        stop();
    }

}
