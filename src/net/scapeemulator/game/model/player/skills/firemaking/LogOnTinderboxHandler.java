package net.scapeemulator.game.model.player.skills.firemaking;

import net.scapeemulator.game.dispatcher.item.ItemOnItemHandler;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author David Insley
 */
public class LogOnTinderboxHandler extends ItemOnItemHandler {

    private final Log log;

    public LogOnTinderboxHandler(Log log) {
        super(log.getItemId(), Firemaking.TINDERBOX);
        this.log = log;
    }

    @Override
    public void handle(Player player, SlottedItem itemOne, SlottedItem itemTwo) {
        player.startAction(new FiremakingAction(player, log, itemOne.getItem().getId() == log.getItemId() ? itemOne : itemTwo));

    }

}