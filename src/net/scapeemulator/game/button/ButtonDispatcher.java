/**
 * Copyright (c) 2012, Hadyn Richard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package net.scapeemulator.game.button;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.Widget;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.appearance.Appearance;
import net.scapeemulator.game.model.player.skills.magic.AutoCastHandler;
import net.scapeemulator.game.model.player.skills.magic.Spell;
import net.scapeemulator.game.model.player.skills.magic.TeleportSpell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hadyn Richard
 */
public final class ButtonDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(ButtonDispatcher.class);
	private Map<Integer, List<ButtonHandler>> handlerLists = new HashMap<>();

	public ButtonDispatcher() {
	}

	public void bind(ButtonHandler handler) {

		/* Calculate the hash for the widget */
		int hash = Widget.getHash(handler.getParent(), handler.getChild());

		List<ButtonHandler> list = handlerLists.get(hash);

		/* Create and store the list if it does not exist */
		if (list == null) {
			list = new LinkedList<>();
			handlerLists.put(hash, list);
		}

		/* Add the handler to the list */
		list.add(handler);
	}

	/**
	 * Unbinds all the handlers.
	 */
	public void unbindAll() {
		handlerLists.clear();
	}

	public void handle(Player player, int hash, int dyn, ExtendedOption option) {
		if(player.actionsBlocked()) {
			return;
		}
		int widgetId = Widget.getWidgetId(hash);
		int child = Widget.getComponentId(hash);
		System.out.println("button dispatcher - parent: " + widgetId + " " + ", child: " + child + ", dyn: " + dyn + ", option: " + option);
		if(widgetId >= 75 && widgetId <= 93) {
			player.getPlayerCombatHandler().attackTabClick(widgetId, child);
			return;
		}
		switch (widgetId) {
		case 105:
			player.getGrandExchangeHandler().handleMainInterface(child, option);
			break;
		case 107:
			player.getGrandExchangeHandler().handleOfferInventoryClick(child, dyn);
			break;
		case 190:
		case 192:
			Spell spell = player.getSpellbook().getSpell(child);
			if(spell == null) {
				return;
			}
			switch(spell.getType()) {
			case COMBAT:
			case EFFECT_MOB:
			case ITEM:
				return;
			case TELEPORT:
				((TeleportSpell) spell).cast(player);
				break;
			}
			break;
		case 387:
			if(child == 55) {
				Equipment.showEquipmentInterface(player);
			}
			break;
		case 310:
		case 319:
		case 388:
		case 406:
			AutoCastHandler.handleSpellSelection(player, widgetId, child);
			break;
		case 771:
			player.getAppearance().handle(child);
			break;
		case 620:
			player.getShopHandler().handleInput(child, dyn, option);
			break;
		case 621:
			player.getShopHandler().handleInventoryClick(dyn, option);
			break;
		default:
			
			/* Fetch the handler list for the specified hash */
			List<ButtonHandler> list = handlerLists.get(hash);

			/* Check if the list is valid */
			if (list == null) {
				return;
			}

			for (ButtonHandler handler : list) {

				/* If the handler option is equal to the option, handle it */
				if (handler.getOption() == option) {
					handler.handle(player, dyn);
				}
			}
			break;
		}

	}
}
