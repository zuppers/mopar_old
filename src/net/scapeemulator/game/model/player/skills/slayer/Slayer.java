package net.scapeemulator.game.model.player.skills.slayer;

import net.scapeemulator.game.model.mob.combat.MobKillListeners;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.PlayerVariables.Variable;

public class Slayer {

    public static void initialize() {
        MobKillListeners.addListener(new SlayerKillListener());
    }

    private static int getTaskInfo(Player player) {
        return player.getVariables().getVar(Variable.SLAYER_TASK);
    }

    private static int getSlayerInfo(Player player) {
        return player.getVariables().getVar(Variable.SLAYER_INFO);

    }
    
    public static SlayerTask getTask(Player player) {
        int taskInfo = getTaskInfo(player);
        return SlayerTask.forTaskId((taskInfo >> 4) & 0xFF);
    }

    public static void resetTask(Player player) {
        player.getVariables().setVar(Variable.SLAYER_TASK, 0);
    }

    public static void setTask(Player player, SlayerMaster master, SlayerTask task, int amount) {
        int taskInfo = master.getMasterId() & 0xF;
        taskInfo |= (task.getTaskId() & 0xFF) << 4;
        taskInfo |= (amount & 0xFF) << 12;
        player.getVariables().setVar(Variable.SLAYER_TASK, taskInfo);
    }

    public static SlayerMaster getTaskMaster(Player player) {
        int taskInfo = getTaskInfo(player);
        return SlayerMaster.forMasterId(taskInfo & 0xF);
    }

    public static int getTaskAmount(Player player) {
        int taskInfo = getTaskInfo(player);
        return (taskInfo >> 12) & 0xFF;
    }

    public static void decrementTaskAmount(Player player) {
        int taskInfo = getTaskInfo(player);
        taskInfo -= 1 << 12;
        player.getVariables().setVar(Variable.SLAYER_TASK, taskInfo);
    }

    // TODO lower
    public static int getCompletedTasks(Player player) {
        int taskInfo = getTaskInfo(player);
        return taskInfo >> 16;
    }

    public static void incrementCompletedTasks(Player player) {
        int taskInfo = getSlayerInfo(player);
        taskInfo += 1 << 16;
        player.getVariables().setVar(Variable.SLAYER_INFO, taskInfo);
    }

}
