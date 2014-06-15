package net.scapeemulator.game.model.player.appearance;


public class Feet extends AppearanceFeature {
	
	private final static int[] FEET_COLORS = {
		6, 1, 2, 3, 4, 5
	};
	
	private final static int[] MALE_FEET_STYLES = { 42, 43 };
	
	private final static int[] FEMALE_FEET_STYLES = { 79, 80 };
	
	Feet(Gender gender) {
		super(gender == Gender.MALE ? MALE_FEET_STYLES : FEMALE_FEET_STYLES, FEET_COLORS, 0, 0);
	}
	
}
