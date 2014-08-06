package net.scapeemulator.game.model.player.consumable;

import net.scapeemulator.game.dispatcher.item.ItemHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.PlayerTimers.Timer;
import net.scapeemulator.game.model.player.SlottedItem;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class FoodHandler extends ItemHandler {

    private static final Animation CONSUME_ANIMATION = new Animation(829);

    public FoodHandler() {
        super(Option.ONE);
    }

    @Override
    public void handle(Player player, Inventory inventory, SlottedItem item, String option, HandlerContext context) {
        if (player.getTimers().timerActive(Timer.EAT)) {
            return;
        }
        Food food = Food.forId(item.getItem().getId());
        if (food != null) {
            context.stop();
            player.playAnimation(CONSUME_ANIMATION);
            player.getInventory().remove(item);
            player.heal(food.getHeal());
            int nextBite = food.getNextBite(item.getItem().getId());
            if (nextBite != -1) {
                player.getInventory().add(new Item(nextBite), item.getSlot());
            }
            player.getTimers().setTimer(Timer.EAT, 3);
        }
    }

}
