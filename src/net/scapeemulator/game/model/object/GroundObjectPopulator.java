package net.scapeemulator.game.model.object;

import net.scapeemulator.cache.def.ObjectDefinition;
import net.scapeemulator.game.cache.MapListenerAdapter;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.definition.ObjectDefinitions;

/**
 * @author Hadyn Richard
 */
public final class GroundObjectPopulator extends MapListenerAdapter {

    /**
     * The ground object list to populate.
     */
    private final GroundObjectList list;

    /**
     * Constructs a new {@link GroundObjectPopulator};
     */
    public GroundObjectPopulator(GroundObjectList list) {
        this.list = list;
    }

    @Override
    public void objectDecoded(int id, int rotation, ObjectType type, Position position) {
        /* Stop the list from appending the object to the updated list */
        list.setRecordUpdates(false);

        ObjectDefinition def = ObjectDefinitions.forId(id);

        /* Only insert an object if it has a name */
        if (!"null".equals(def.getName()) || shouldForce(position) || def.getChildIds() != null) {
            list.put(position, id, def.getAnimationId(), rotation, type);
        }

        /* Reset the record updates state of the list */
        list.setRecordUpdates(true);
    }

    private static boolean shouldForce(Position pos) {
        int x = pos.getRegionX();
        int y = pos.getRegionY();
        return (x >= 232 && x < 247 && y >= 632 && y <= 639);
    }
}
