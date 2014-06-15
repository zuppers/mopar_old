/**
 * 
 */
package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.CreateProjectileMessage;
import net.scapeemulator.game.msg.impl.PlacementCoordsMessage;

/**
 * @author David
 * 
 */
public class CombatSpell extends Spell {

	private String name;
	private int autoCastConfig;
	public static SpotAnimation SPLASH_GRAPHIC = new SpotAnimation(85, 0, 100);
	private int projectileGraphic;
	private int projectileStartHeight;
	private int projectileEndHeight;
	private int projectileDelay;
	private SpotAnimation explosionGraphic;
	private int maxHit;

	public CombatSpell(String name, int autoCastConfig, double xp, int maxHit, int animation, int graphic) {
		super(SpellType.COMBAT, xp, new Animation(animation), new SpotAnimation(graphic, 0, 100)); 
		// TODO anim height should be 0 for teleblock and miasmic spells
		this.name = name;
		this.autoCastConfig = autoCastConfig;
		this.maxHit = maxHit;
	}

	public void cast(Mob caster, Mob target) {
		Position source = caster.getPosition();
		Position destination = target.getPosition();
		caster.playAnimation(animation);
		caster.playSpotAnimation(graphic);
		for (Player p : World.getWorld().getPlayers()) {
			if (!p.getPosition().isWithinScene(caster.getPosition())) {
				continue;
			}
			int localX = source.getX() - p.getPosition().getBaseLocalX(p.getLastKnownRegion().getX() >> 3) - 3;
			int localY = source.getY() - p.getPosition().getBaseLocalY(p.getLastKnownRegion().getY() >> 3) - 2;
			p.send(new PlacementCoordsMessage(localX, localY));
			p.send(new CreateProjectileMessage(source, destination, target, projectileGraphic, projectileStartHeight, projectileEndHeight, projectileDelay, 100));
		}
	}

	public void setProjectileInformation(int projectileGraphic, int explosionGraphic, int projectileStartHeight, int projectileEndHeight, int projectileDelay) {
		this.projectileGraphic = projectileGraphic;
		this.explosionGraphic = new SpotAnimation(explosionGraphic, 0, 100);
		this.projectileStartHeight = projectileStartHeight;
		this.projectileEndHeight = projectileEndHeight;
		this.projectileDelay = projectileDelay;
	}

	public String getName() {
		return name;
	}

	public int getAutoCastConfig() {
		return autoCastConfig;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public int getProjectileGraphic() {
		return projectileGraphic;
	}

	public SpotAnimation getExplosionGraphic() {
		return explosionGraphic;
	}

}
