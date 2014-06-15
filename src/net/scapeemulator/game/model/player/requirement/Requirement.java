/**
 * 
 */
package net.scapeemulator.game.model.player.requirement;

import net.scapeemulator.game.model.player.Player;

/**
 * @author David
 *
 */
public abstract class Requirement {

	public abstract boolean hasRequirement(Player player);
	public abstract void displayErrorMessage(Player player);
	public abstract void fulfill(Player player);
	
}
