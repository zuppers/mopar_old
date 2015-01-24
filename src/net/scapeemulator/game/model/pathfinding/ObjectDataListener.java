package net.scapeemulator.game.model.pathfinding;

import net.scapeemulator.cache.def.ObjectDefinition;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.definition.ObjectDefinitions;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.GroundObjectListenerAdapter;

/**
 * @author Hadyn Richard
 */
public final class ObjectDataListener extends GroundObjectListenerAdapter {

    private final TraversalMap traversalMap;

    public ObjectDataListener(TraversalMap traversalMap) {
        this.traversalMap = traversalMap;
    }

    @Override
    public void groundObjectAdded(GroundObject object) {
        ObjectDefinition def = ObjectDefinitions.forId(object.getId());
        if (!def.isSolid()) {
            return;
        }

        Position position = object.getPosition();

        if (!traversalMap.regionInitialized(position.getX(), position.getY())) {
            traversalMap.initializeRegion(position.getX(), position.getY());
        }

        if (object.getType().isWall()) {
            traversalMap.markWall(object.getRotation(), position.getHeight(), position.getX(), position.getY(), object.getType(), def.isImpenetrable());
        }

        if (object.getType().getId() >= 9 && object.getType().getId() <= 12) {

            /* Flip the length and width if the object is rotated */
            int width = def.getWidth();
            int length = def.getLength();
            if (object.getRotation() == 1 || object.getRotation() == 3) {
                width = def.getLength();
                length = def.getWidth();
            }

            traversalMap.markOccupant(position.getHeight(), position.getX(), position.getY(), width, length, def.isImpenetrable());
        }
    }

    @Override
    public void groundObjectIdUpdated(GroundObject object, int oldId) {
        ObjectDefinition oldDef = ObjectDefinitions.forId(oldId);
        ObjectDefinition newDef = ObjectDefinitions.forId(object.getId());

        if (oldDef.getWidth() == newDef.getWidth() && oldDef.getLength() == newDef.getLength() && oldDef.isImpenetrable() == newDef.isImpenetrable()) {
            return;
        }

        Position position = object.getPosition();
        if (!traversalMap.regionInitialized(position.getX(), position.getY())) {
            traversalMap.initializeRegion(position.getX(), position.getY());
        }

        if (oldDef.isSolid()) {
            if (object.getType().isWall()) {
                traversalMap.unmarkWall(object.getRotation(), position.getHeight(), position.getX(), position.getY(), object.getType(), oldDef.isImpenetrable());
            }

            if (object.getType().getId() >= 9 && object.getType().getId() <= 12) {
                /* Flip the length and width if the object is rotated */
                int width = oldDef.getWidth();
                int length = oldDef.getLength();
                if (object.getRotation() == 1 || object.getRotation() == 3) {
                    width = oldDef.getLength();
                    length = oldDef.getWidth();
                }
                traversalMap.unmarkOccupant(position.getHeight(), position.getX(), position.getY(), width, length, oldDef.isImpenetrable());
            }
        }

        if (newDef.isSolid()) {
            if (object.getType().isWall()) {
                traversalMap.markWall(object.getRotation(), position.getHeight(), position.getX(), position.getY(), object.getType(), newDef.isImpenetrable());
            }

            if (object.getType().getId() >= 9 && object.getType().getId() <= 12) {
                /* Flip the length and width if the object is rotated */
                int width = newDef.getWidth();
                int length = newDef.getLength();
                if (object.getRotation() == 1 || object.getRotation() == 3) {
                    width = newDef.getLength();
                    length = newDef.getWidth();
                }
                traversalMap.markOccupant(position.getHeight(), position.getX(), position.getY(), width, length, newDef.isImpenetrable());
            }
        }
    }

    @Override
    public void groundObjectRotationUpdated(GroundObject object, int oldRotation) {
        ObjectDefinition def = ObjectDefinitions.forId(object.getId());
        if (!def.isSolid()) {
            return;
        }

        Position position = object.getPosition();

        if (!traversalMap.regionInitialized(position.getX(), position.getY())) {
            traversalMap.initializeRegion(position.getX(), position.getY());
        }

        if (object.getType().isWall()) {
            traversalMap.unmarkWall(oldRotation, position.getHeight(), position.getX(), position.getY(), object.getType(), def.isImpenetrable());
            traversalMap.markWall(object.getRotation(), position.getHeight(), position.getX(), position.getY(), object.getType(), def.isImpenetrable());
        }

        if (object.getType().getId() >= 9 && object.getType().getId() <= 12) {
            if (oldRotation % 2 == object.getRotation() % 2) {
                return;
            }
            int width = def.getWidth();
            int length = def.getLength();
            if (1 == oldRotation || oldRotation == 3) {
                width = def.getLength();
                length = def.getWidth();
            }
            traversalMap.unmarkOccupant(position.getHeight(), position.getX(), position.getY(), width, length, def.isImpenetrable());
            traversalMap.markOccupant(position.getHeight(), position.getX(), position.getY(), length, width, def.isImpenetrable());
        }
    }

    @Override
    public void groundObjectRemoved(GroundObject object) {
        ObjectDefinition def = ObjectDefinitions.forId(object.getId());
        if (!def.isSolid()) {
            return;
        }

        Position position = object.getPosition();

        if (!traversalMap.regionInitialized(position.getX(), position.getY())) {
            traversalMap.initializeRegion(position.getX(), position.getY());
        }

        if (object.getType().isWall()) {
            traversalMap.unmarkWall(object.getRotation(), position.getHeight(), position.getX(), position.getY(), object.getType(), def.isImpenetrable());
        }

        if (object.getType().getId() >= 9 && object.getType().getId() <= 12) {

            /* Flip the length and width if the object is rotated */
            int width = def.getWidth();
            int length = def.getLength();
            if (1 == object.getRotation() || object.getRotation() == 3) {
                width = def.getLength();
                length = def.getWidth();
            }

            traversalMap.unmarkOccupant(position.getHeight(), position.getX(), position.getY(), width, length, def.isImpenetrable());
        }
    }
}
