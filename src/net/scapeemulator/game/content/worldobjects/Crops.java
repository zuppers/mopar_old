package net.scapeemulator.game.content.worldobjects;

import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.task.Task;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class Crops {

    private static final int RESPAWN_TICKS = 50; // 30 seconds
    private static final Animation PICK_ANIMATION = new Animation(827);

    enum Crop {
        ONION(1957, 3366, 5538, 8584),
        CABBAGE(1965, 1161, 8535, 8536, 8537, 8538, 8539, 8540, 8541, 8542, 8543),
        POTATO(1942, 312, 8562, 9408),
        WHEAT(1947, 313, 5583, 5584, 5585, 15506, 15507, 15508);

        private final int[] objectIds;
        private final int itemId;

        private Crop(int itemId, int... objectIds) {
            this.objectIds = objectIds;
            this.itemId = itemId;
        }

        static Crop forObjectId(int objectId) {
            for (Crop crop : values()) {
                for (int objectIdC : crop.objectIds) {
                    if (objectIdC == objectId) {
                        return crop;
                    }
                }
            }
            return null;
        }

        int getItemId() {
            return itemId;
        }
    }

    public static void init() {
        ObjectDispatcher.getInstance().bind(new CropObjectHandler());
    }

    private static class CropObjectHandler extends ObjectHandler {

        public CropObjectHandler() {
            super(Option.TWO);
        }

        @Override
        public void handle(Player player, final GroundObject object, String optionName, HandlerContext context) {
            Crop crop = Crop.forObjectId(object.getId());
            if (crop == null) {
                return;
            }
            context.stop();
            player.startAction(new PickCropAction(player, object, crop));
        }

    }

    private static class PickCropAction extends ReachObjectAction {
        private final GroundObject obj;
        private final Crop crop;
        private boolean started;

        PickCropAction(Player player, GroundObject obj, Crop crop) {
            super(1, true, player, obj, 1);
            this.obj = obj;
            this.crop = crop;
        }

        public void executeAction() {
            if (!started) {
                mob.setActionsBlocked(true);
                mob.getWalkingQueue().addFirstStep(obj.getPosition());
                mob.playAnimation(PICK_ANIMATION);
                started = true;
            } else {
                mob.setActionsBlocked(false);
                Item remaining = mob.getInventory().add(new Item(crop.getItemId()));
                if (remaining == null) {
                    obj.hide();
                    World.getWorld().getTaskScheduler().schedule(new Task(RESPAWN_TICKS, false) {
                        @Override
                        public void execute() {
                            obj.reveal();
                            stop();
                        }
                    });
                }
                stop();
            }
        }

        @Override
        public void stop() {
            mob.setActionsBlocked(false);
        }

    }
}
