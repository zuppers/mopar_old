package net.scapeemulator.game.model.player.skills.farming;

public enum FarmingCycle {

    FIVE(5),
    TEN(10),
    TWENTY(20),
    FORTY(40),
    EIGHTY(80),
    ONE_SIXTY(160),
    THREE_TWENTY(320);

    private final int minutes;

    private FarmingCycle(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }

}
