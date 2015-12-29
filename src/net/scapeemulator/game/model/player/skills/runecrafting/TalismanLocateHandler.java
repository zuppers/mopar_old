package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.dispatcher.item.ItemHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class TalismanLocateHandler extends ItemHandler {

    public TalismanLocateHandler() {
        super(Option.FOUR);
    }

    @Override
    public void handle(Player player, SlottedItem item, String optionName, HandlerContext context) {
        RCAltar altar = RCAltar.forTalismanId(item.getItem().getId());
        if (altar != null) {
            int deltaX = player.getPosition().getX() - altar.getRuinsPos().getX();
            int deltaY = player.getPosition().getY() - altar.getRuinsPos().getY();
            if (Math.abs(deltaX) <= 4 && Math.abs(deltaY) <= 4) {
                player.sendMessage("You can't tell which direction the talisman is pulling.");
            } else {
                Direction dir = Direction.between(player.getPosition(), altar.getRuinsPos());
                player.sendMessage("You feel the talisman pull towards the " + dir.name().toLowerCase().replace("_", "-") + ".");
            }
        }
    }

}