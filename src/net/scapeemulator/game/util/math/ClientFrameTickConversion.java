package net.scapeemulator.game.util.math;

public class ClientFrameTickConversion {

    private static final int CLIENT_FPT = 30; // frames per server tick
        
    public static final int framesToTicks(double frames) {
        return (int) Math.round(frames / CLIENT_FPT);
    }
    
    public static final int ticksToFrames(int ticks) {
        return ticks * CLIENT_FPT;
    }
}
