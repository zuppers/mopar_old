package net.scapeemulator.game.util.math;

/**
 * Class to handle some commonly used math methods.
 * 
 * @author David Insley
 */
public class BasicMath {

    /**
     * Adds two integers together and compares it against Integer.MAX_VALUE
     * 
     * @param i1 first integer
     * @param i2 second integer
     * @return the amount of overflow, or 0 if the two integers can be added with no overflow
     */
    public static int integerOverflow(int i1, int i2) {
        if (i1 < 0 || i2 < 0) {
            throw new IllegalArgumentException("checking for overflow with negative numbers");
        }
        long l = (long) i1 + i2;
        if (l <= Integer.MAX_VALUE) {
            return 0;
        }
        return (int) (l - Integer.MAX_VALUE);
    }

}
