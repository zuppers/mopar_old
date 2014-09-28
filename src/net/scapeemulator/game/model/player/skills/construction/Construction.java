package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.dialogue.Dialogue;
import net.scapeemulator.game.dialogue.DialogueContext;
import net.scapeemulator.game.dialogue.DialogueOption;
import net.scapeemulator.game.dialogue.Stage;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;

/**
 * @author David
 *
 */
public class Construction {

    public static final int POH_LOADING_INTERFACE = 399;
    public static final int ROOM_CREATE_INTERFACE = 402;
    public static final Dialogue PREVIEW_DIALOGUE = new Dialogue();

    public static void initialize() {
        GameServer.getInstance().getMapLoader().load(29, 79);
        PREVIEW_DIALOGUE.setStartingStage(new Stage() {

            @Override
            public void initializeContext(DialogueContext context) {
                context.openOptionDialogue("Rotate clockwise", "Rotate counter-clockwise", "Finish");
            }

            @Override
            public void handleOption(DialogueContext context, DialogueOption option) {
                switch (option) {
                case OPTION_1:
                    context.getPlayer().getHouse().rotatePreview(Rotation.CW_90);
                    context.setStage(Dialogue.START_STAGE);
                    break;
                case OPTION_2:
                    context.getPlayer().getHouse().rotatePreview(Rotation.CW_270);
                    context.setStage(Dialogue.START_STAGE);
                    break;
                case OPTION_3:
                    context.stop();
                    context.getPlayer().getHouse().finishPreview();
                    break;
                default:
                    break;
                }

            }
        });
    }
}
