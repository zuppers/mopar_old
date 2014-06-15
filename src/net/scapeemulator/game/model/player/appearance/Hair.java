package net.scapeemulator.game.model.player.appearance;


public class Hair extends AppearanceFeature {
	
	private final static int[] HAIR_COLORS = {
		20 /*burgundy*/,      19 /*red*/,    10 /*vermillion*/, 18 /*pink*/,       4  /*orange*/,
		5  /*yello*/ ,        15 /*peach*/,  7  /*brown*/,      0  /*dark brown*/, 6  /*light brown*/,
		21 /*mint green*/,    9  /*green*/,  22 /*dark green*/, 17 /*dark blue*/,  8  /*turquoise*/,
		16 /*cyan*/,          11 /*purple*/, 24 /*viloet*/,     23 /*indigo*/,     3  /*dark grey*/,
		2  /*military gray*/, 1  /*white*/,  14 /*light gray*/, 13 /*taupe*/,      12 /*black*/,
	};
	
	private final static int[] MALE_HAIR_STYLES = {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 91, 92, 93, 94, 95, 96, 97, 261, 262, 263, 264, 265, 266, 267, 268
	};
	
	private final static int[] FEMALE_HAIR_STYLES = {
		45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 
		146, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280
	};
	
	Hair(Gender gender) {
		super(gender == Gender.MALE ? MALE_HAIR_STYLES : FEMALE_HAIR_STYLES, HAIR_COLORS, 1, 0);
	}
	
}
