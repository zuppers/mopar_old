package net.scapeemulator.game.model.player.appearance;

import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.dialogue.HeadAnimation;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.inter.InterfaceAnimationMessage;
import net.scapeemulator.game.msg.impl.inter.InterfacePlayerHeadMessage;


public class Appearance {
	
	public enum Feature {
		HAIR, FACIAL_HAIR, TORSO, ARMS, WRISTS, LEGS, FEET, SKIN
	}
	
	private final Player player;
	private Map<Feature, AppearanceFeature> features = new HashMap<Feature, AppearanceFeature>();
	private Gender gender;
	
	static final int APPEARANCE_INTERFACE = 771;

	public Appearance(Player player) {
		this.player = player;
		gender = Gender.MALE;
		reset();
	}

	public void reset() {
		features.put(Feature.HAIR, new Hair(gender));
		features.put(Feature.FACIAL_HAIR, new FacialHair(gender));
		features.put(Feature.TORSO, new Torso(gender));
		features.put(Feature.ARMS, new Arms(gender));
		features.put(Feature.WRISTS, new Wrists(gender));
		features.put(Feature.LEGS, new Legs(gender));
		features.put(Feature.FEET, new Feet(gender));
		features.put(Feature.SKIN, new Skin(gender));
	}
	
	public AppearanceFeature getFeature(Feature feature) {
		return features.get(feature);
	}
	
	public int getColor(Feature feature) {
		return features.get(feature).getColor();
	}
	
	public int getColorIndex(Feature feature) {
		return features.get(feature).getColorIndex();
	}
	
	public void setColorIndex(Feature feature, int index) {
		features.get(feature).setColorIndex(index);
	}
	
	public int getStyle(Feature feature) {
		return features.get(feature).getStyle();
	}
	
	public int getStyleIndex(Feature feature) {
		return features.get(feature).getStyleIndex();
	}
	
	public void setStyleIndex(Feature feature, int index) {
		features.get(feature).setStyleIndex(index);
	}
	
	public void showAppearanceInterface() {
		 player.send(new InterfaceAnimationMessage(APPEARANCE_INTERFACE, 79, HeadAnimation.CALM.getAnimationId()));
		 player.send(new InterfacePlayerHeadMessage(APPEARANCE_INTERFACE, 79));
		 player.getInterfaceSet().openWindow(APPEARANCE_INTERFACE);
	}
	
	public void handle(int child) {
		if(child == 92) {
			getFeature(Feature.HAIR).modifyStyle(-1);
		} else if(child == 93) {
			getFeature(Feature.HAIR).modifyStyle(1);
		} else if(child > 99 && child < 125) {
			setColorIndex(Feature.HAIR, child - 100);
		} else if(child > 150 && child < 159) {			
			setColorIndex(Feature.SKIN, child - 151);
		} else if(child > 188 && child < 218) {
			setColorIndex(Feature.TORSO, child - 189);
		} else if(child > 247 && child < 277) {
			setColorIndex(Feature.LEGS, child - 248);
		} else if(child > 306 && child < 313) {
			setColorIndex(Feature.FEET, child - 307);
		} else if(child == 92) {
			//setHairStyleIndex(hairStyleIndex);
		} else if(child == 341) {
			getFeature(Feature.TORSO).modifyStyle(-1);
		} else if(child == 342) {
			getFeature(Feature.TORSO).modifyStyle(1);
		} else if(child == 49) {
			gender = Gender.MALE;
			reset();
		} else if(child == 52) {
			gender = Gender.FEMALE;
			reset();
		} else {
			return;
		}
		player.appearanceUpdated();
	}
		
	public Gender getGender() {
		return gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
		
}
