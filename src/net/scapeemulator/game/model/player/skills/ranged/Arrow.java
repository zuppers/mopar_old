/**
 * 
 */
package net.scapeemulator.game.model.player.skills.ranged;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.CreateProjectileMessage;
import net.scapeemulator.game.msg.impl.PlacementCoordsMessage;

/**
 * @author David
 *
 */
public enum Arrow {
	//ICE(0, 25, 1110, unknown)
	BRONZE(882, 19, 1104, 10),
	BRONZE_P(883, 19, 1104, 10),
	BRONZE_PP(5616, 19, 1104, 10),
	BRONZE_PPP(5622, 19, 1104, 10),
	
	IRON(884, 18, 1105, 11),
	IRON_P(885, 18, 1105, 11),
	IRON_PP(5617, 18, 1105, 11),
	IRON_PPP(5623, 18, 1105, 11),
	
	STEEL(886, 20, 1106, 12),
	STEEL_P(887, 20, 1106, 12),
	STEEL_PP(5618, 20, 1106, 12),
	STEEL_PPP(5624, 20, 1106, 12),
	
	MITHRIL(888, 21, 1107, 13),
	MITHRIL_P(889, 21, 1107, 13),
	MITHRIL_PP(5619, 21, 1107, 13),
	MITHRIL_PPP(5625, 21, 1107, 13),
	
	ADAMANT(890, 22, 1108, 14),
	ADAMANT_P(891, 22, 1108, 14),
	ADAMANT_PP(5620, 22, 1108, 14),
	ADAMANT_PPP(5626, 22, 1108, 14),
	
	RUNE(892, 24, 1109, 15),
	RUNE_P(893, 24, 1109, 15),
	RUNE_PP(5621, 24, 1109, 15),
	RUNE_PPP(5627, 24, 1109, 15),
	
	DRAGON(11212, -1, 1111, 1121),
	DRAGON_P(11227, -1, 1111, 1121),
	DRAGON_PP(11228, -1, 1111, 1121),
	DRAGON_PPP(11229, -1, 1111, 1121),
	
	CRYSTAL(-1, 250, -1, 249);
	
	private final int itemId;
	private final int drawbackGraphic;
	private final int doubleDrawbackGraphic;
	private final int projectileGraphic;
	
	private Arrow(int itemId, int drawbackGraphic, int doubleDrawbackGraphic, int projectileGraphic) {
		this.itemId = itemId;
		this.drawbackGraphic = drawbackGraphic;
		this.doubleDrawbackGraphic = doubleDrawbackGraphic;
		this.projectileGraphic = projectileGraphic;
	}
	
	public int getArrowId() {
	    return itemId;
	}
	
	public static Arrow forId(int itemId) {
		for(Arrow arrow : values()) {
			if(arrow.itemId == itemId) {
				return arrow;
			}
		}
		return null;
	}
	
	public SpotAnimation getDrawbackGraphic(boolean dub) {
		return new SpotAnimation(dub ? doubleDrawbackGraphic : drawbackGraphic, 0, 90);
	}
	
	private final static int[] SPEEDS = {60, 60, 60, 63, 65, 67, 69, 69, 71, 73, 73};
	
	public void createProjectile(Mob source, Mob target, boolean dub) {
		Position start = source.getPosition();
		Position destination = target.getPosition();
		int distance = start.getDistance(destination);
		
		for (Player p : World.getWorld().getPlayers()) {
			if (!p.getPosition().isWithinScene(source.getPosition())) {
				continue;
			}
			int localX = start.getX() - p.getPosition().getBaseLocalX(p.getLastKnownRegion().getX() >> 3) - 3;
			int localY = start.getY() - p.getPosition().getBaseLocalY(p.getLastKnownRegion().getY() >> 3) - 2;
			p.send(new PlacementCoordsMessage(localX, localY));
			p.send(new CreateProjectileMessage(start, destination, target, projectileGraphic, 31, distance < 2 ? 34 : 39, 50, SPEEDS[distance] + (dub ? 10 : 0)));
		}
	}
}
