package net.scapeemulator.game.model.object;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;

/**
 * @author Hadyn Richard
 */
public abstract class GroundObjectListenerAdapter extends GroundObjectListener {

    @Override
    public void groundObjectRotationUpdated(GroundObject object, int oldRotation) {}

    @Override
    public void groundObjectAdded(GroundObject object) {}

    @Override
    public void groundObjectUpdated(GroundObject object) {}

    @Override
    public void groundObjectAnimated(GroundObject object) {}

    @Override
    public void groundObjectRemoved(GroundObject object) {}
}
