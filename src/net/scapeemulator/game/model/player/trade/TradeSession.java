package net.scapeemulator.game.model.player.trade;

import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.ScriptInputListenerAdapter;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.model.player.inventory.*;
import net.scapeemulator.game.msg.impl.ScriptMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceAccessMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;
import net.scapeemulator.game.task.Action;

/**
 * Represents a trade session between two players.
 * 
 * @author David Insley
 */
public class TradeSession extends ComponentListener {

    static {
        ButtonDispatcher.getInstance().bind(new TradeInterfaceHandler());
    }

    private static final int VERIFY_WINDOW = 334;
    private static final int TRADE_WINDOW = 335;
    private static final int TRADE_INVENTORY = 336;
    private static final String WHITE = "<col=FFFFFF>";
    private static final String ORANGE = "<col=FF9040>";
    private static final String WAITING = "Waiting for other player...";
    private static final String ACCEPTED = "Other player has accepted.";
    private static final int[] AMOUNTS = { 1, 5, 10, Integer.MAX_VALUE };

    private final Player player;
    private final Player otherPlayer;

    private TradeStatus status = TradeStatus.INIT;
    private TradeSession otherSession;
    private Inventory inventory;
    private Inventory tradeInventory;

    public TradeSession(Player player, Player otherPlayer) {
        this.player = player;
        this.otherPlayer = otherPlayer;
    }

    public void init() {
        player.startAction(new Action<Player>(player, 1, true) {
            @Override
            public void execute() {
            }

            @Override
            public void stop() {
                decline();
                super.stop();
            }
        });
        otherSession = otherPlayer.getTradeSession();

        // Configure player inventory
        player.getInventory().lock();
        inventory = new Inventory(player.getInventory());
        inventory.addListener(new InventorySpaceChangedListener());
        inventory.addListener(new InventoryMessageListener(player, -1, -1, 93));
        player.send(new InterfaceAccessMessage(TRADE_INVENTORY, 0, 0, 27, 1278));
        player.send(new ScriptMessage(150, "IviiiIsssssssss", "", "", "", "", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, TRADE_INVENTORY << 16));
        player.getInterfaceSet().openInventory(TRADE_INVENTORY);
        inventory.refresh();

        // Configure trade screen
        player.send(new InterfaceAccessMessage(TRADE_WINDOW, 30, 0, 27, 1278));
        player.send(new InterfaceAccessMessage(TRADE_WINDOW, 32, 0, 27, 1278));
        player.send(new ScriptMessage(150, "IviiiIsssssssss", "", "", "", "", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, TRADE_WINDOW << 16 | 30));
        player.send(new ScriptMessage(695, "IviiiIsssssssss", "", "", "", "", "", "", "", "", "", -1, 0, 7, 4, 90, TRADE_WINDOW << 16 | 32));
        player.setInterfaceText(TRADE_WINDOW, 15, "Trading with: " + otherPlayer.getDisplayName());
        player.setInterfaceText(TRADE_WINDOW, 36, "");
        tradeInventory = new Inventory(player, 28);
        tradeInventory.addListener(new InventoryMessageListener(player, -1, -1, 90));
        tradeInventory.addListener(new TradeItemsChangedListener());
        tradeInventory.addListener(new InventoryMessageListener(otherPlayer, -2, 60981, 90));
        player.getInterfaceSet().openWindow(TRADE_WINDOW, this);
        tradeInventory.refresh();

        status = TradeStatus.UPDATING_ITEMS;
    }

    private void secondWindow() {
        player.setInterfaceText(VERIFY_WINDOW, 37, itemsToString());
        player.setInterfaceText(VERIFY_WINDOW, 41, otherPlayer.getTradeSession().itemsToString());
        player.setInterfaceText(VERIFY_WINDOW, 45, "Trading with:<br>" + otherPlayer.getDisplayName());
        player.send(new InterfaceVisibleMessage(VERIFY_WINDOW, 37, true));
        player.send(new InterfaceVisibleMessage(VERIFY_WINDOW, 41, true));
        // player.send(new InterfaceVisibleMessage(VERIFY_WINDOW, 46, true)); TODO TRADE MODIFIED
        player.getInterfaceSet().getWindow().removeListener();
        player.getInterfaceSet().openWindow(VERIFY_WINDOW, this);

        status = TradeStatus.VERIFYING;
    }

    public void handleInterfaceClick(int windowId, int childId, final int dynamicId, ExtendedOption option) {
        if (player.getInterfaceSet().getWindow().getCurrentId() != windowId) {
            return;
        }
        switch (windowId) {
        case VERIFY_WINDOW:
            switch (childId) {
            case 20:
                accept();
                break;
            case 8: // Close
            case 21: // Decline
                decline();
                break;
            }
            break;
        case TRADE_WINDOW:
            switch (childId) {
            case 16:
                accept();
                break;
            case 12: // Close
            case 18: // Decline
                decline();
                break;
            case 30:
                if (dynamicId < 0 || dynamicId > 27) {
                    return;
                }
                final Item item = tradeInventory.get(dynamicId);
                if (item == null) {
                    return;
                }
                if (option == ExtendedOption.NINE) {
                    player.sendMessage(item.getDefinition().getExamine());
                    return;
                }
                if (status != TradeStatus.UPDATING_ITEMS && status != TradeStatus.WAITING_FIRST) {
                    return;
                }
                switch (option) {
                case ONE:
                case TWO:
                case THREE:
                case FOUR:
                    Item removed = tradeInventory.remove(new Item(item.getId(), AMOUNTS[option.toInteger()]), dynamicId);
                    inventory.add(removed);
                    break;
                case FIVE:
                    player.getScriptInput().showIntegerScriptInput(new ScriptInputListenerAdapter() {
                        @Override
                        public void intInputReceived(int value) {
                            Item removed = tradeInventory.remove(new Item(item.getId(), value), dynamicId);
                            inventory.add(removed);
                            player.getScriptInput().reset();
                        }
                    });
                    break;
                default:
                    return;
                }
                break;
            }
            break;
        }
    }

    public void handleInventoryClick(final int dynamicId, ExtendedOption option) {
        if (player.getInterfaceSet().getInventory().getCurrentId() != TRADE_INVENTORY) {
            return;
        }
        if (dynamicId < 0 || dynamicId > 27) {
            return;
        }
        final Item item = inventory.get(dynamicId);
        if (item == null) {
            return;
        }
        if (option == ExtendedOption.NINE) {
            player.sendMessage(item.getDefinition().getExamine());
            return;
        }
        if (status != TradeStatus.UPDATING_ITEMS && status != TradeStatus.WAITING_FIRST) {
            return;
        }
        switch (option) {
        case ONE:
        case TWO:
        case THREE:
        case FOUR:
            Item removed = inventory.remove(new Item(item.getId(), AMOUNTS[option.toInteger()]), dynamicId);
            tradeInventory.add(removed);
            break;
        case FIVE:
            player.getScriptInput().showIntegerScriptInput(new ScriptInputListenerAdapter() {
                @Override
                public void intInputReceived(int value) {
                    Item removed = inventory.remove(new Item(item.getId(), value), dynamicId);
                    tradeInventory.add(removed);
                    player.getScriptInput().reset();
                }
            });
            break;
        default:
            return;
        }
    }

    private void accept() {
        switch (status) {
        case INIT:
        case WAITING_FIRST:
        case WAITING_FINISH:
            break;
        case UPDATING_ITEMS:
            if (otherSession.status == TradeStatus.WAITING_FIRST) {
                tradeInventory.lock();
                otherSession.tradeInventory.lock();
                secondWindow();
                otherSession.secondWindow();
            } else {
                status = TradeStatus.WAITING_FIRST;
                player.setInterfaceText(TRADE_WINDOW, 36, WAITING);
                otherPlayer.setInterfaceText(TRADE_WINDOW, 36, ACCEPTED);
            }
            break;
        case VERIFYING:
            if (otherSession.status == TradeStatus.WAITING_FINISH) {
                if (canComplete() && otherSession.canComplete()) {
                    player.sendMessage("Trade accepted.");
                    otherPlayer.sendMessage("Trade accepted.");
                    player.getInventory().unlock();
                    player.getInventory().removeAll(tradeInventory.toArray());
                    player.getInventory().addAll(otherSession.tradeInventory.toArray());
                    otherPlayer.getInventory().unlock();
                    otherPlayer.getInventory().removeAll(otherSession.tradeInventory.toArray());
                    otherPlayer.getInventory().addAll(tradeInventory.toArray());
                }
                closeTrade();
                otherSession.closeTrade();
            } else {
                status = TradeStatus.WAITING_FINISH;
                player.setInterfaceText(VERIFY_WINDOW, 33, WAITING);
                otherPlayer.setInterfaceText(VERIFY_WINDOW, 33, ACCEPTED);
            }
            break;
        }
    }

    private boolean canComplete() {

        /*
         * Copy the players inventory into a temporary one we can manipulate. The players inventory
         * shouldn't have been modified since the start of the trade because we locked it.
         */
        Inventory temp = new Inventory(player.getInventory());

        if (!temp.removeAll(tradeInventory.toArray()).isEmpty()) {
            player.sendMessage("There was a problem with the trade. Please try again.");
            otherPlayer.sendMessage("There was a problem with the trade. Please try again.");
            return false;
        }

        if (!temp.addAll(otherSession.tradeInventory.toArray()).isEmpty()) {
            player.sendMessage("You do not have enough free inventory space to complete that transaction.");
            otherPlayer.sendMessage("Other player does not have enough free inventory space.");
            return false;
        }

        return true;

    }

    /**
     * Called when the player closes the interface or clicks decline.
     */
    private void decline() {
        player.sendMessage("Trade cancelled.");
        otherPlayer.sendMessage("Other player declined the trade.");
        closeTrade();
        otherSession.closeTrade();
    }

    private void closeTrade() {
        player.getInterfaceSet().getWindow().removeListener();
        player.getInterfaceSet().closeInventory();
        player.getInterfaceSet().closeWindow();
        player.getScriptInput().reset();
        player.getInventory().unlock();
        player.getInventory().refresh();
        player.setTradeSession(null);
    }

    private String itemsToString() {
        // TODO two columns?
        if (tradeInventory.isEmpty()) {
            return WHITE + "Absolutely nothing!";
        }
        String string = "";
        for (Item item : tradeInventory.toArray()) {
            if (item == null) {
                continue;
            }
            string += ORANGE + item.getDefinition().getName();
            string += (item.getAmount() == 1 ? "" : (WHITE + " x " + item.getAmount())) + "<br>";
        }
        return string;
    }

    private class TradeItemsChangedListener implements InventoryListener {

        @Override
        public void itemChanged(Inventory inventory, int slot, Item item, Item oldItem) {
            if (oldItem != null) {
                otherPlayer.send(new ScriptMessage(143, "Iiii", slot, 7, 4, TRADE_WINDOW << 16 | 33));
            }
            itemsChanged(inventory);
        }

        @Override
        public void itemsChanged(Inventory inventory) {
            player.setInterfaceText(TRADE_WINDOW, 36, "");
            otherPlayer.setInterfaceText(TRADE_WINDOW, 36, "");
            status = TradeStatus.UPDATING_ITEMS;
            otherSession.status = TradeStatus.UPDATING_ITEMS;
        }

        @Override
        public void capacityExceeded(Inventory inventory) {
        }
    }

    private class InventorySpaceChangedListener implements InventoryListener {

        @Override
        public void itemChanged(Inventory inventory, int slot, Item item, Item oldItem) {
            itemsChanged(inventory);
        }

        @Override
        public void itemsChanged(Inventory inventory) {
            otherPlayer.setInterfaceText(TRADE_WINDOW, 21, player.getDisplayName() + " has " + inventory.freeSlots() + " free inventory slots.");
        }

        @Override
        public void capacityExceeded(Inventory inventory) {
        }

    }

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
    }

    @Override
    public void componentClosed(Component component) {
        decline();
    }
}
