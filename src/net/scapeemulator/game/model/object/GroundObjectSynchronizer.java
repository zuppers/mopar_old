package net.scapeemulator.game.model.object;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlacementCoordsMessage;
import net.scapeemulator.game.msg.impl.object.GroundObjectAnimateMessage;
import net.scapeemulator.game.msg.impl.object.GroundObjectRemoveMessage;
import net.scapeemulator.game.msg.impl.object.GroundObjectUpdateMessage;

/**
 * @author Hadyn Richard
 */
public final class GroundObjectSynchronizer extends GroundObjectListenerAdapter {

    /**
     * The player to synchronize the object list for.
     */
    private final Player player;

    /**
     * The set of unique ids for objects that are within the players scene.
     */
    private final Set<Integer> uids = new HashSet<>();

    /**
     * The list of ground objects to remove once the player reaches a certain height.
     */
    private final List<GroundObject> toRemove = new LinkedList<>();

    public GroundObjectSynchronizer(Player player) {
        this.player = player;
    }

    @Override
    public void groundObjectAdded(GroundObject object) {
        Position position = object.getPosition();
        boolean withinScene = player.getLastKnownRegion().isWithinScene(position);
        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        if (withinScene && sameHeight && !uids.contains(object.getUid())) {
            uids.add(object.getUid());
            sendPlacementCoords(position);
            player.send(new GroundObjectUpdateMessage(object.getPosition(), object.getId(), object.getType().getId(), object.getRotation()));
            return;
        }

        if (!withinScene && uids.contains(object.getUid())) {
            uids.remove(object.getUid());
        }
    }

    @Override
    public void groundObjectIdUpdated(GroundObject object, int oldId) {
        Position position = object.getPosition();
        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        if (sameHeight && player.getLastKnownRegion().isWithinScene(position)) {
            uids.add(object.getUid());
            sendPlacementCoords(position);
            player.send(new GroundObjectUpdateMessage(object.getPosition(), object.getId(), object.getType().getId(), object.getRotation()));
        }
    }

    @Override
    public void groundObjectRotationUpdated(GroundObject object, int oldRotation) {
        Position position = object.getPosition();
        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        if (sameHeight && player.getLastKnownRegion().isWithinScene(position)) {
            uids.add(object.getUid());
            sendPlacementCoords(position);
            player.send(new GroundObjectUpdateMessage(object.getPosition(), object.getId(), object.getType().getId(), object.getRotation()));
        }
    }

    @Override
    public void groundObjectAnimated(GroundObject object) {
        Position position = object.getPosition();
        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        if (sameHeight && player.getLastKnownRegion().isWithinScene(position)) {
            sendPlacementCoords(position);
            player.send(new GroundObjectAnimateMessage(position, object.getAnimationId(), object.getType().getId(), object.getRotation()));
        }
    }

    @Override
    public void groundObjectRemoved(GroundObject object) {
        Position position = object.getPosition();
        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        uids.remove(object.getUid());
        if (sameHeight && player.getPosition().isWithinScene(position)) {
            sendPlacementCoords(position);
            player.send(new GroundObjectRemoveMessage(position, object.getType().getId(), object.getRotation()));
            return;
        }

        if (!sameHeight && player.getPosition().isWithinScene(position)) {
            toRemove.add(object);
        }
    }

    /**
     * Purges the to remove list.
     */
    public void purge() {
        if (!toRemove.isEmpty()) {
            for (GroundObject object : toRemove) {
                boolean sameHeight = object.getPosition().getHeight() == player.getPosition().getHeight();
                Position position = object.getPosition();
                if (sameHeight && player.getPosition().isWithinScene(position)) {
                    sendPlacementCoords(position);
                    player.send(new GroundObjectRemoveMessage(position, object.getType().getId(), object.getRotation()));
                }
            }
            toRemove.clear();
        }
    }

    /**
     * Sends a set of placement coords.
     */
    private void sendPlacementCoords(Position position) {
        int localX = position.getLocalX(player.getLastKnownRegion().getRegionX()) & 0xf8;
        int localY = position.getLocalY(player.getLastKnownRegion().getRegionY()) & 0xf8;
        player.send(new PlacementCoordsMessage(localX, localY));
    }
}
