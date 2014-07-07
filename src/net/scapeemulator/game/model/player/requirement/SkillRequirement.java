/**
 * 
 */
package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.util.StringUtils;

/**
 * @author David
 *
 */
public class SkillRequirement extends Requirement {

	private final int skill;
	private final int level;
	private final String error;
	private final boolean current;
	
	/**
	 * A skill requirement that looks at the current level instead of the actual level of a player
	 * 
	 * @param skill The skill id
	 * @param level The level 
	 * @param concat Text to append: "You need (a/an) (skill name) level of (level) to (concat)"
	 */
	public SkillRequirement(int skill, int level, String concat) {
		this(skill, level, true, concat);
	}
	
	public SkillRequirement(int skill, int level, boolean current, String concat) {
		this.skill = skill;
		this.level = level;
		this.current = current;
		error =  "You need " + StringUtils.addAOrAn(StringUtils.capitalize(Skill.SKILL_NAMES[skill])) + " level of " + level + " to " + concat + ".";
	}
	
	@Override
	public boolean hasRequirement(Player player) {
		if(current) {
			return player.getSkillSet().getCurrentLevel(skill) >= level;
		} else {
			return player.getSkillSet().getMaximumLevel(skill) >= level;
		}
	}

	@Override
	public void displayErrorMessage(Player player) {
		player.sendMessage(error);
		
	}

	@Override
	public void fulfill(Player player) {}

}
