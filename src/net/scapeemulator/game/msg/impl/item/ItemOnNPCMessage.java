package net.scapeemulator.game.msg.impl.item;

import net.scapeemulator.game.msg.Message;

/**
 * @author zuppers
 */
public class ItemOnNPCMessage extends Message {

    private final int itemId;
    private final int slot;
    private final int npcIndex;
    private final int widgetHash;

    public ItemOnNPCMessage(int itemId, int slot, int npcIndex, int widgetHash) {
        this.itemId = itemId;
        this.slot = slot;
        this.npcIndex = npcIndex;
        this.widgetHash = widgetHash;
    }

    public int getItemId() {
        return itemId;
    }

    public int getSlot() {
        return slot;
    }

    public int getNPCIndex() {
        return npcIndex;
    }

    public int getWidgetHash() {
        return widgetHash;
    }

}
