package net.scapeemulator.game.model.player.skills.slayer;

import java.util.Random;

/**
 * @author David Insley
 */
public enum SlayerMaster {

    /* @formatter:off */
    TURAEL(1, 8273, 3, 1, new int[]{0, 0, 0}),
    MAZCHNA(2, 8274, 20, 1, new int[]{2, 10, 30}), 
    VANNAKA(3, 1597, 40, 1, new int[]{4, 20, 60}),
    CHAELDAR(4, 1598, 70, 1, new int[]{10, 50, 150}),
    SUMONA(5, 7780, 3, 35, new int[]{12, 60, 180}),
    DURADEL(6, 8275, 100, 50, new int[]{15, 75, 225});
    /* @formatter:on */

    private static final Random RANDOM = new Random();
    private final int masterId;
    private final int npcId;
    private final int cbReq;
    private final int slayerReq;
    private final int[] points;
    private final SlayerTask[] tasks;

    private SlayerMaster(int masterId, int npcId, int cbReq, int slayerReq, int[] points, SlayerTask... tasks) {
        if (points.length != 3) {
            throw new IllegalArgumentException("Points are for every 1, 10, and 50 tasks.");
        }
        this.masterId = masterId;
        this.npcId = npcId;
        this.cbReq = cbReq;
        this.slayerReq = slayerReq;
        this.points = points;
        this.tasks = tasks;
    }

    public static SlayerMaster forMasterId(int masterId) {
        for (SlayerMaster master : values()) {
            if (master.masterId == masterId) {
                return master;
            }
        }
        return null;
    }

    public static SlayerMaster forNPCId(int npcId) {
        for (SlayerMaster master : values()) {
            if (master.npcId == npcId) {
                return master;
            }
        }
        return null;
    }

    public int getMasterId() {
        return masterId;
    }

    public int getCombatRequirement() {
        return cbReq;
    }

    public int getSlayerRequirement() {
        return slayerReq;
    }

    public SlayerTask randomTask() {
        return tasks[RANDOM.nextInt(tasks.length)];
    }

    public int getRewardPoints(int completedTasks) {
        if (completedTasks % 50 == 0) {
            return points[2];
        }
        if (completedTasks % 10 == 0) {
            return points[1];
        }
        return points[0];
    }

}
