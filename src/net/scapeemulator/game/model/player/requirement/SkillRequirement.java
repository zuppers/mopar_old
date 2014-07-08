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

    public SkillRequirement(int skill, int level, boolean current, String concat) {
        this.skill = skill;
        this.level = level;
        this.current = current;
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
    }

}
