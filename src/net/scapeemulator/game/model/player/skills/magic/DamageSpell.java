package net.scapeemulator.game.model.player.skills.magic;

/**
 * @author David Insley
 */
public class DamageSpell extends CombatSpell {
    
    private String name;
    private int autoCastConfig;
    private int maxHit;

    public DamageSpell(String name, int autoCastConfig, int maxHit, int animation, int graphic) {
        super(SpellType.DAMAGE, animation, graphic);
        // TODO anim height should be 0 for teleblock and miasmic spells
        this.autoCastConfig = autoCastConfig;
        this.maxHit = maxHit;
    }
            
    public String getName() {
        return name;
    }
    
    public int getAutoCastConfig() {
        return autoCastConfig;
    }

    public int getMaxHit() {
        return maxHit;
    }

}
