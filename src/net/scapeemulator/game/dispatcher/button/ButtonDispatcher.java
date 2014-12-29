package net.scapeemulator.game.dispatcher.button;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.Widget;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.skills.prayer.Prayer;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class ButtonDispatcher {

    private Map<Integer, List<ButtonHandler>> handlerLists = new HashMap<>();
    private Map<Integer, WindowHandler> windowHandlers = new HashMap<>();

    public ButtonDispatcher() {
    }

    /**
     * Shortcut method to get the GameServer instance of this dispatcher.
     * 
     * @return the GameServer instance of the ButtonDispatcher
     */
    public static ButtonDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getButtonDispatcher();
    }

    public void bind(WindowHandler handler) {
        for (int windowId : handler.getWindowIds()) {
            if (windowHandlers.put(windowId, handler) != null) {
                System.out.println("Duplicate window handler entries for ID: " + windowId);
            }
        }
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
        System.out.println("button - parent: " + widgetId + " " + ", child: " + child + ", dyn: " + dyn + ", option: " + option);

        // Check for correct interfaces open in the handler!
        WindowHandler wHandler = windowHandlers.get(widgetId);
        if (wHandler != null) {
            if (wHandler.handle(player, widgetId, child, option, dyn)) {
                return;
            }
        }
        // TODO convert all of these switched ones to the new WindowHandler
        switch (widgetId) {

        case 271:
            player.getPrayers().toggle(Prayer.forId(child));
            break;
        case 387:
            if (child == 55) {
                Equipment.showEquipmentInterface(player);
            }
            break;
        case 771:
            player.getAppearance().handle(child);
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
