package net.scapeemulator.game.model.player.skills.cooking;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.skills.firemaking.Log;

/**
 * @author David Insley
 */
public enum HeatSource {

    FIRE(0, Cooking.FIRE_ANIMATION, Log.NORMAL.getFireId()),
    RANGE(3, Cooking.RANGE_ANIMATION),
    SPECIAL_RANGE(5, Cooking.RANGE_ANIMATION, 114);

    private final double cookingBonus;
    private final Animation anim;
    private final int[] objectIds;

    private HeatSource(double cookingBonus, Animation anim, int... objectIds) {
        this.cookingBonus = cookingBonus;
        this.anim = anim;
        this.objectIds = objectIds;
    }

    public double getCookingBonus() {
        return cookingBonus;
    }

    public Animation getAnimation() {
        return anim;
    }

    public int[] getObjectIds() {
        return objectIds;
    }

}
