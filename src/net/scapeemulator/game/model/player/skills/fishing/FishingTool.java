package net.scapeemulator.game.model.player.skills.fishing;

import net.scapeemulator.game.model.mob.Animation;

/**
 * @author David Insley
 */
public enum FishingTool {
    
    SMALL_NET(303, 620, "You cast out your net..."),
    CRAYFISH_CAGE(13431, 10009, "You attempt to catch a crayfish..."),
    BIG_NET(305, 620, "You cast out your large net..."),
    FISHING_ROD(307, 313, 622, "You cast out your line..."),
    FLY_FISHING_ROD(309, 314, 622, "You cast out your fly fishing rod..."),
    HARPOON(311, 618, "You start harpooning fish."),
    LOBSTER_CAGE(301, 619, "You lower the cage into the water...");
    
    private final int toolId;
    private final int baitId;
    private final Animation animation;
    private final String message;
    
    private FishingTool(int toolId, int animationId, String message) {
        this(toolId, -1, animationId, message);
    }
    
    private FishingTool(int toolId, int baitId, int animationId, String message) {
        this.toolId = toolId;
        this.baitId = baitId;
        animation = new Animation(animationId);
        this.message = message;
    }
    
    public int getToolId() {
        return toolId;
    }
    
    public int getBaitId() {
        return baitId;
    }
    
    public Animation getAnimation() {
        return animation;
    }
    
    public String getMessage() {
        return message;
    }
}
