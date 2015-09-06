package net.scapeemulator.game.content.alkharid;

import net.scapeemulator.game.dialogue.Dialogue;
import net.scapeemulator.game.dialogue.DialogueContext;
import net.scapeemulator.game.dialogue.DialogueOption;
import net.scapeemulator.game.dialogue.HeadAnimation;
import net.scapeemulator.game.dispatcher.npc.NPCDispatcher;
import net.scapeemulator.game.dispatcher.npc.NPCHandler;
import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;
import net.scapeemulator.game.dispatcher.object.ObjectHandler;
import net.scapeemulator.game.model.Option;
import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Direction;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.npc.stateful.impl.NormalNPC;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.GroundObjectListenerAdapter;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.BlockedAction;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;
import net.scapeemulator.game.task.Task;
import net.scapeemulator.game.util.HandlerContext;

/**
 * @author David Insley
 */
public class TollGate {

    private static final Dialogue GATE_DIALOGUE = new GateDialogue();
    private static final int GUARD_TYPE = 925;
    private static final Item TOLL = new Item(995, 10);

    public static void init() {
        ObjectDispatcher.getInstance().bind(new ObjectHandler(Option.ONE) {
            @Override
            public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
                if (object != GateObjects.closedLeft && object != GateObjects.closedRight) {
                    return;
                }
                player.startAction(new WalkToGate(player, object, false));
            }
        });
        ObjectDispatcher.getInstance().bind(new ObjectHandler(Option.FOUR) {
            @Override
            public void handle(Player player, GroundObject object, String optionName, HandlerContext context) {
                if (object != GateObjects.closedLeft && object != GateObjects.closedRight) {
                    return;
                }
                player.startAction(new WalkToGate(player, object, true));
            }
        });
        NPCDispatcher.getInstance().bind(new NPCHandler(Option.ONE) {
            @Override
            public void handle(Player player, NPC npc, String option, HandlerContext context) {
                if (npc.getType() == GUARD_TYPE) {
                    context.stop();
                    player.sendMessage("He don't seem very interested in talking.");
                }
            }

        });
        World.getWorld().getGroundObjects().addListener(new GateObjects());
        NPC npc = new NormalNPC(GUARD_TYPE);
        npc.setPosition(new Position(3267, 3226, 0));
        npc.setDirections(Direction.NORTH, Direction.NONE);
        World.getWorld().addNpc(npc);
        npc = new NormalNPC(GUARD_TYPE);
        npc.setPosition(new Position(3268, 3226, 0));
        npc.setDirections(Direction.NORTH, Direction.NONE);
        World.getWorld().addNpc(npc);
    }

    private static class WalkToGate extends ReachDistancedAction {

        private final boolean quickPay;

        public WalkToGate(Player player, GroundObject gate, boolean quickPay) {
            super(1, true, player, gate.getBounds(), 1);
            this.quickPay = quickPay;
        }

        @Override
        public void executeAction() {
            if (!mob.notWalking()) {
                return;
            }
            if (quickPay) {
                if (!mob.getInventory().contains(TOLL)) {
                    mob.sendMessage("You don't have enough coins to pay the toll!");
                    stop();
                } else {
                    mob.getInventory().remove(TOLL);
                    mob.sendMessage("You pay the guard and he opens the gate.");
                    mob.startAction(new WalkThroughGate(mob));
                }
            } else {
                GATE_DIALOGUE.displayTo(mob);
                stop();
            }
        }
    }

    private static class WalkThroughGate extends BlockedAction {

        private final Position dest;
        private boolean started;
        private boolean wasRunning = false;

        public WalkThroughGate(Player player) {
            super(player, 1, true);
            dest = new Position(player.getPosition().getX() >= 3268 ? 3266 : 3269, mob.getPosition().getY());
        }

        @Override
        public void executeAction() {
            if (!started) {
                GateObjects.open();
                World.getWorld().getTaskScheduler().schedule(new Task(4, false) {
                    @Override
                    public void execute() {
                        GateObjects.close();
                        stop();

                    }
                });

                mob.setClipped(false);
                if (mob.isRunning()) {
                    wasRunning = true;
                    mob.getSettings().setRunning(false);
                }
                // TODO change to force walking update block when added maybe?
                mob.getWalkingQueue().addFirstStep(dest);
                started = true;
            }
            if (mob.getPosition().equals(dest)) {
                stop();
            }

        }

        public void stop() {
            mob.setClipped(true);
            if (wasRunning) {
                mob.getSettings().setRunning(true);
            }
            super.stop();
        }

    }

    private static class GateObjects extends GroundObjectListenerAdapter {

        private static final int CLOSED_LEFT_ID = 35549;
        private static final int CLOSED_RIGHT_ID = 35551;

        private static GroundObject closedLeft;
        private static GroundObject closedRight;
        private static GroundObject openLeft;
        private static GroundObject openRight;

        @Override
        public void groundObjectAdded(GroundObject object) {
            if (closedLeft == null && object.getId() == CLOSED_LEFT_ID) {
                closedLeft = object;
                openLeft = World.getWorld().getGroundObjects().put(object.getPosition().copy(-1, 0), 35550, 3, object.getType());
                openLeft.hide();
            }
            if (closedRight == null && object.getId() == CLOSED_RIGHT_ID) {
                closedRight = object;
                openRight = World.getWorld().getGroundObjects().put(object.getPosition().copy(-1, 0), 35552, 1, object.getType());
                openRight.hide();
            }
            if (closedRight != null && closedLeft != null) {
                World.getWorld().getGroundObjects().removeListener(this);
            }
        }

        private static void open() {
            /*
             * We use setId(0) instead of hide() because it keeps the path
             * clipped
             */
            closedRight.setId(0);
            openRight.reveal();
            closedLeft.setId(0);
            openLeft.reveal();
        }

        private static void close() {
            closedRight.setId(CLOSED_RIGHT_ID);
            openRight.hide();
            closedLeft.setId(CLOSED_LEFT_ID);
            openLeft.hide();
        }
    }

    private static class GateDialogue extends Dialogue {

        @Override
        public void initialize(DialogueContext ctx) {
            ctx.openNpcConversationDialogue("Halt! Anyone wishing to enter Al Kharid must pay the toll.", GUARD_TYPE, HeadAnimation.STERN, true);
            ctx.setStage(1);
        }

        @Override
        public void handleOption(DialogueContext ctx, DialogueOption opt) {
            switch (ctx.getStage()) {
            case -1:
                ctx.stop();
                break;
            case 1:
                ctx.openPlayerConversationDialogue("And how much is that?", HeadAnimation.CALMLY_TALKING, true);
                ctx.setStage(2);
                break;
            case 2:
                ctx.openNpcConversationDialogue("10 gold pieces.", GUARD_TYPE, HeadAnimation.PLEASED, true);
                ctx.setStage(ctx.getPlayer().getInventory().contains(TOLL) ? 31 : 32);
                break;
            case 31:
                ctx.openOptionDialogue("10 whole pieces?! That's ridiculous! I'm not paying that.", "Fine. I'll pay that.");
                ctx.setStage(41);
                break;
            case 41:
                if (opt == DialogueOption.OPTION_1) {
                    ctx.openPlayerConversationDialogue("10 whole pieces?! That's ridiculous! I'm not paying that.", HeadAnimation.ANGRY, true);
                    ctx.setStage(511);
                } else {
                    ctx.openPlayerConversationDialogue("Fine. I'll pay that.", HeadAnimation.SAD, true);
                    ctx.setStage(512);
                }
                break;
            case 512:
                Player player = ctx.getPlayer();
                if (!player.getInventory().contains(TOLL)) {
                    // Really shouldn't happen because we checked already... but
                    // just in case.
                    ctx.openNpcConversationDialogue("Bah, you don't even have enough coins! Go away!", GUARD_TYPE, HeadAnimation.ANGRY, true);
                    ctx.setStage(-1);
                } else {
                    ctx.stop();
                    player.getInventory().remove(TOLL);
                    player.sendMessage("You pay the guard and he opens the gate.");
                    player.startAction(new WalkThroughGate(player));
                }
                break;
            case 32:
                ctx.openPlayerConversationDialogue("But I don't have 10 gold pieces...", HeadAnimation.SAD, true);
                ctx.setStage(42);
                break;
            case 42:
            case 511:
                ctx.openNpcConversationDialogue("Then go away!", GUARD_TYPE, HeadAnimation.ANGRY, true);
                ctx.setStage(-1);
                break;
            }
        }

    }

}
