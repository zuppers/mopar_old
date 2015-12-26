package net.scapeemulator.game.model.player.skills.slayer;

import java.util.Random;

public enum SlayerTask {

    /* @formatter:off */
    SPIDERS(1, 40, 70),
    BIRDS(2, 40, 70),
    ZOMBIES(3, 40, 80);
    /* @formatter:on */

    private static final Random RANDOM = new Random();
    private final int taskId;
    private final int min;
    private final int max;
    private final int[] npcIds;

    private SlayerTask(int taskId, int min, int max, int... npcIds) {
        this.taskId = taskId;
        this.min = min;
        this.max = max;
        this.npcIds = npcIds;
    }

    public static SlayerTask forTaskId(int taskId) {
        for (SlayerTask task : values()) {
            if (task.taskId == taskId) {
                return task;
            }
        }
        return null;
    }

    public int getTaskId() {
        return taskId;
    }

    public int randomAmount() {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public boolean contains(int npcId) {
        for (int npc : npcIds) {
            if (npc == npcId) {
                return true;
            }
        }
        return false;
    }
}
