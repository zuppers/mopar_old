package net.scapeemulator.game.model.player.skills.magic;

import net.scapeemulator.game.model.SpotAnimation;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.requirement.Requirements;

public class Spell {

    private final SpellType type;
    protected final Requirements requirements = new Requirements();
    protected final double xp;
    protected final Animation animation;
    protected final SpotAnimation graphic;

    public Spell(SpellType type, double xp, Animation animation, SpotAnimation graphic) {
        this.type = type;
        this.xp = xp;
        this.animation = animation;
        this.graphic = graphic;
    }

    public SpellType getType() {
        return type;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public double getXp() {
        return xp;
    }
}
