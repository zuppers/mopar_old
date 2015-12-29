package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.PlayerVariables.Variable;

public enum RCPouch {

    SMALL(5509, 3, -1, Variable.SMALL_POUCH),
    MEDIUM(5510, 6, 45, Variable.MEDIUM_POUCH),
    LARGE(5512, 9, 29, Variable.LARGE_POUCH),
    GIANT(5514, 12, 10, Variable.GIANT_POUCH);

    private final int itemId;
    private final int essHeld;
    private final int usesPerDecay;
    private final Variable playerVar;
    
    private RCPouch(int itemId, int essHeld, int usesPerDecay, Variable playerVar) {
        this.itemId = itemId;
        this.essHeld = essHeld;
        this.usesPerDecay = usesPerDecay;
        this.playerVar = playerVar;
    }

    public static RCPouch forItemId(int itemId) {
        for (RCPouch pouch : values()) {
            if (pouch.itemId == itemId || pouch.getDecayedId() == itemId) {
                return pouch;
            }
        }
        return null;
    }

    public int getAmountInPouch(Player player) {
        int var = player.getVariables().getVar(playerVar);
        return var & 0xFF;
    }
    
    public void setAmountInPouch(Player player, int amount) {
        
        int var = player.getVariables().getVar(playerVar);
        var = var & 0xFFFFFF00;
    }
    
    public int getItemId() {
        return itemId;
    }

    public int getDecayedId() {
        if (this == SMALL) {
            return itemId;
        }
        return itemId + 1;
    }

    public int getMaxEssenceHeld() {
        return essHeld;
    }

    public int getUsesPerDecay() {
        return usesPerDecay;
    }

}