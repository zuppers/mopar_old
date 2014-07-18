package net.scapeemulator.game.button;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.Widget;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.magic.AutoCastHandler;
import net.scapeemulator.game.model.player.skills.magic.Spell;
import net.scapeemulator.game.model.player.skills.magic.TeleportSpell;
import net.scapeemulator.game.model.player.skills.prayer.Prayer;

/**
 * Created by Hadyn Richard
 */
public final class ButtonDispatcher {

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
        if (player.actionsBlocked()) {
            return;
        }
        int widgetId = Widget.getWidgetId(hash);
        int child = Widget.getComponentId(hash);
        System.out.println("button dispatcher - parent: " + widgetId + " " + ", child: " + child + ", dyn: " + dyn + ", option: " + option);
        
        // Check for correct interfaces open in the handler!
        
        if (widgetId >= 75 && widgetId <= 93) {
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
            if (spell == null) {
                return;
            }
            switch (spell.getType()) {
            case COMBAT:
            case EFFECT_MOB:
            case ITEM:
                return;
            case TELEPORT:
                ((TeleportSpell) spell).cast(player);
                break;
            }
            break;
        case 271:
            player.getPrayers().toggle(Prayer.forId(child));
            break;
        case 387:
            if (child == 55) {
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
