package net.scapeemulator.game.model.player.skills.mining;

import net.scapeemulator.game.GameServer;

/**
 * @author David Insley
 */
public class Mining {

    static final int[] CLAY_ROCKS = { 31062, 31063, 15503, 15504, 15505 };
    static final int[] COPPER_ROCKS = { 2090, 2091, 2110, 11189, 11190, 11191, 31080, 31081, 31082, 11936, 11937, 11938, 11960, 11961, 11962 };
    static final int[] TIN_ROCKS = { 2094, 2311, 37304, 37305, 37306, 11186, 11187, 11188, 2095, 31077, 31078, 31079, 11933, 11934, 11935, 11959, 11958, 11957 };
    static final int[] BLURITE_ROCKS = {};
    static final int[] IRON_ROCKS = { 31071, 31072, 31073, 37309, 37307, 37308, 11954, 11955, 11956 };
    static final int[] SILVER_ROCKS = { 31074, 31075, 31076, 11948, 11949, 11950 };
    static final int[] COAL_ROCKS = { 31068, 31069, 31070, 11932, 11931, 11930 };
    static final int[] GOLD_ROCKS = { 31065, 31066, 31067, 37310, 37311, 37312, 11951, 11952, 11953 };
    static final int[] MITHRIL_ROCKS = { 31086, 31087, 31088, 11942, 11943, 11944 };
    static final int[] ADAMANT_ROCKS = { 31083, 31084, 31085, 11939, 11940, 11941 };
    static final int[] RUNITE_ROCKS = {};

    public static void initialize() {
        GameServer.getInstance().getMessageDispatcher().getObjectDispatcher().bind(new RockObjectHandler());
    }

    static int getDepletedId(int rockId) {
        if (rockId >= 2090 && rockId <= 2109) {
            return (rockId % 2 == 0) ? 450 : 452;
        } else if (rockId >= 31062 && rockId <= 31088) {
            return 31059 + (rockId % 3);
        } else if (rockId >= 11945 && rockId <= 11962) {
            switch (rockId % 3) {
            case 0:
                return 11556;
            case 1:
                return 11557;
            case 2:
                return 11555;
            }
        }
        return 450;
    }

}
