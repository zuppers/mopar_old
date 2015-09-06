package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.model.area.Area;
import net.scapeemulator.game.model.player.Player;

/**
 * @author David Insley
 */
public class OpenShopAction extends ReachDistancedAction {

    private final String shop;

    public OpenShopAction(Player player, Area bounds, int distance, String shop) {
        super(1, true, player, bounds, distance);
        this.shop = shop;
    }

    @Override
    public void executeAction() {
        mob.getShopHandler().openShop(shop);
        stop();
    }

}
