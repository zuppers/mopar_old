package net.scapeemulator.game.model.mob.combat;

import net.scapeemulator.game.model.mob.Mob;

public interface MobKillListener<T extends Mob, S extends Mob> {

    public void mobKilled(T killer, S killed);

}