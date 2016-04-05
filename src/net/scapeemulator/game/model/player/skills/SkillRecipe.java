package net.scapeemulator.game.model.player.skills;

import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.requirement.Requirements;

public abstract class SkillRecipe {

    protected Requirements requirements;
    
    public Item getProduct() {
        return null;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }
}
