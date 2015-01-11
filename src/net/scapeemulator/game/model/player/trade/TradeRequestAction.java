package net.scapeemulator.game.model.player.trade;

import net.scapeemulator.game.model.mob.action.MobInteractionAction;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class TradeRequestAction extends MobInteractionAction<Player, Player> {

    public TradeRequestAction(Player player, Player other) {
        super(player, other, 1);
    }

    @Override
    public void executeAction() {
        if(target == mob) {
            mob.sendMessage("Why would you want to trade with yourself?");
        } else if (target.getInterfaceSet().getWindow().getCurrentId() != -1) {
            mob.sendMessage("That player is busy at the moment.");
        } else if (target.wantsToTrade(mob)) {
            mob.setTradeRequest(null);
            target.setTradeRequest(null);
            mob.setTradeSession(new TradeSession(mob, target));
            target.setTradeSession(new TradeSession(target, mob));
            mob.getTradeSession().init();
            target.getTradeSession().init();
        } else {
            target.sendMessage(mob.getDisplayName() + ":tradereq:");
            mob.sendMessage("Sending trade request...");
            mob.setTradeRequest(target);
        }
        stop();
    }

}
