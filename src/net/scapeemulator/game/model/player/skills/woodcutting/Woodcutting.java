package net.scapeemulator.game.model.player.skills.woodcutting;

import net.scapeemulator.game.dispatcher.object.ObjectDispatcher;

/**
 * @author David Insley
 */
public class Woodcutting {

    /**
     * Just here to make the TreeType enum less messy
     */
    static int[] NORMAL_TREES = { 1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1285, 1286, 1289, 1290, 1291, 1315, 
                                  1316, 1318, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882, 
                                  3883, 5902, 5903, 5904, 10041 };

    public static void initialize() {
        ObjectDispatcher.getInstance().bind(new TreeObjectHandler());
    }
}
