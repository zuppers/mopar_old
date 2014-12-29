package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.dialogue.Dialogue;
import net.scapeemulator.game.dialogue.DialogueContext;
import net.scapeemulator.game.dialogue.DialogueOption;
import net.scapeemulator.game.dialogue.Stage;
import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.dispatcher.button.WindowHandler;
import net.scapeemulator.game.model.ExtendedOption;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.ScriptInputListenerAdapter;
import net.scapeemulator.game.model.player.requirement.ItemRequirement;
import net.scapeemulator.game.model.player.skills.construction.House.BuildingSession;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.player.skills.construction.room.RoomType;

/**
 * @author David Insley
 */
public class Construction {

    /**
     * 
     */
    private static final boolean ENABLED = false;

    public static final int POH_LOADING_INTERFACE = 399;
    public static final int ROOM_CREATE_INTERFACE = 402;
    public static final Animation BUILD_ANIM = new Animation(3676);
    public static final Dialogue PREVIEW_DIALOGUE = new Dialogue();
    public static final Dialogue ROOM_DELETION_DIALOGUE = new Dialogue();
    public static final Dialogue ENTER_PORTAL_DIALOGUE = new Dialogue();
    public static final ItemRequirement SAW_REQ = new ItemRequirement(8794, false, "You need a hammer and saw to make furniture.");
    public static final ItemRequirement HAMMER_REQ = new ItemRequirement(2347, false, "You need a hammer and saw to make furniture.");

    public static void initialize() {

        if (!ENABLED) {
            return;
        }

        // Load the room palette regions
        GameServer.getInstance().getMapLoader().load(29, 79);

        ButtonDispatcher.getInstance().bind(new WindowHandler(ROOM_CREATE_INTERFACE) {
            @Override
            public boolean handle(Player player, int windowId, int child, ExtendedOption option, int dyn) {
                if (player.getHouse().getBuildingSession() != null) {
                    player.getHouse().getBuildingSession().handleSelectRoomInterface(child);
                }
                return true;
            }
        });

        ENTER_PORTAL_DIALOGUE.setStartingStage(new Stage() {

            @Override
            public void initializeContext(DialogueContext context) {
                context.openOptionDialogue("Go to your house", "Go to your house (building mode)", "Go to a friend's house", "Nevermind");
            }

            @Override
            public void handleOption(final DialogueContext context, DialogueOption option) {

                switch (option) {
                case OPTION_1:
                case OPTION_2:
                    context.stop();
                    context.getPlayer().getHouse().ownerEnterPortal(option == DialogueOption.OPTION_2);
                    break;
                case OPTION_3:
                    context.stop();
                    context.getPlayer().getScriptInput().showUsernameScriptInput("Enter name:", new ScriptInputListenerAdapter() {
                        @Override
                        public void usernameInputReceived(long value) {
                            Player friend = World.getWorld().getPlayerByLongName(value);
                            if (friend != null) {
                                if (friend == context.getPlayer()) {
                                    context.getPlayer().getHouse().ownerEnterPortal(false);
                                } else {
                                    friend.getHouse().otherEnterPortal(context.getPlayer());
                                }
                            } else {
                                context.getPlayer().sendMessage("No online user found by that name.");
                            }
                        }
                    });
                    break;
                case OPTION_4:
                default:
                    context.stop();
                    break;
                }
            }
        });

        PREVIEW_DIALOGUE.setStartingStage(new Stage() {

            @Override
            public void initializeContext(DialogueContext context) {
                context.openOptionDialogue("Rotate clockwise", "Rotate counter-clockwise", "Finish", "Cancel");
            }

            @Override
            public void handleOption(DialogueContext context, DialogueOption option) {
                BuildingSession session = context.getPlayer().getHouse().getBuildingSession();
                if (session == null) {
                    context.stop();
                    return;
                }
                switch (option) {
                case OPTION_1:
                    session.rotatePreview(Rotation.CW_90);
                    context.setStage(Dialogue.START_STAGE);
                    break;
                case OPTION_2:
                    session.rotatePreview(Rotation.CW_270);
                    context.setStage(Dialogue.START_STAGE);
                    break;
                case OPTION_3:
                    context.stop();
                    session.finishPreview();
                    break;
                case OPTION_4:
                default:
                    context.stop();
                    session.cancelPreview();
                    break;
                }

            }
        });
        PREVIEW_DIALOGUE.setStartingStage(new Stage() {

            @Override
            public void initializeContext(DialogueContext context) {
                context.openOptionDialogue("Rotate clockwise", "Rotate counter-clockwise", "Finish");
            }

            @Override
            public void handleOption(DialogueContext context, DialogueOption option) {
                BuildingSession session = context.getPlayer().getHouse().getBuildingSession();
                if (session == null) {
                    context.stop();
                    return;
                }
                switch (option) {
                case OPTION_1:
                    session.rotatePreview(Rotation.CW_90);
                    context.setStage(Dialogue.START_STAGE);
                    break;
                case OPTION_2:
                    session.rotatePreview(Rotation.CW_270);
                    context.setStage(Dialogue.START_STAGE);
                    break;
                case OPTION_3:
                    context.stop();
                    session.finishPreview();
                    break;
                default:
                    break;
                }

            }
        });

        ROOM_DELETION_DIALOGUE.setStartingStage(new Stage() {

            @Override
            public void initializeContext(DialogueContext context) {
                context.openTextDialogue("Are you sure you want to delete this room? Any furniture inside will be lost.", true);
            }

            @Override
            public void handleOption(DialogueContext context, DialogueOption option) {
                context.setStage("YESNO");
            }
        });

        ROOM_DELETION_DIALOGUE.addStage("YESNO", new Stage() {

            @Override
            public void initializeContext(DialogueContext context) {
                context.openOptionDialogue("Yes, remove it.", "No thanks, I've changed my mind.");
            }

            @Override
            public void handleOption(DialogueContext context, DialogueOption option) {
                BuildingSession session = context.getPlayer().getHouse().getBuildingSession();
                if (session == null) {
                    context.stop();
                    return;
                }
                switch (option) {
                case OPTION_1:
                    session.confirmDeletion();
                    context.stop();
                    break;
                case OPTION_2:
                    session.cancelDeletion();
                    context.stop();
                    break;
                default:
                    break;
                }

            }
        });
        GameServer.getInstance().getMessageDispatcher().getObjectDispatcher().bind(new ObjectBuildHandler());
    }

    static RoomType defaultRoom(int height) {
        switch (height) {
        case 0:
            return RoomType.DUNGEON_CLEAR;
        case 1:
            return RoomType.GRASS;
        default:
            return RoomType.NONE;
        }
    }
}
