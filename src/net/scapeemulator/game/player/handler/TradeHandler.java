package net.scapeemulator.game.player.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.trade.TradeRequestAction;
import net.scapeemulator.game.player.PlayerHandler;

public final class TradeHandler extends PlayerHandler {

    public TradeHandler() {
        super("trade with");
    }

    @Override
    public void handle(Player player, Player selectedPlayer) {
        player.startAction(new TradeRequestAction(player, selectedPlayer));
    }
}