package net.scapeemulator.game.model.player.skills;

import net.scapeemulator.game.model.player.Player;

public final class SkillAppearanceListener implements SkillListener {

	private final Player player;

	public SkillAppearanceListener(Player player) {
		this.player = player;
	}

	@Override
	public void skillChanged(SkillSet set, int skill) {
		/* empty */
	}

    @Override
    public void combatLevelledUp(SkillSet set, int combat) {}

    @Override
	public void skillLevelledUp(SkillSet set, int amount, int skill) {
		player.setAppearance(player.getAppearance());
	}
}
