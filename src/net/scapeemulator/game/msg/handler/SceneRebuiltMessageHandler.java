package net.scapeemulator.game.msg.handler;

import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.MessageHandler;
import net.scapeemulator.game.msg.impl.SceneRebuiltMessage;

/**
 * @author Hadyn Richard
 */
public class SceneRebuiltMessageHandler extends MessageHandler<SceneRebuiltMessage> {

    @Override
    public void handle(Player player, SceneRebuiltMessage msg) {
        player.refreshGroundItems();
        player.refreshGroundObjects();
        if (player.getSceneRebuiltListener() != null) {
            player.getSceneRebuiltListener().sceneRebuilt();
            player.setSceneRebuiltListener(null);
        }
    }
}