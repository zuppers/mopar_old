package net.scapeemulator.game.model.player.skills.fishing;

import static net.scapeemulator.game.model.player.skills.fishing.FishingTool.*;
import static net.scapeemulator.game.model.player.skills.fishing.Fish.*;
import net.scapeemulator.game.model.Option;

/**
 * @author David Insley
 */
public enum FishingSpot {
    
    SMALLNET_ROD(316, SMALL_NET, FISHING_ROD, new Fish[] { SHRIMP, ANCHOVY }, new Fish[] { SARDINE, HERRING }),
    FLYROD_ROD(309, FLY_FISHING_ROD, FISHING_ROD, new Fish[] { TROUT, SALMON }, new Fish[] { PIKE }),
    CAGE_HARPOON(312, LOBSTER_CAGE, HARPOON, new Fish[] { LOBSTER }, new Fish[] { TUNA, SWORDFISH }),
    BIGNET_HARPOON(322, BIG_NET, HARPOON, new Fish[] { MACKEREL, COD, BASS }, new Fish[] { SHARK }),
    HARPOON_SMALLNET(400, HARPOON, SMALL_NET, new Fish[] { TUNA, SWORDFISH }, new Fish[] { MONKFISH });

    private final int npcId;
    private final FishingTool firstTool;
    private final FishingTool secondTool;
    private final Fish[] firstFish;
    private final Fish[] secondFish;
    
    private FishingSpot(int npcId, FishingTool firstTool, FishingTool secondTool, Fish[] firstFish, Fish[] secondFish) {
        this.npcId = npcId;
        this.firstTool = firstTool;
        this.secondTool = secondTool;
        this.firstFish = firstFish;
        this.secondFish = secondFish;
    }
    
    public int getNpcId() {
        return npcId;
    }
    
    public FishingTool getTool(Option option) {
        switch(option) {
        case ONE:
            return firstTool;
        case TWO:
            return secondTool;
        default:
            throw new IllegalArgumentException("Fishing spots only support options one and two!");          
        }        
    }
    
    public Fish[] getFish(Option option) {
        switch(option) {
        case ONE:
            return firstFish;
        case TWO:
            return secondFish;
        default:
            throw new IllegalArgumentException("Fishing spots only support options one and two!");          
        }        
    }
    
    static FishingSpot forNpcId(int npcId) {
        for(FishingSpot spot : values()) {
            if(spot.npcId == npcId) {
                return spot;
            }
        }
        return null;
    }

}
