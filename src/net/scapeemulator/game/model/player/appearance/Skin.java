package net.scapeemulator.game.model.player.appearance;

public class Skin extends AppearanceFeature {
	
	public final static int[] SKIN_COLORS = {
		0, 1, 2, 3, 4, 5, 6, 7
	};
	
	Skin(Gender gender) {
		super(null, SKIN_COLORS, -1, 0);
	}
	
}
