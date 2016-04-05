package net.scapeemulator.game.model.player.skills.fletching;

public enum Arrow {
    
    BRONZE(1, 19.5, 39, 882),
    IRON(15, 37.5, 40, 884),
    STEEL(30, 75, 41, 886),
    MITHRIL(45, 112.5, 42, 888),
    ADAMANT(60, 150, 43, 890),
    RUNE(75, 187.4, 44, 892),
    DRAGON(90, 224.5, 11237, 11212);
    
    private Arrow(int level, double xp, int tipId, int arrowId) {
        
    }
}
