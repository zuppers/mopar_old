package net.scapeemulator.game.model.grounditem;

import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;

/**
 * @author Hadyn Richard
 */
public abstract class GroundItemListener {

    public abstract boolean shouldFireEvents(GroundItem groundItem);

    public abstract void groundItemCreated(GroundItem groundItem);

    public abstract void groundItemUpdated(GroundItem groundItem, int previousAmount);

    public abstract void groundItemRemoved(GroundItem groundItem);
}