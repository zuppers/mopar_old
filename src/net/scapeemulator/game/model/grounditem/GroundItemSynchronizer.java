package net.scapeemulator.game.model.grounditem;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.grounditem.GroundItemList.GroundItem;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.PlacementCoordsMessage;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemCreateMessage;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemRemoveMessage;
import net.scapeemulator.game.msg.impl.grounditem.GroundItemUpdateMessage;

/**
 * @author Hadyn Richard
 * @author David Insley
 */
public final class GroundItemSynchronizer extends GroundItemListener {

    /**
     * The player to synchronize the ground items for.
     */
    private final Player player;

    /**
     * The ids of all the ground items that the synchronizer has handled.
     */
    private final Set<Integer> uids = new HashSet<>();

    /**
     * The list of ground objects to remove once the player reaches a certain height.
     */
    private final List<GroundItem> toRemove = new LinkedList<>();

    /**
     * Constructs a new synchronizer for a player.
     * 
     * @param player player to synchronize the ground items for
     */
    public GroundItemSynchronizer(Player player) {
        this.player = player;
    }

    @Override
    public boolean shouldFireEvents(GroundItem groundItem) {
        int owner = groundItem.getOwner();
        return (owner == -1) || (owner == player.getDatabaseId());
    }

    @Override
    public void groundItemCreated(GroundItem groundItem) {
        Position position = groundItem.getPosition();
        boolean withinScene = player.getLastKnownRegion().isWithinScene(position);
        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        if (withinScene && sameHeight && !uids.contains(groundItem.getUid())) {
            uids.add(groundItem.getUid());
            sendPlacementCoords(position);
            player.send(new GroundItemCreateMessage(groundItem.getItemId(), groundItem.getAmount(), position));
            return;
        }

        /* Remove the ground item from the added list if its not longer in the scene */
        if (!withinScene && uids.contains(groundItem.getUid())) {
            uids.remove(groundItem.getUid());
        }
    }

    @Override
    public void groundItemUpdated(GroundItem groundItem, int previousAmount) {
        Position position = groundItem.getPosition();
        if (uids.contains(groundItem.getUid()) && player.getPosition().isWithinScene(position)) {
            sendPlacementCoords(position);
            player.send(new GroundItemUpdateMessage(groundItem.getItemId(), groundItem.getAmount(), groundItem.getPosition(), previousAmount));
        }
    }

    @Override
    public void groundItemRemoved(GroundItem groundItem) {
        Position position = groundItem.getPosition();

        boolean sameHeight = player.getPosition().getHeight() == position.getHeight();
        uids.remove(groundItem.getUid());
        if (sameHeight && player.getPosition().isWithinScene(position)) {
            sendPlacementCoords(position);
            player.send(new GroundItemRemoveMessage(groundItem.getItemId(), groundItem.getPosition()));
        }

        if (!sameHeight && player.getPosition().isWithinScene(position)) {
            toRemove.add(groundItem);
        }
    }

    /**
     * Purges the to remove list.
     */
    public void purge() {
        if (!toRemove.isEmpty()) {
            for (GroundItem object : toRemove) {
                boolean sameHeight = object.getPosition().getHeight() == player.getPosition().getHeight();
                Position position = object.getPosition();
                if (sameHeight && player.getPosition().isWithinScene(position)) {
                    sendPlacementCoords(position);
                    player.send(new GroundItemRemoveMessage(object.getItemId(), position));
                }
            }
            toRemove.clear();
        }
    }

    private void sendPlacementCoords(Position position) {
        int localX = position.getLocalX(player.getLastKnownRegion().getRegionX()) & 0xfff8;
        int localY = position.getLocalY(player.getLastKnownRegion().getRegionY()) & 0xfff8;
        player.send(new PlacementCoordsMessage(localX, localY));
    }

}
