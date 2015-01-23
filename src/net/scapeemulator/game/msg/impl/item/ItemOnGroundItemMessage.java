package net.scapeemulator.game.msg.impl.item;

import net.scapeemulator.game.msg.Message;

/**
 * @author zuppers
 */
public class ItemOnGroundItemMessage extends Message {

    private final int x;
    private final int y;
    private final int slot;
    private final int itemId;
    private final int groundItemId;
    private final int widgetHash;

    public ItemOnGroundItemMessage(int x, int y, int slot, int itemId, int groundItemId, int widgetHash) {
        this.x = x;
        this.y = y;
        this.slot = slot;
        this.itemId = itemId;
        this.groundItemId = groundItemId;
        this.widgetHash = widgetHash;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSlot() {
        return slot;
    }

    public int getItemId() {
        return itemId;
    }

    public int getGroundItemId() {
        return groundItemId;
    }

    public int getWidgetHash() {
        return widgetHash;
    }

}
