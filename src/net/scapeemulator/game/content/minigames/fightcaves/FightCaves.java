package net.scapeemulator.game.content.minigames.fightcaves;

public class FightCaves {

    private static int[][] waves;

    public static void init() {
        int monsterCount = CaveEnemy.values().length;
        int waveCount = (1 << monsterCount) - 1;
        waves = new int[waveCount][monsterCount];
        for (int wave = 1; wave < waveCount + 1; wave++) {
            int points = wave;
            for (int i = monsterCount - 1; i >= 0; i--) {
                int cost = (1 << (i + 1)) - 1;
                int count = (points / cost) % 3;
                waves[wave - 1][i] = count;
                points -= cost * count;
            }
        }
    }

}
