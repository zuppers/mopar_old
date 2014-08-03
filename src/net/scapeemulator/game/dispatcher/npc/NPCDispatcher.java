package net.scapeemulator.game.dispatcher.npc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.mob.action.CombatAction;
import net.scapeemulator.game.model.mob.action.InitiateCombatAction;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;
import net.scapeemulator.game.model.player.skills.magic.Spell;
import net.scapeemulator.game.model.player.skills.magic.Spellbook;
import net.scapeemulator.game.util.HandlerContext;

/**
 * Written by Hadyn Richard
 */
public class NPCDispatcher {

	/**
	 * The mapping for all of the handler lists.
	 */
	private Map<Option, List<NPCHandler>> handlerLists = new HashMap<>();

	/**
	 * Constructs a new {@link NPCDispatcher};
	 */
	public NPCDispatcher() {
		for (Option option : Option.values()) {
			if (option.equals(Option.ALL)) {
				continue;
			}
			handlerLists.put(option, new LinkedList<NPCHandler>());
		}
	}

	/**
	 * Binds a handler to this dispatcher.
	 * 
	 * @param handler The handler to bind.
	 */
	public void bind(NPCHandler handler) {
		if (handler.getOption().equals(Option.ALL)) {
			for (Entry<Option, List<NPCHandler>> entry : handlerLists.entrySet()) {
				entry.getValue().add(handler);
			}
		} else {
			List<NPCHandler> list = handlerLists.get(handler.getOption());
			list.add(handler);
		}
	}

	public void unbindAll() {
		for (List<?> list : handlerLists.values()) {
			list.clear();
		}
	}

	/**
	 * Gets the name of the option for an item.
	 * 
	 * @param id The item id.
	 * @param option The option.
	 * @return The option name.
	 */
	private static String getOptionName(int id, Option option) {
		String optionName = NPCDefinitions.forId(id).getOptions()[option.toInteger()];
		return optionName == null ? "null" : optionName.toLowerCase();
	}

	public void handleInteract(Player player, int id) {
		if (player.actionsBlocked()) {
			return;
		}
		NPC npc = World.getWorld().getNpcs().get(id);
		if (npc == null || !player.getPosition().isWithinScene(npc.getPosition()) || npc.isHidden()) {
			return;
		}
		player.turnToTarget(npc);
		if (!npc.alive() || !npc.getDefinition().isAttackable()) {
			return;
		}
		player.startAction(new InitiateCombatAction(player, npc));
	}

	public void handleMagic(Player player, int interfaceId, int childId, int id) {
		if (player.actionsBlocked()) {
			return;
		}
		Spellbook spellbook = player.getSpellbook();
		if (interfaceId != spellbook.getInterfaceId()) {
			return;
		}
		System.out.println(childId);
		Spell spell = spellbook.getSpell(childId);
		if (spell == null) {
			return;
		}
		NPC npc = World.getWorld().getNpcs().get(id);
		if (npc == null || !player.getPosition().isWithinScene(npc.getPosition()) || npc.isHidden()) {
			return;
		}
		player.turnToTarget(npc);
		if (!npc.alive() || !npc.getDefinition().isAttackable()) {
			player.getWalkingQueue().reset();
			return;
		}
		switch (spell.getType()) {
		case COMBAT:
			player.getCombatHandler().setNextSpell((CombatSpell) spell);
			if (!(player.getAction() instanceof CombatAction)) {
				player.startAction(new InitiateCombatAction(player, npc, true));
			}
			break;
		case EFFECT_MOB:
			break;
		case ITEM:
		case TELEPORT:
			return;
		}
	}

	public void handle(Player player, int id, Option option) {
		if (player.actionsBlocked()) {
			return;
		}
		List<NPCHandler> handlers = handlerLists.get(option);
		if (handlers != null) {

			NPC npc = World.getWorld().getNpcs().get(id);
			if (npc == null || !npc.alive() || !player.getPosition().isWithinScene(npc.getPosition())) {
				return;
			}

			String optionName = getOptionName(id, option);
			System.out.println("NPC dispatcher [opt=" + option + "; type=" + npc.getType() + "; optname=" + optionName + "]");
			HandlerContext context = new HandlerContext();

			for (NPCHandler handler : handlers) {

				/* Handle the message parameters */
				handler.handle(player, npc, optionName, context);

				/* Stop propagating the message further if a stop is requested */
				if (context.doStop()) {
					break;
				}
			}
		}
	}
}
