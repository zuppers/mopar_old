package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.util.StringUtils;

/**
 * @author David Insley
 */
public class SkillRequirement extends Requirement {

    private final int skill;
    private final int level;
    private final String error;
    private final boolean current;
    private final double xpReward;

    public SkillRequirement(int skill, int level, boolean current, String concat) {
        this(skill, level, current, concat, 0);
    }

    public SkillRequirement(int skill, int level, boolean current, String concat, double xpReward) {
        this.skill = skill;
        this.level = level;
        this.current = current;
        this.xpReward = xpReward;
        error = "You need level " + level + " " + StringUtils.capitalize(Skill.SKILL_NAMES[skill]) + " to " + concat + ".";
    }

    @Override
    public boolean hasRequirement(Player player) {
        if (current) {
            return player.getSkillSet().getCurrentLevel(skill) >= level;
        } else {
            return player.getSkillSet().getLevel(skill) >= level;
        }
    }

    @Override
    public void displayErrorMessage(Player player) {
        player.sendMessage(error);

    }

    @Override
    public void fulfill(Player player) {
        if (xpReward > 0) {
            player.getSkillSet().addExperience(skill, xpReward);
        }
    }

}
