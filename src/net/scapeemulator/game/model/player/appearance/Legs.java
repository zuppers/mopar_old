package net.scapeemulator.game.model.player.appearance;


public class Legs extends AppearanceFeature {
	
	private final static int[] LEG_COLORS = {
		26, 24, 23, 3, 22, 13, 12, 7, 19, 5, 1, 10, 14, 25, 9, 0, 21, 8, 20, 15, 11, 28, 27, 4, 6, 18, 17, 2, 16
	};
	
	private final static int[] MALE_LEG_STYLES = {
		36, 37, 38, 39, 40, 85, 86, 87, 88, 89, 90
	};
	
	private final static int[] FEMALE_LEG_STYLES = {
		70, 71, 72, 73, 74, 75, 76, 77, 128, 129, 130, 131, 132, 133, 134
	};
	
	Legs(Gender gender) {
		super(gender == Gender.MALE ? MALE_LEG_STYLES : FEMALE_LEG_STYLES, LEG_COLORS, 0, 0);
	}
	
}
