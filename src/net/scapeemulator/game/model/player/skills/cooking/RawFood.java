package net.scapeemulator.game.model.player.skills.cooking;

import net.scapeemulator.game.model.player.requirement.InventorySpaceRequirement;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.requirement.Requirement;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.fishing.Fish;

/**
 * @author David Insley
 */
public enum RawFood {
    
    // Baking
    BREAD(1, 40, 2307, 2309, 2311, 34, true),
    PITTA(1, 40, 1863, 1865, 1867, 1, true), // Can't be burned
    PIZZA(35, 143, 2287, 2289, 2305, 68, true),
    CAKE(40, 180, 1889, 1891, 1903, 73, true, InventorySpaceRequirement.ONE_SLOT), // TODO give tin back
    
    // Meat
    BEEF(1, 30, 2132, 2142, 2146, 34),
    CHICKEN(1, 30, 2138, 2140, 2144, 34),
    RABBIT(1, 30, 3226, 3228, 7222, 34),
    UGTHANKI(1, 40, 1859, 1861, 2146, 34),
    
    // Fish
    SHRIMP(Fish.SHRIMP, 1, 30, 323, 34),
    SARDINE(Fish.SARDINE, 1, 40, 369, 34),
    ANCHOVY(Fish.ANCHOVY, 1, 30, 323, 34),
    HERRING(Fish.HERRING, 5, 50, 357, 34),
    MACKEREL(Fish.MACKEREL, 10, 60, 357, 45),
    TROUT(Fish.TROUT, 15, 70, 343, 50),
    COD(Fish.COD, 18, 75, 343, 50),
    PIKE(Fish.PIKE, 20, 80, 343, 52),
    SALMON(Fish.SALMON, 25, 90, 343, 58),
    TUNA(Fish.TUNA, 35, 100, 367, 64, -1),
    LOBSTER(Fish.LOBSTER, 40, 120, 381, 74, -6),
    BASS(Fish.BASS, 43, 130, 367, 80),
    SWORDFISH(Fish.SWORDFISH, 45, 140, 375, 86, -5),
    MONKFISH(Fish.MONKFISH, 62, 150, 7948, 92, -2),
    SHARK(Fish.SHARK, 80, 210, 387, 104, -10);

    
    private final int levelReq;
    private final double xp;
    private final int rawId;
    private final int cookedId;
    private final int burnedId;
    private final int stopBurn;
    private final int gauntletMod;
    private final boolean reqStove;
    private final Requirements requirements;
    
    private RawFood(Fish fish, int levelReq, double xp, int burnedId, int stopBurn) {
        this(levelReq, xp, fish.getRawId(), fish.getCookedId(), burnedId, stopBurn, 0);
    }
    
    private RawFood(Fish fish, int levelReq, double xp, int burnedId, int stopBurn, int gauntletMod) {
        this(levelReq, xp, fish.getRawId(), fish.getCookedId(), burnedId, stopBurn, gauntletMod);
    }
    
    private RawFood(int levelReq, double xp, int rawId, int cookedId, int burnedId, int stopBurn) {
        this(levelReq, xp, rawId, cookedId, burnedId, stopBurn, 0, false);
    }
    
    private RawFood(int levelReq, double xp, int rawId, int cookedId, int burnedId, int stopBurn, boolean reqStove, Requirement... reqs) {
        this(levelReq, xp, rawId, cookedId, burnedId, stopBurn, 0, reqStove, reqs);
    } 
    
    private RawFood(int levelReq, double xp, int rawId, int cookedId, int burnedId, int stopBurn, int gauntletMod) {
        this(levelReq, xp, rawId, cookedId, burnedId, stopBurn, gauntletMod, false);
    }
    
    private RawFood(int levelReq, double xp, int rawId, int cookedId, int burnedId, int stopBurn, int gauntletMod, boolean reqStove, Requirement... reqs) {
        this.levelReq = levelReq;
        this.xp = xp;
        this.rawId = rawId;
        this.cookedId = cookedId;
        this.burnedId = burnedId;
        this.stopBurn = stopBurn;
        this.gauntletMod = gauntletMod;
        this.reqStove = reqStove;
        requirements = new Requirements();
        requirements.addRequirement(new SkillRequirement(Skill.COOKING, levelReq, true, "cook that"));
        requirements.addRequirement(new ItemRequirement(rawId, false));
        requirements.addRequirements(reqs);
    }

    public int getLevelReq() {
        return levelReq;
    }

    public double getXp() {
        return xp;
    }

    public int getRawId() {
        return rawId;
    }

    public int getCookedId() {
        return cookedId;
    }

    public int getBurnedId() {
        return burnedId;
    }

    public int getStopBurn() {
        return stopBurn;
    }

    public int getGauntletMod() {
        return gauntletMod;
    }
    
    public boolean requiresStove() {
        return reqStove;
    }
    
    public Requirements getRequirements() {
        return requirements;
    }
}
