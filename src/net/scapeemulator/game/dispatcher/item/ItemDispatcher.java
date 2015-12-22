package net.scapeemulator.game.dispatcher.item;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.interfaces.Interface;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.model.player.skills.magic.EffectItemSpell;
import net.scapeemulator.game.model.player.skills.magic.Spell;
import net.scapeemulator.game.model.player.skills.magic.Spellbook;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author Hadyn Richard
 */
public final class ItemDispatcher {

    /**
     * The mapping for all of the handler lists.
     */
    private Map<Option, List<ItemHandler>> handlerLists = new HashMap<>();

    /**
     * Constructs a new {@link ItemDispatcher};
     */
    public ItemDispatcher() {
        for (Option option : Option.values()) {
            if (option.equals(Option.ALL)) {
                continue;
            }
            handlerLists.put(option, new LinkedList<ItemHandler>());
        }
        bind(new ItemEquipHandler());
    }

    /**
     * Binds a handler to this dispatcher.
     * 
     * @param handler the handler to bind
     */
    public void bind(ItemHandler handler) {
        if (handler.getOption().equals(Option.ALL)) {
            for (Map.Entry<Option, List<ItemHandler>> entry : handlerLists.entrySet()) {
                entry.getValue().add(handler);
            }
        } else {
            List<ItemHandler> list = handlerLists.get(handler.getOption());
            list.add(handler);
        }
    }

    private static boolean validateInventory(Inventory inventory, int id, int slot) {
        return inventory.get(slot) != null && inventory.get(slot).getId() == id;
    }

    /**
     * Gets the name of the option for an item.
     * 
     * @param id the item id
     * @param option the option
     * @return the option name
     */
    private static String getOptionName(int id, Option option) {
        String optionName = ItemDefinitions.forId(id).getInventoryOptions()[option.toInteger()];
        return optionName == null ? "null" : optionName.toLowerCase();
    }

    public void handle(Player player, int id, int slot, int hash, Option option) {
        if (player.actionsBlocked() || player.getInterfaceSet().getInventory().getCurrentId() != Interface.INVENTORY) {
            return;
        }
        List<ItemHandler> handlers = handlerLists.get(option);
        if (handlers != null) {

            Inventory inventory = player.getInventorySet().get(hash);
            if (inventory != player.getInventory() || !validateInventory(inventory, id, slot)) {
                return;
            }

            SlottedItem slottedItem = new SlottedItem(slot, inventory.get(slot));
            String optionName = getOptionName(id, option);

            HandlerContext context = new HandlerContext();

            for (ItemHandler handler : handlers) {

                /* Handle the message parameters */
                handler.handle(player, slottedItem, optionName, context);

                if (context.doStop()) {
                    break;
                }
            }
        }
    }

    public void handleMagic(Player player, int tabId, int spellId, int slot, int itemId) {
        System.out.println("[MagicOnItem] tab/spell: (" + tabId + "/" + spellId + ") slot/itemid: (" + slot + "/" + itemId + ")");
        if (player.actionsBlocked()) {
            return;
        }

        Spellbook spellbook = player.getSpellbook();
        if (tabId != spellbook.getInterfaceId()) {
            return;
        }

        if (!validateInventory(player.getInventory(), itemId, slot)) {
            return;
        }

        Spell spell = spellbook.getSpell(spellId);
        if (spell == null) {
            return;
        }

        switch (spell.getType()) {
        case ITEM:
            ((EffectItemSpell) spell).cast(player, itemId, slot);
            break;
        default:
            return;
        }
    }

    public static ItemDispatcher getInstance() {
        return GameServer.getInstance().getMessageDispatcher().getItemDispatcher();
    }
}
