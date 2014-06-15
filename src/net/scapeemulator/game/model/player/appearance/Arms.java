package net.scapeemulator.game.model.player.appearance;


public class Arms extends AppearanceFeature {
	
	public final static int[] MALE_ARM_STYLES = {
		26, 27, 28, 29, 30, 31, 105, 106, 107, 108, 109, 110
	};
	
	public final static int[] FEMALE_ARM_STYLES = {
		61, 62, 63, 64, 65, 147, 148, 149, 150, 151, 152
	};
	
	Arms(Gender gender) {
		super(gender == Gender.MALE ? MALE_ARM_STYLES : FEMALE_ARM_STYLES, null, 0, 0);
	}
	
}
