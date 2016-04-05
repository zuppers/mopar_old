package net.scapeemulator.game.model;

import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachObjectAction;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class Ladders {

    private enum LadderType {
        ANY_POS_DOWN(38222),
        ANY_POS_UP(38221);
        // 2113 = mining guild down (tele to 3019, 9739)
        // 30941 = mining guild up , 2112 = door
        private final int[] ids;

        private LadderType(int ... ids) {
            this.ids = ids;
        }

        static LadderType forId(int objectId) {
            for (LadderType type : values()) {
                for (int id : type.ids) {
                    if (id == objectId) {
                        return type;
                    }
                }
            }
            return null;
        }
    }

    public static void init() {
        ObjectDispatcher.getInstance().bind(new LadderHandler());
    }

    private static class LadderHandler extends ObjectHandler {

        public LadderHandler() {
            super(Option.ONE);
        }

        @Override
        public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
            LadderType type = LadderType.forId(object.getId());
            if (type != null) {
                context.stop();
                player.startAction(new UseLadderAction(player, type, object));
            }

        }

    }

    private static class UseLadderAction extends ReachObjectAction {

        private final LadderType type;

        public UseLadderAction(Player player, LadderType type, GroundObject object) {
            super(1, true, player, object, 1, false);
            this.type = type;
        }

        @Override
        public void executeAction() {
            switch (type) {
                case ANY_POS_DOWN:
                    mob.teleport(mob.getPosition().copy(0, 0, -1));
                    stop();
                    break;
                case ANY_POS_UP:
                    mob.teleport(mob.getPosition().copy(0, 0, 1));
                    stop();
                    break;
            }

        }

    }
}
