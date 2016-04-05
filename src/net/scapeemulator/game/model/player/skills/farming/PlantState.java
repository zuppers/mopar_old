package net.scapeemulator.game.model.player.skills.farming;

public enum PlantState {
    
    /**
     * The plant is healthy and growing.
     */
    GROWING,

    /**
     * The plant is fully grown.
     */
    GROWN,

    /**
     * The plant is watered and cannot become diseased on its next growth tick. Allotments and flowers only.
     */
    WATERED,

    /**
     * The plant is diseased.
     */
    DISEASED,

    /**
     * The plant is completely dead.
     */
    DEAD,

    /**
     * Indicates that the tree is fully grown and the player has checked its health for XP. Trees only.
     */
    HEALTH_CHECKED,

    /**
     * Indicates that the tree is fully grown and the player has checked its health and cut it down. Trees only.
     */
    STUMP;
}
