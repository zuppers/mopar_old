package net.scapeemulator.game.model.player.appearance;


public class Wrists extends AppearanceFeature {
	
	private final static int[] MALE_WRIST_STYLES = {
		33, 34, 84, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126
	};
	
	private final static int[] FEMALE_WRIST_STYLES = {
		67, 68, 127, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168
	};
	
	Wrists(Gender gender) {
		super(gender == Gender.MALE ? MALE_WRIST_STYLES : FEMALE_WRIST_STYLES, null, 0, 0);
	}
	
}
