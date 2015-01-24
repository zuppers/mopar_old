package net.scapeemulator.game.model.object;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;

/**
 * @author Hadyn Richard
 */
public abstract class GroundObjectListener {

    /**
     * Called when a ground object was added.
     */
    public abstract void groundObjectAdded(GroundObject object);

    /**
     * Called when a ground object was updated.
     */
    public abstract void groundObjectIdUpdated(GroundObject object, int oldId);

    /**
     * Called when the rotation of a ground object was updated.
     */
    public abstract void groundObjectRotationUpdated(GroundObject object, int oldRotation);

    /**
     * Called when a ground object was animated.
     */
    public abstract void groundObjectAnimated(GroundObject object);

    /**
     * Called when a ground object was removed.
     */
    public abstract void groundObjectRemoved(GroundObject object);

}
