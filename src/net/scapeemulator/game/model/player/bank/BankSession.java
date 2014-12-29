package net.scapeemulator.game.model.player.bank;

import static net.scapeemulator.game.model.player.bank.BankTab.TAB_ALL;
import static net.scapeemulator.game.model.player.interfaces.Interface.BANK;
import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.player.ScriptInputListenerAdapter;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.msg.impl.inter.InterfaceAccessMessage;

/**
 * @author David Insley
 */
public class BankSession extends ComponentListener {

    static {
        ButtonDispatcher.getInstance().bind(new BankInterfaceHandler());
    }

    public static final int BANK_SLOTS = 400;
    public static final int BANK_INVENTORY = 763;
    private static final int[] AMOUNTS = { 1, 5, 10 };

    private final Player player;
    private final Inventory inventory;
    private final Inventory bank;
    private final BankSettings settings;

    /**
     * If true, items will attempt to be withdrawn as notes instead of their normal id. Move to
     * BankSettings to have this persist across bank sessions, and add to the serializer as well to
     * have it persist across login sessions.
     */
    private boolean noteWithdrawal;
    private boolean insert;

    public BankSession(Player player) {
        this.player = player;
        this.inventory = player.getInventory();
        this.bank = player.getBank();
        this.settings = player.getBankSettings();
    }

    public void init() {
        sendOpenTab();
        bank.unlock();

        // Set the deposit/withdraw-x default value and button configs
        player.getStateSet().setState(1249, settings.getLastX());
        player.getStateSet().setState(105, noteWithdrawal ? 1 : 0);
        player.getStateSet().setState(304, insert ? 1 : 0);

        // Configure player inventory
        player.send(new InterfaceAccessMessage(BANK_INVENTORY, 0, 0, 27, 2360446));
        player.getInterfaceSet().openInventory(BANK_INVENTORY);
        inventory.refresh();

        // TODO what do these do?
        // player.send(new ConfigMessage(563, 4194304));
        // player.send(new ScriptMessage(1451, 239, ""));

        sendTabSizes();

        // Configure bank screen
        player.send(new InterfaceAccessMessage(BANK, 73, 0, BANK_SLOTS - 1, 2360446));
        player.getInterfaceSet().openWindow(BANK, this);

        bank.refresh();
    }

    public void handleInterfaceClick(int childId, final int dyn, ExtendedOption option) {
        BankTab tab = BankTab.forChildId(childId);
        if (tab != null) {
            settings.setOpenTab(tab);
            return;
        }
        switch (childId) {
        case 14:
            insert = !insert;
            player.getStateSet().setState(304, insert ? 1 : 0);
            break;
        case 16:
            noteWithdrawal = !noteWithdrawal;
            player.getStateSet().setState(105, noteWithdrawal ? 1 : 0);
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
                player.getScriptInput().showIntegerScriptInput(new ScriptInputListenerAdapter() {
                    @Override
                    public void intInputReceived(int value) {
                        withdraw(dyn, value);
                        settings.setLastX(value);
                        player.getStateSet().setState(1249, value);
                        player.getScriptInput().reset();
                    }

                });
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
            player.getScriptInput().showIntegerScriptInput(new ScriptInputListenerAdapter() {
                @Override
                public void intInputReceived(int value) {
                    deposit(dyn, value);
                    settings.setLastX(value);
                    player.getStateSet().setState(1249, value);
                    player.getScriptInput().reset();
                }
            });
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

                // Move all items one slot to fill the empty space.
                bank.silence();
                for (int i = slot; i < BANK_SLOTS - 1; i++) {
                    bank.set(i, bank.get(i + 1));
                }
                bank.set(BANK_SLOTS - 1, null);
                bank.unsilence();
                settings.decrementTabStarts(fromTab);
                if (fromTab != BankTab.TAB_ALL && settings.getTabSize(fromTab) == 0) {
                    settings.setOpenTab(TAB_ALL);
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
            if (tab != null && tab != settings.getOpenTab()) {
                moveInsert(source, tab);
            }
        } else {
            if (insert) {
                swapInsert(source, dest);
            } else {
                bank.swap(source, dest);
            }
        }
        sendOpenTab();
    }

    /**
     * Called when the player attempts to swap two items but has the 'insert' option selected. Moves
     * the source item to the slot after the destination item. Should only be called if the items
     * are in the same tab.
     * 
     * @param source the slot of the item to move
     * @param dest the slot of the item to insert after
     */
    private void swapInsert(int source, int dest) {
        Item temp = bank.get(source);
        bank.silence();
        if (dest < source) {
            dest += 1;
        }
        int delta = dest > source ? 1 : -1;
        for (int i = source; Math.abs(i - dest) > 0; i += delta) {
            bank.set(i, bank.get(i + delta));
        }
        bank.set(dest, temp);
        bank.unsilence();
        bank.refresh();
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
        int dest = settings.getOpenTab() == BankTab.TAB_ALL ? last : settings.getTabStart(settings.getOpenTab()) + settings.getTabSize(settings.getOpenTab());

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
        settings.incrementTabStarts(settings.getOpenTab());
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
        player.getStateSet().setBitState(4893, settings.getOpenTab().getTabId());
    }

    @Override
    public void inputPressed(Component component, int componentId, int dynamicId) {
    }

    @Override
    public void componentClosed(Component component) {
        endSession();
    }
}
