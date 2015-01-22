package net.scapeemulator.game.model.grounditem;

import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.grounditem.GroundItemList.Type;

/**
 * Created by Hadyn Richard
 */
public abstract class GroundItemListener {

    public abstract void groundItemCreated(GroundItem groundItem, Type type);

    public abstract void groundItemUpdated(GroundItem groundItem, int previousAmount, Type type);

    public abstract void groundItemRemoved(GroundItem groundItem, Type type);
}