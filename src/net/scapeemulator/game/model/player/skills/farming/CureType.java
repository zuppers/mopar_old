package net.scapeemulator.game.model.player.skills.farming;

import net.scapeemulator.game.model.mob.Animation;

public enum CureType {

    PLANT_CURE(6036, 2288),
    PRUNING(5329, 2274);

    private final int itemId;
    private final Animation anim;

    private CureType(int itemId, int animId) {
        this.itemId = itemId;
        this.anim = new Animation(animId);
    }

    public int getItemId() {
        return itemId;
    }

    public Animation getAnimation() {
        return anim;
    }

}