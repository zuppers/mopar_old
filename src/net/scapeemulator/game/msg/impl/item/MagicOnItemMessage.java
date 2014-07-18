package net.scapeemulator.game.msg.impl.item;

import net.scapeemulator.game.msg.Message;

/**
 * @author David Insley
 */
public final class MagicOnItemMessage extends Message {

    private final int tabId;
    private final int spellId;
    private final int slot;
    private final int itemId;

    public MagicOnItemMessage(int tabId, int spellId, int slot, int itemId) {
        this.tabId = tabId;
        this.spellId = spellId;
        this.slot = slot;
        this.itemId = itemId;
    }

    public int getTabId() {
        return tabId;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getSlot() {
        return slot;
    }

    public int getItemId() {
        return itemId;
    }

}
