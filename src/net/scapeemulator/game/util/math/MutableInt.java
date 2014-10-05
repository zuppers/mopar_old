package net.scapeemulator.game.util.math;

/**
 * A wrapper for integers that allows them to be mutated and passed by reference. Avoid using if
 * possible.
 * 
 * @author David Insley
 */
public class MutableInt {

    private int value;

    public MutableInt(int value) {
        this.value = value;
    }

    public void set(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
