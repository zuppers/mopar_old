package net.scapeemulator.game.model.player.skills.farming;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.scapeemulator.game.dispatcher.item.ItemOnObjectDispatcher;
import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.skills.farming.items.CureOnPatchHandler;
import net.scapeemulator.game.model.player.skills.farming.items.RakeOnPatchHandler;
import net.scapeemulator.game.model.player.skills.farming.items.SecateursOnPatchHandler;
import net.scapeemulator.game.model.player.skills.farming.items.SeedOnPatchHandler;
import net.scapeemulator.game.model.player.skills.farming.items.SpadeOnPatchHandler;
import net.scapeemulator.game.model.player.skills.farming.object.GuideOptionHandler;
import net.scapeemulator.game.model.player.skills.farming.patch.AllotmentPatch;
import net.scapeemulator.game.model.player.skills.farming.patch.HerbPatch;
import net.scapeemulator.game.model.player.skills.farming.patch.IFarmPatch;
import net.scapeemulator.game.model.player.skills.farming.plant.AllotmentPlant;
import net.scapeemulator.game.model.player.skills.farming.plant.HerbPlant;
import net.scapeemulator.game.model.player.skills.farming.plant.IPlant;

public class Farming {

    // January 1st, 2016, 00:00:00 UTC
    private static final long BASE_MILLIS = 1451635200000L;

    // 2272 = trowel?
    static final Animation COMPOST_ANIM = new Animation(2283);
    static final Animation WATERING_ANIM = new Animation(2293);
    public static final ItemRequirement DIBBER_REQ = new ItemRequirement(5343, false, "You need a seed dibber to plant that.");

    static final Set<IFarmPatch> allPatchTypes = new HashSet<>();
    static final Set<IPlant> allPlantTypes = new HashSet<>();

    // 2274 = prune

    public static void initialize() {
        Collections.addAll(allPatchTypes, AllotmentPatch.values());
        Collections.addAll(allPlantTypes, AllotmentPlant.values());
        
        Collections.addAll(allPatchTypes, HerbPatch.values());
        Collections.addAll(allPlantTypes, HerbPlant.values());
        
        // Collections.addAll(allPatchTypes, HopsPatch.values());

        ObjectDispatcher.getInstance().bind(new GuideOptionHandler());

        for (IFarmPatch patch : allPatchTypes) {

            ItemOnObjectDispatcher.getInstance().bind(new RakeOnPatchHandler(patch));
            ItemOnObjectDispatcher.getInstance().bind(new CureOnPatchHandler(patch));
            ItemOnObjectDispatcher.getInstance().bind(new SecateursOnPatchHandler(patch));
            ItemOnObjectDispatcher.getInstance().bind(new SpadeOnPatchHandler(patch));

            for (IPlant plant : allPlantTypes) {
                // We bind the seed from every plant type from every patch type to allow for proper error messages
                ItemOnObjectDispatcher.getInstance().bind(new SeedOnPatchHandler(plant, patch));
            }

        }

    }

    public static IFarmPatch patchForObjectId(int objectId) {
        for (IFarmPatch patch : allPatchTypes) {
            if (patch.getObjectId() == objectId) {
                return patch;
            }
        }
        return null;
    }

    /**
     * Each farming cycle is active for 5 minutes every X minutes, where X is the cycle time. For example, a 10 minute cycle time is active for 5
     * minutes and then inactive for 5, whereas a 40 minute cycle time is inactive for 35 minutes. Crops can only grow while their cycle time is
     * active.
     * 
     * @param cycle the farming cycle to check
     * @return true if the cycle is currently active
     */
    static boolean cycleActive(FarmingCycle cycle) {
        long minutes = ((System.currentTimeMillis() - BASE_MILLIS) / 1000) / 60;
        return (minutes % cycle.getMinutes()) <= 5;
    }

}
