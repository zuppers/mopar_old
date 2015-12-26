package net.scapeemulator.game.model.player.skills.slayer;

import net.scapeemulator.game.model.mob.combat.MobKillListener;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;

public class SlayerKillListener implements MobKillListener<Player, NPC> {

    @Override
    public void mobKilled(Player killer, NPC killed) {
        SlayerTask task = Slayer.getTask(killer);
        if (task != null && task.contains(killed.getType())) {
            Slayer.decrementTaskAmount(killer);
            if(Slayer.getTaskAmount(killer) == 0) {
                Slayer.resetTask(killer);
                Slayer.incrementCompletedTasks(killer);
                killer.sendMessage("You have completed your task. Return to a slayer master for a new one.");
                // TODO if completed points quest (smoking kills)
                int completedTasks = Slayer.getCompletedTasks(killer);
                int points = Slayer.getTaskMaster(killer).getRewardPoints(completedTasks);
                killer.sendMessage("You have now completed " + completedTasks + " tasks and earned " + points + " reward points.");
            }
        }

    }

}
