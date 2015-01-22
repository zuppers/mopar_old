package net.scapeemulator.game.model.player.skills.cooking;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectHandler;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.SlottedItem;

/**
 * @author David Insley
 */
public class HeatSourceHandler extends ItemOnObjectHandler {

    private final HeatSource heatSource;
    private final RawFood food;

    public HeatSourceHandler(int itemId, int objectId, HeatSource heatSource, RawFood food) {
        super(itemId, objectId);
        this.heatSource = heatSource;
        this.food = food;
    }

    @Override
    public void handle(Player player, GroundObject object, SlottedItem item) {
        player.startAction(new CookingAction(player, heatSource, food, item, object));
    }

}