package net.scapeemulator.game.model.player.appearance;


public class Torso extends AppearanceFeature {
	
	private final static int[] TORSO_COLORS = {
		24, 23, 2, 22, 12, 11, 6, 19, 4, 0, 9, 13, 25, 8, 15, 26, 21, 7, 20, 14, 10, 28, 27, 3, 5, 18, 17, 1, 16
	};
	
	private final static int[] MALE_TORSO_STYLES = {
		18, 19, 20, 21, 22, 23, 24, 25, 111, 112, 113, 114, 115, 116
	};
	
	private final static int[] FEMALE_TORSO_STYLES = {
		56, 57, 58, 59, 60, 153, 154, 155, 156, 157, 158
	};
	
	Torso(Gender gender) {
		super(gender == Gender.MALE ? MALE_TORSO_STYLES : FEMALE_TORSO_STYLES, TORSO_COLORS, 0, 0);
	}
	
}
