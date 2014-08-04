package net.scapeemulator.game.model.player;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Handles player instance timers. Some timers may be accessed through other classes, such as the
 * combat delay timer in the combat handler class.
 * 
 * @author David Insley
 */
public class PlayerTimers {

    public enum Timer {
        EAT, DRINK, FROZEN
    }

    private final Map<Timer, Integer> timers;

    PlayerTimers() {
        timers = new EnumMap<Timer, Integer>(Timer.class);
    }

    public boolean timerActive(Timer type) {
        return timers.containsKey(type);
    }

    public int getTimer(Timer type) {
        if (timers.containsKey(type)) {
            return timers.get(type);
        }
        return 0;
    }

    /**
     * Sets the time for the specified timer type. A value less than 0 will keep the timer active
     * until resetTimer is called for the same type.
     * 
     * @param type timer type to set the time for
     * @param time int value of timer, in ticks
     */
    public void setTimer(Timer type, int time) {
        if (time < 0) {
            timers.put(type, -1);
        } else if (time == 0) {
            return;
        } else {
            timers.put(type, time);
        }
    }

    /**
     * Resets a timer, removing it from the active timers collection no matter what the current
     * remaining time may be.
     * 
     * @param type timer type to reset
     */
    public void resetTimer(Timer type) {
        timers.remove(type);
    }

    public void tick() {
        Iterator<Entry<Timer, Integer>> it = timers.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Timer, Integer> timer = it.next();
            int time = timer.getValue();
            if (time == -1) {
                continue;
            } else if (time == 1) {
                it.remove();
            } else {
                timer.setValue(time - 1);
            }
        }
    }
}
