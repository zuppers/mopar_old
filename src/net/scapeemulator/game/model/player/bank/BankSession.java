package net.scapeemulator.game.model.player.bank;

import static net.scapeemulator.game.model.player.Interface.BANK;
import static net.scapeemulator.game.model.player.bank.BankTab.TAB_ALL;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.ComponentListener;
import net.scapeemulator.game.model.player.IntegerScriptInputListener;
import net.scapeemulator.game.model.player.InterfaceSet.Component;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.msg.impl.ConfigMessage;
import net.scapeemulator.game.msg.impl.ScriptMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceAccessMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;

/**
 * @author David Insley
 */
public class BankSession extends ComponentListener {

    public static final int BANK_SLOTS = 400;
    public static final int BANK_INVENTORY = 763;
    private static final int[] AMOUNTS = { 1, 5, 10 };
    private static final int[] HIDDEN_COMPONENTS = { 18, 19, 23 }; // burden inv, help

    private final Player player;
    private final Inventory inventory;
    private final Inventory bank;
    private final BankSettings settings;

    private BankTab openTab;

    /**
     * If true, items will attempt to be withdrawn as notes instead of their normal id. Move to
     * BankSettings to have this persist across sessions.
     */
    private boolean noteWithdrawal;

    public BankSession(Player player) {
        this.player = player;
        this.inventory = player.getInventory();
        this.bank = player.getBank();
        this.settings = player.getBankSettings();
    }

    public void init() {
        openTab = TAB_ALL;
        sendOpenTab();
        bank.unlock();
        
        // Set the deposit/withdraw-x default value
        player.send(new ConfigMessage(1249, settings.getLastX()));

        // Configure player inventory
        player.send(new InterfaceAccessMessage(BANK_INVENTORY, 0, 0, 27, 2360446));
        player.getInterfaceSet().openInventory(BANK_INVENTORY);
        inventory.refresh();

        player.send(new ConfigMessage(563, 4194304));
        player.send(new ScriptMessage(1451, 239, ""));

        sendTabSizes();

        // Configure bank screen
        player.send(new InterfaceAccessMessage(BANK, 73, 0, BANK_SLOTS - 1, 2360446));
        for (int hide : HIDDEN_COMPONENTS) {
            player.send(new InterfaceVisibleMessage(BANK, hide, false));
        }
        player.getInterfaceSet().openWindow(BANK, this);

        bank.refresh();
    }

    public void handleInterfaceClick(int childId, final int dyn, ExtendedOption option) {
        BankTab tab = BankTab.forChildId(childId);
        if (tab != null) {
            openTab = tab;
            return;
        }
        switch (childId) {
        case 16:
            noteWithdrawal = !noteWithdrawal;
            break;
        case 73:
            switch (option) {
            case ONE:
            case TWO:
            case THREE:
                withdraw(dyn, AMOUNTS[option.toInteger()]);
                break;
            case FOUR:
                withdraw(dyn, settings.getLastX());
                break;
            case FIVE:
                player.getScriptInput().setIntegerInputListener(new IntegerScriptInputListener() {
                    @Override
                    public void inputReceived(int value) {
                        withdraw(dyn, value);
                        settings.setLastX(value);
                        player.send(new ConfigMessage(1249, value));
                        player.getScriptInput().reset();
                    }
                });
                player.getScriptInput().showIntegerScriptInput();
                break;
            case SIX:
                withdraw(dyn, Integer.MAX_VALUE);
                break;
            case NINE:
                player.sendMessage(bank.get(dyn).getDefinition().getExamine());
                break;
            default:
                break;
            }
            break;
        }
    }

    public void handleInventoryClick(int childId, final int dyn, ExtendedOption option) {
        if (childId != 0) {
            return;
        }
        switch (option) {
        case ONE:
        case TWO:
        case THREE:
            deposit(dyn, AMOUNTS[option.toInteger()]);
            break;
        case FOUR:
            deposit(dyn, settings.getLastX());
            break;
        case FIVE:
            player.getScriptInput().setIntegerInputListener(new IntegerScriptInputListener() {
                @Override
                public void inputReceived(int value) {
                    deposit(dyn, value);
                    settings.setLastX(value);
                    player.send(new ConfigMessage(1249, value));
                    player.getScriptInput().reset();
                }
            });
            player.getScriptInput().showIntegerScriptInput();
            break;
        case SIX:
            deposit(dyn, Integer.MAX_VALUE);
            break;
        case NINE:
            player.sendMessage(inventory.get(dyn).getDefinition().getExamine());
            break;
        default:
            break;
        }

    }

    /**
     * Attempts to deposit items from the players inventory to the bank.
     * 
     * @param slot the slot of the item in the players inventory
     * @param amount the amount to deposit
     */
    private void deposit(int slot, int amount) {
        Item originalItem = inventory.get(slot);
        if (originalItem == null) {
            return;
        }

        if (!originalItem.getDefinition().canBank()) {
            player.sendMessage("A magical force prevents you from banking that item.");
            return;
        }

        // Check the requested deposit amount against the players actual amount
        int invenAmount = inventory.getAmount(originalItem.getId());
        amount = amount > invenAmount ? invenAmount : amount;

        Item toDeposit = new Item(originalItem.getDefinition().getUnnoted(), amount);

        int depositedAmount = 0;

        /*
         * If we already have this stack in the bank, we can use the normal inventory add methods.
         * If not, we can't use the normal inventory methods because it uses set instead of insert
         * for the preferred slot.
         */
        if (bank.contains(toDeposit.getId())) {
            Item notAdded = bank.add(toDeposit);
            if (notAdded != null) {
                depositedAmount = amount - notAdded.getAmount();
            } else {
                depositedAmount = amount;
            }
        } else {
            if (bank.freeSlot() == -1) {
                player.sendMessage("You have run out of bank space.");
            } else {
                insert(toDeposit);
                depositedAmount = amount;
            }
        }

        /*
         * If we actually deposited anything, make sure to remove the original item from the
         * inventory.
         */
        if (depositedAmount > 0) {
            inventory.remove(new Item(originalItem.getId(), depositedAmount), slot);
        }

        sendOpenTab();
    }

    private void withdraw(int slot, int amount) {
        BankTab fromTab = settings.getTab(slot);
        Item originalItem = bank.get(slot);
        if (originalItem == null) {
            return;
        }

        // Check the requested withdraw amount against the actual amount in the bank
        amount = amount > originalItem.getAmount() ? originalItem.getAmount() : amount;

        Item toWithdraw = new Item(originalItem.getId(), amount);
        if (noteWithdrawal) {
            if (originalItem.getDefinition().canSwap()) {
                toWithdraw = new Item(originalItem.getDefinition().getNoted(), amount);
            } else {
                player.sendMessage("That item cannot be withdrawn as a note.");
            }
        }

        int withdrawn = 0;
        Item notAdded = inventory.add(toWithdraw);

        if (notAdded != null) {
            withdrawn = amount - notAdded.getAmount();
        } else {
            withdrawn = amount;
        }

        // If we actually withdrew anything, make sure to remove the original item from the bank.
        if (withdrawn > 0) {
            bank.remove(new Item(originalItem.getId(), withdrawn), slot);
            if (bank.get(slot) == null) {
                /*
                 * Move all items one slot to fill the empty space.
                 */
                bank.silence();
                for (int i = slot; i < BANK_SLOTS - 1; i++) {
                    bank.set(i, bank.get(i + 1));
                }
                bank.set(BANK_SLOTS - 1, null);
                bank.unsilence();
                settings.decrementTabStarts(fromTab);
                if (fromTab != BankTab.TAB_ALL && settings.getTabSize(fromTab) == 0) {
                    openTab = BankTab.TAB_ALL;
                }
                sendTabSizes();
                bank.refresh();
            }
        }

        sendOpenTab();
    }

    public void handleBankSwap(int childId2, int source, int dest) {
        if (bank.get(source) == null) {
            return;
        }
        if (childId2 != 73) {
            BankTab tab = BankTab.forChildId(childId2);
            if (tab != null && tab != openTab) {
                moveInsert(source, tab);
            }
        } else {
            bank.swap(source, dest);
        }
        sendOpenTab();
    }

    /**
     * Moves an item from the specified bank slot to the end of the given tab. Creates a new tab if
     * it does not exist.
     * 
     * @param source the slot of the item to move
     * @param destTab the tab to move the item to
     */
    private void moveInsert(int source, BankTab destTab) {
        BankTab sourceTab = settings.getTab(source);

        /*
         * If we're not moving to the main tab and the destination tab does no exist, make sure to
         * set up a new one.
         */
        if (destTab != BankTab.TAB_ALL && settings.getTabSize(destTab) == 0) {
            destTab = settings.createTab();
        }

        /*
         * Calculate the destination slot at the end of the destination tab, using the banks next
         * free slot if the destination is the 'all' tab.
         */
        int dest = destTab == BankTab.TAB_ALL ? bank.freeSlot() : settings.getTabStart(destTab) + settings.getTabSize(destTab);

        /*
         * If the destination slot is greater than the source slot, take into account that
         * everything will shift.
         */
        if (dest > source) {
            dest -= 1;
        }

        Item temp = bank.get(source);

        /*
         * Move all of the items between the two slots in the correct direction, and then set the
         * destination slot to the new item.
         */
        bank.silence();
        int delta = dest > source ? 1 : -1;
        for (int i = source; Math.abs(i - dest) > 0; i += delta) {
            bank.set(i, bank.get(i + delta));
        }
        bank.set(dest, temp);
        bank.unsilence();

        /*
         * Increase the tab start slot of everything greater than the destination tab because we
         * inserted a new item there, then decrease the tab starts of all tabs (source tab + 1)
         * because we removed the item. These two operations will overlap only adjusting some of the
         * tabs. If a tab is emptied, tabs shift appropriately.
         */
        settings.incrementTabStarts(destTab);
        settings.decrementTabStarts(sourceTab);
        sendTabSizes();

        bank.refresh();
    }

    /**
     * Inserts an item at the end of the current open tab.
     * 
     * @param source the slot of the item to move
     * @param destTab the tab to move the item to
     */
    private void insert(Item item) {
        if (bank.freeSlot() == -1) {
            return;
        }
        int last = bank.freeSlot();

        /*
         * Calculate the destination slot at the end of the open tab, using the banks next free slot
         * if the destination is the 'all' tab.
         */
        int dest = openTab == BankTab.TAB_ALL ? last : settings.getTabStart(openTab) + settings.getTabSize(openTab);

        /*
         * Move all items one slot to make room for the new one.
         */
        bank.silence();
        for (int i = last; i > dest; i--) {
            bank.set(i, bank.get(i - 1));
        }
        bank.set(dest, item);
        bank.unsilence();

        /*
         * Increase the tab start slot of everything greater than the destination tab because we
         * inserted a new item there.
         */
        settings.incrementTabStarts(openTab);
        sendTabSizes();

        bank.refresh();
    }

    private void endSession() {
        player.getScriptInput().reset();
        player.endBankSession();
        bank.lock();
    }

    private void sendTabSizes() {
        for (BankTab tab : BankTab.values()) {
            if (tab == TAB_ALL) {
                continue;
            }
            player.getStateSet().setBitState(4885 + tab.index(), settings.getTabSize(tab));
        }
    }

    private void sendOpenTab() {
        player.getStateSet().setBitState(4893, openTab.getTabId());
    }

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
    }

    @Override
    public void componentClosed(Component component) {
        endSession();
    }
}
