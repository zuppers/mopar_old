package net.scapeemulator.game.model.player.bank;

import net.scapeemulator.game.dispatcher.npc.NPCHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.mob.action.MobInteractionAction;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class BankerAction extends NPCHandler {

    public BankerAction() {
        super(Option.THREE);
    }

    @Override
    public void handle(Player player, NPC npc, String option, HandlerContext context) {
        if (option.equals("bank")) {
            context.stop();
            player.startAction(new MobInteractionAction<Player, NPC>(player, npc, 2) {

                @Override
                public void executeAction() {
                    mob.startBankSession();
                    stop();
                }
            });
        }
    }

}
