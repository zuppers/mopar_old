package net.scapeemulator.game.model.npc.stateful.impl;

import static net.scapeemulator.game.model.npc.stateful.impl.NormalNPC.State.*;

import java.util.Random;

import net.scapeemulator.game.model.npc.stateful.StatefulNPC;
import net.scapeemulator.game.model.npc.stateful.handler.RandomWalkStateHandler;
import net.scapeemulator.game.model.npc.stateful.handler.WalkToSpawnStateHandler;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC.State;

/**
 * @author Hadyn Richard
 */
public final class NormalNPC extends StatefulNPC<State> {

    /**
     * The random used to calculate if the NPC should randomly walk.
     */
    private static final Random random = new Random();

    /**
     * Each of the enumerable states.
     */
    public enum State {
        NONE, WALK_RANDOMLY, WALK_TO_SPAWN
    }

    /**
     * Constructs a new {@link NormalNPC};
     * 
     * @param type The type id.
     */
    public NormalNPC(int type) {
        super(type);
        initialize();
    }

    /**
     * Initializes the NPC by binding the appropriate state handlers.
     */
    private void initialize() {
        bindHandler(WALK_RANDOMLY, new RandomWalkStateHandler());
        bindHandler(WALK_TO_SPAWN, new WalkToSpawnStateHandler());
    }

    @Override
    public State determineState() {
        if (getCombatHandler().getNoRetaliate() > 0 || frozen() || !alive()) {
            return NONE;
        }

        if (getWalkingBounds() != null) {
            if (!getWalkingBounds().withinArea(position.getX(), position.getY(), getDefinition().getLeashRange(), true)) {
                return WALK_TO_SPAWN;
            }
            /* 10% chance to randomly walk around, ~every 3 seconds */
            if (random.nextInt(15) == 0 && getWalkingQueue().isEmpty() && !isTurnToTargetSet()) {
                return WALK_RANDOMLY;
            }
        } else {
            if (getSpawnPosition().getDistance(position) > getDefinition().getLeashRange()) {
                return WALK_TO_SPAWN;
            }
        }

        return NONE;
    }

}
