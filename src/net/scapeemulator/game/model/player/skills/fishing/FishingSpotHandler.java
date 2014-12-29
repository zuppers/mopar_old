package net.scapeemulator.game.model.player.skills.fishing;

import net.scapeemulator.game.dispatcher.npc.NPCHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class FishingSpotHandler extends NPCHandler {

    public FishingSpotHandler(Option option) {
        super(option);
    }

    @Override
    public void handle(Player player, NPC npc, String option, HandlerContext context) {
        FishingSpot spot = FishingSpot.forNpcId(npc.getType());
        if (spot != null) {
            context.stop();
            player.startAction(new FishingAction(player, npc, spot, getOption()));
        }
    }

}
