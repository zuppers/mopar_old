package net.scapeemulator.game.model.player;

import net.scapeemulator.cache.def.VarbitDefinition;
import net.scapeemulator.game.model.definition.VarbitDefinitions;
import net.scapeemulator.game.msg.impl.ConfigMessage;
import net.scapeemulator.game.msg.impl.VarbitMessage;

/**
 * @author Hadyn Richard
 */
public final class StateSet {

    private final Player player;
    private final int[] stateValues = new int[2500];

    public StateSet(Player player) {
        this.player = player;
    }

    private void check(int id) {
        if (id < 0 || id >= stateValues.length) {
            throw new ArrayIndexOutOfBoundsException(id);
        }
    }

    public void setState(int id, int value) {
        check(id);
        stateValues[id] = value;
        player.send(new ConfigMessage(id, value));
    }

    public int getState(int id) {
        check(id);
        return stateValues[id];
    }

    public void setBitState(int id, boolean bool) {
        setBitState(id, bool ? 1 : 0);
    }

    public void setBitState(int id, int value) {
        VarbitDefinition definition = VarbitDefinitions.forId(id);
        int lowBit = definition.getLowBit();
        int highBit = definition.getHighBit();
        int mask = (1 << highBit - lowBit + 1) - 1;
        stateValues[definition.getState()] = (value & mask) << definition.getLowBit() | getState(definition.getState()) & ~(mask << definition.getLowBit());
        player.send(new VarbitMessage(id, value));
    }

    public boolean isBitStateActive(int id) {
        return isBitStateActive(id, 0x1);
    }

    public boolean isBitStateActive(int id, int flag) {
        return (getBitState(id) & flag) != 0;
    }

    public int getBitState(int id) {
        VarbitDefinition definition = VarbitDefinitions.forId(id);

        int lowBit = definition.getLowBit();
        int highBit = definition.getHighBit();
        int mask = (1 << highBit - lowBit + 1) - 1;

        return getState(definition.getState()) >> definition.getLowBit() & mask;
    }
}
