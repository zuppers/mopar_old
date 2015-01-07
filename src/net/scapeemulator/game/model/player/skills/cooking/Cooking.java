package net.scapeemulator.game.model.player.skills.cooking;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectDispatcher;
import net.scapeemulator.game.model.mob.Animation;

/**
 * @author David Insley
 */
public class Cooking {

    public static final int COOKING_GAUNTLETS = 775;

    public static final Animation RANGE_ANIMATION = new Animation(896);
    public static final Animation FIRE_ANIMATION = new Animation(896);

    public static void initialize() {
        for (HeatSource obj : HeatSource.values()) {
            for (int objId : obj.getObjectIds()) {
                for (RawFood food : RawFood.values()) {
                    ItemOnObjectDispatcher.getInstance().bind(new HeatSourceHandler(food.getRawId(), objId, obj, food));
                }
            }
        }
    }

}
