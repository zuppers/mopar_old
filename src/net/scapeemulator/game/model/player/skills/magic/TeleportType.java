package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.mob.Animation;

/**
 * @author David Insley
 */
enum TeleportType {
	
	STANDARD(3, 8939, 8941, 1576, 1577);
	// ANCIENTS
	// LUNAR

	final int delay;
	final Animation startAnimation;
	final Animation endAnimation;
	final SpotAnimation startGraphic;
	final SpotAnimation endGraphic;

	private TeleportType(int delay, int animStart, int animEnd, int gfxStart, int gfxEnd) {
		this.delay = delay;
		this.startAnimation = new Animation(animStart);
		this.endAnimation = new Animation(animEnd);
		this.startGraphic = new SpotAnimation(gfxStart);
		this.endGraphic = new SpotAnimation(gfxEnd);
	}
}
