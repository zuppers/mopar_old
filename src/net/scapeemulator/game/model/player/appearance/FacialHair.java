package net.scapeemulator.game.model.player.appearance;


public class FacialHair extends AppearanceFeature {
		
	private final static int[] MALE_FACIAL_HAIR_STYLES = {
		10, 11, 12, 13, 14, 15, 16, 17, 98, 99, 100, 101, 102, 103, 104,
		305, 306, 307, 308
	};
	
	FacialHair(Gender gender) {
		super(gender == Gender.MALE ? MALE_FACIAL_HAIR_STYLES : null, null, 0, 0);
	}
	
}
