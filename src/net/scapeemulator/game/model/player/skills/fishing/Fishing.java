package net.scapeemulator.game.model.player.skills.fishing;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;

/**
 * @author David Insley
 */
public class Fishing {

    public static void initialize() {
        spawnFishingSpots();
        GameServer.getInstance().getMessageDispatcher().getNpcDispatcher().bind(new FishingSpotHandler(Option.ONE));
        GameServer.getInstance().getMessageDispatcher().getNpcDispatcher().bind(new FishingSpotHandler(Option.TWO));
    }

    private static void spawnSpot(FishingSpot spot, int x, int y) {
        NPC npc = new NormalNPC(spot.getNpcId());
        npc.setPosition(new Position(x, y));
        World.getWorld().addNpc(npc);
    }

    private static void spawnFishingSpots() {
        
        // Catherby fishing spots
        spawnSpot(FishingSpot.CAGE_HARPOON, 2836, 3431);
        spawnSpot(FishingSpot.BIGNET_HARPOON, 2837, 3431);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2838, 3431);
        spawnSpot(FishingSpot.BIGNET_HARPOON, 2844, 3429);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2845, 3429);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2859, 3426);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2853, 3423);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2845, 3423);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2855, 3423);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2860, 3426);

        // Musa point pier fishing spots
        spawnSpot(FishingSpot.CAGE_HARPOON, 2925, 3181);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2926, 3180);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2926, 3179);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2923, 3179);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2921, 3178);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2924, 3181);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2923, 3180);

        // Fishing guild spots
        spawnSpot(FishingSpot.SMALLNET_ROD, 2602, 3414);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2602, 3422);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2604, 3417);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2605, 3420);
        spawnSpot(FishingSpot.BIGNET_HARPOON, 2612, 3411);
        spawnSpot(FishingSpot.BIGNET_HARPOON, 2612, 3415);

        // Draynor village spots
        spawnSpot(FishingSpot.SMALLNET_ROD, 3085, 3231);
        spawnSpot(FishingSpot.SMALLNET_ROD, 3085, 3230);

        // Barbarian village spots
        spawnSpot(FishingSpot.FLYROD_ROD, 3104, 3424);
        spawnSpot(FishingSpot.FLYROD_ROD, 3104, 3425);
        spawnSpot(FishingSpot.FLYROD_ROD, 3110, 3432);
        spawnSpot(FishingSpot.FLYROD_ROD, 3110, 3433);
        spawnSpot(FishingSpot.FLYROD_ROD, 3110, 3434);

        // Relleka spots
        spawnSpot(FishingSpot.SMALLNET_ROD, 2633, 3691);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2633, 3694);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2639, 3698);
        spawnSpot(FishingSpot.CAGE_HARPOON, 2640, 3700);
        spawnSpot(FishingSpot.BIGNET_HARPOON, 2645, 3708);
        spawnSpot(FishingSpot.BIGNET_HARPOON, 2649, 3708);

        // Barbarian outpost spots (different than heavy rod spots)
        spawnSpot(FishingSpot.SMALLNET_ROD, 2498, 3545);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2511, 3562);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2516, 3575);

        // Rimmington spots
        spawnSpot(FishingSpot.SMALLNET_ROD, 2986, 3176);
        spawnSpot(FishingSpot.SMALLNET_ROD, 2997, 3159);

        // Al-Kharid spots
        spawnSpot(FishingSpot.SMALLNET_ROD, 3267, 3148);
        spawnSpot(FishingSpot.SMALLNET_ROD, 3275, 3140);

        // Seers village spots
        spawnSpot(FishingSpot.FLYROD_ROD, 2716, 3530);
        spawnSpot(FishingSpot.FLYROD_ROD, 2726, 3524);

        // Random river spots north of Ardougne
        spawnSpot(FishingSpot.FLYROD_ROD, 2508, 3421);
        spawnSpot(FishingSpot.FLYROD_ROD, 2527, 3412);
        spawnSpot(FishingSpot.FLYROD_ROD, 2537, 3406);
        spawnSpot(FishingSpot.FLYROD_ROD, 2562, 3374);
        spawnSpot(FishingSpot.FLYROD_ROD, 2566, 3370);

        // River north east of castle wars (east of observ.) spots
        spawnSpot(FishingSpot.FLYROD_ROD, 2461, 3150);
        spawnSpot(FishingSpot.FLYROD_ROD, 2465, 3156);

        // Shilo village spots
        spawnSpot(FishingSpot.FLYROD_ROD, 2855, 2974);
        spawnSpot(FishingSpot.FLYROD_ROD, 2855, 2977);
        spawnSpot(FishingSpot.FLYROD_ROD, 2859, 2976);
        spawnSpot(FishingSpot.FLYROD_ROD, 2860, 2972);
        spawnSpot(FishingSpot.FLYROD_ROD, 2864, 2975);

        // Wilderness spots
        spawnSpot(FishingSpot.CAGE_HARPOON, 3347, 3814);
        spawnSpot(FishingSpot.CAGE_HARPOON, 3364, 3800);

        // Lumbridge spots
        spawnSpot(FishingSpot.FLYROD_ROD, 3238, 3252);
        spawnSpot(FishingSpot.FLYROD_ROD, 3239, 3241);
        spawnSpot(FishingSpot.FLYROD_ROD, 3239, 3243);
        spawnSpot(FishingSpot.FLYROD_ROD, 3238, 3253);
        spawnSpot(FishingSpot.CAGE_CRAYFISH, 3259, 3203);
        spawnSpot(FishingSpot.CAGE_CRAYFISH, 3259, 3204);
        spawnSpot(FishingSpot.CAGE_CRAYFISH, 3259, 3206);
    }

}
