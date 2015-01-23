package net.scapeemulator.game.model;

import net.scapeemulator.game.model.grandexchange.GrandExchange;
import net.scapeemulator.game.model.grounditem.GroundItemList;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.object.GroundObjectList;
import net.scapeemulator.game.model.pathfinding.ObjectDataListener;
import net.scapeemulator.game.model.pathfinding.TraversalMap;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.net.game.GameSession;
import net.scapeemulator.game.task.TaskScheduler;
import net.scapeemulator.game.update.PlayerUpdater;

/**
 * Represents our singleton world which can be retrieved by {@link getWorld()}. This contains a
 * {@link MobList} for {@link Player} and one for {@link Npc}. This contains a
 * {@link GroundItemList} and a {@link GroundObjectList}.
 *
 */
public final class World {

    public static final int MAX_PLAYERS = 2000;

    private static final World world = new World();

    /**
     * The world instance.
     * 
     * @return The world.
     */
    public static World getWorld() {
        return world;
    }

    private final MobList<Player> players = new MobList<>(MAX_PLAYERS);
    private final MobList<NPC> npcs = new MobList<>(32000);
    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final GrandExchange grandExchange = new GrandExchange();
    private final PlayerUpdater updater = new PlayerUpdater(this);
    private final GroundItemList groundItems = new GroundItemList();
    private final GroundObjectList groundObjects = new GroundObjectList();
    private final TraversalMap traversalMap = new TraversalMap();

    /**
     * Adds a listener ({@link ObjectDataListener}) to our groundObjects list.
     */
    private World() {
        /* TODO: Is this in the correct place? */
        /* Add the object data listener for the traversal map */
        groundObjects.addListener(new ObjectDataListener(traversalMap));
    }

    /**
     * Gets the {@link MobList} of all Players in this world.
     * 
     * @return The {@link Player}-s in this {@link World}.
     */
    public MobList<Player> getPlayers() {
        return players;
    }

    /**
     * Add the npc to our MobList of npcs.
     * 
     * @param npc The {@link NPC} to add.
     */
    public void addNpc(NPC npc) {
        npcs.add(npc);
    }

    /**
     * Gets the {@link MobList} of all Npcs in this world.
     * 
     * @return The {@link NPC}-s in this {@link World}.
     */
    public MobList<NPC> getNpcs() {
        return npcs;
    }

    /**
     * Gets the {@link GroundItemList} of this world.
     * 
     * @return The {@link GroundItem}-s in this {@link World}.
     */
    public GroundItemList getGroundItems() {
        return groundItems;
    }

    /**
     * Gets the {@link GroundObjectList} of this world.
     * 
     * @return The {@link GroundObject}-s in this {@link World}.
     */
    public GroundObjectList getGroundObjects() {
        return groundObjects;
    }

    /**
     * Gets the {@link TraversalMap} of this world.
     * 
     * @return The {@link TraversalMap} of this {@link World}.
     */
    public TraversalMap getTraversalMap() {
        return traversalMap;
    }

    /**
     * Gets the {@link TaskScheduler} of this world.
     * 
     * @return The {@link TaskScheduler} in this {@link World}.
     */
    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    /**
     * Gets the {@link GrandExchange} of this world.
     * 
     * @return The {@link GrandExchange} in this {@link World}.
     */
    public GrandExchange getGrandExchange() {
        return grandExchange;
    }

    /**
     * Performs {@link GameSession#processMessageQueue()} for every {
     *
     * @Player in {@link getPlayers()} as well as perform the {@link TaskScheduler#tick()},
     *         {@link GroundItemList#tick()} & {@link PlayerUpdater#tick()}.
     */
    public void tick() {
        for (Player player : players) {
            GameSession session = player.getSession();
            if (session != null) {
                session.processMessageQueue();
            }
        }
        taskScheduler.tick();
        groundItems.tick();
        updater.tick();
    }

    /**
     * Get a {@link Player} for which {@link Player#getUsername()} or
     * {@link Player#getDisplayName()} returns username, ignoring the cases. Iterates over {@link
     * getPlayers()} to find the {@link Player}.
     *
     * @param username The String used to find a match.
     * @return The {@link Player} we found a match for. Null if no match was found.
     */
    public Player getPlayerByName(String username) {
        for (Player player : players) {
            if (player.getUsername().equalsIgnoreCase(username) || player.getDisplayName().equalsIgnoreCase(username)) {
                return player;
            }
        }

        return null;
    }

    /**
     * Get a {@link Player} for which {@link Player#getLongUsername()} returns longName. Iterates
     * over {@link getPlayers()} to find the {@link Player}.
     *
     * @param longName The long used to find a match.
     * @return The {@link Player} we found a match for. Null if no match was found.
     */
    public Player getPlayerByLongName(long longName) {
        for (Player player : players) {
            if (player.getLongUsername() == longName) {
                return player;
            }
        }
        return null;
    }

    /**
     * Get a {@link Player} associated with the databaseId. Iterates over {@link getPlayers()} and
     * uses {@link Player#getDatabaseId()}.
     *
     * @param id The id to search the associated player with.
     * @return The player or null if no player was found to be associated with the given id.
     */
    public Player getPlayerByDatabaseId(int id) {
        for (Player player : players) {
            if (player.getDatabaseId() == id) {
                return player;
            }
        }

        return null;
    }

    /**
     * Send the provided text to all players, {@link getPlayers()} using
     * {@link Player#sendMessage(String text)}.
     *
     * @param text The text to send to every player.
     */
    public void sendGlobalMessage(String text) {
        for (Player player : players) {
            player.sendMessage(text);
        }
    }
}