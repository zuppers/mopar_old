package net.scapeemulator.game.model.player;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.combat.AttackStyle;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.mob.combat.CombatBonuses;
import net.scapeemulator.game.model.player.requirement.Requirements;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EquipmentDefinition {

	public enum WeaponClass {
		AXE(75, "ACCURATE_SLASH", "AGGRESSIVE_SLASH", "AGGRESSIVE_CRUSH", "DEFENSIVE_SLASH"),
		HAMMER(76, "ACCURATE_CRUSH", "AGGRESSIVE_CRUSH", "DEFENSIVE_CRUSH"),
		BOW(77, "ACCURATE_RANGE", "RAPID_RANGE", "DEFENSIVE_RANGE"),
		CLAWS(78, "ACCURATE_SLASH", "AGGRESSIVE_SLASH", "SHARED_STAB", "DEFENSIVE_SLASH"),
		LONGBOW(79, "ACCURATE_RANGE", "RAPID_RANGE", "DEFENSIVE_RANGE"),
		FIXED_DEVICE(80),
		SWORD(81, "ACCURATE_SLASH", "AGGRESSIVE_SLASH", "SHARED_STAB", "DEFENSIVE_SLASH"),
		TWO_H_SWORD(82, "ACCURATE_SLASH", "AGGRESSIVE_SLASH", "AGGRESSIVE_CRUSH", "DEFENSIVE_SLASH"),
		PICKAXE(83, "ACCURATE_STAB", "AGGRESSIVE_STAB", "AGGRESSIVE_CRUSH", "DEFENSIVE_STAB"),
		HALBERD(84, "SHARED_STAB", "AGGRESSIVE_SLASH", "DEFENSIVE_STAB"),
		STAFF(85, "ACCURATE_CRUSH", "AGGRESSIVE_CRUSH", "DEFENSIVE_CRUSH"),
		SCYTHE(86, "ACCURATE_SLASH", "AGGRESSIVE_STAB", "AGGRESSIVE_CRUSH", "DEFENSIVE_SLASH"),
		SPEAR(87, "SHARED_STAB", "SHARED_SLASH", "SHARED_CRUSH", "DEFENSIVE_STAB"),
		MACE(88, "ACCURATE_CRUSH", "AGGRESSIVE_CRUSH", "SHARED_STAB", "DEFENSIVE_CRUSH"),
		DAGGER(89, "ACCURATE_STAB", "AGGRESSIVE_STAB", "AGGRESSIVE_SLASH", "DEFENSIVE_STAB"),
		MAGIC_STAFF(90, "ACCURATE_CRUSH", "AGGRESSIVE_CRUSH", "DEFENSIVE_CRUSH"),
		THROWN(91, "ACCURATE_RANGE", "RAPID_RANGE", "DEFENSIVE_RANGE"),
		UNARMED(92, "ACCURATE_CRUSH", "AGGRESSIVE_CRUSH", "DEFENSIVE_CRUSH"),
		WHIP(93, "ACCURATE_SLASH", "SHARED_SLASH", "DEFENSIVE_SLASH");
		//473, 474 chin/lizard
		
		private int tabId;
		private AttackStyle[] styles;
		private AttackType[] types;
		
		private WeaponClass(int tabId, String ... attackInfo) {
			this.tabId = tabId;
			styles = new AttackStyle[attackInfo.length];
			types = new AttackType[attackInfo.length];
			for(int i = 0; i < attackInfo.length; i++) {
				String[] info = attackInfo[i].split("_");	
				styles[i] = AttackStyle.valueOf(info[0]);
				types[i] = AttackType.valueOf(info[1]);
			}
		}
		
		public int getTabId() {
			return tabId;
		}
		
		public AttackStyle getAttackStyle(int index) {
			if(index < 0 || index >= styles.length) {
				return null;
			}
			return styles[index];
		}
		
		public AttackType getAttackType(int index) {
			if(index < 0 || index >= types.length) {
				return null;
			}
			return types[index];
		}
	}

	public static final int FLAG_TWO_HANDED = 0x1;
	public static final int FLAG_FULL_HELM = 0x2;
	public static final int FLAG_FULL_MASK = 0x4;
	public static final int FLAG_FULL_BODY = 0x8;
		
	private static final Logger logger = LoggerFactory.getLogger(EquipmentDefinition.class);
	private static final Map<Integer, EquipmentDefinition> definitions = new HashMap<>();

	public static void init() throws IOException {
		try (DataInputStream reader = new DataInputStream(new FileInputStream("data/game/equipment.dat"))) {
			int id;
			while ((id = reader.readShort()) != -1) {
				int flags = reader.read() & 0xFF;
				int slot = reader.read() & 0xFF;
				int equipId = reader.readShort() & 0xFFFF;
				int[] bonusValues = new int[13];
				for(int i = 0; i < 13; i++) {
					bonusValues[i] = reader.readShort();
				}
				int stance = 0, weaponClass = 0, speed = 0, range = 0;
				
				int[] animations = new int[5];
				if (slot == Equipment.WEAPON) {
					stance = reader.readShort() & 0xFFFF;
					weaponClass = reader.read() & 0xFF;
					speed = reader.read() & 0xFF;
					range = reader.read() & 0xFF;
					for(int i = 0; i < animations.length; i++) {
						animations[i] = reader.readShort() & 0xFFFF;
					}
				}
				EquipmentDefinition equipment = new EquipmentDefinition();
				equipment.equipmentId = equipId;
				equipment.slot = slot;
				equipment.twoHanded = (flags & FLAG_TWO_HANDED) != 0;
				equipment.fullHelm = (flags & FLAG_FULL_HELM) != 0;
				equipment.fullMask = (flags & FLAG_FULL_MASK) != 0;
				equipment.fullBody = (flags & FLAG_FULL_BODY) != 0;
				
				CombatBonuses bonuses = new CombatBonuses();
				bonuses.setAttackBonus(AttackType.STAB, bonusValues[0]);
				bonuses.setAttackBonus(AttackType.SLASH, bonusValues[1]);
				bonuses.setAttackBonus(AttackType.CRUSH, bonusValues[2]);
				bonuses.setAttackBonus(AttackType.MAGIC, bonusValues[3]);
				bonuses.setAttackBonus(AttackType.RANGE, bonusValues[4]);
				bonuses.setDefenceBonus(AttackType.STAB, bonusValues[5]);
				bonuses.setDefenceBonus(AttackType.SLASH, bonusValues[6]);
				bonuses.setDefenceBonus(AttackType.CRUSH, bonusValues[7]);
				bonuses.setDefenceBonus(AttackType.MAGIC, bonusValues[8]);
				bonuses.setDefenceBonus(AttackType.RANGE, bonusValues[9]);
				bonuses.setStrengthBonus(bonusValues[10]);
				bonuses.setPrayerBonus(bonusValues[11]);
				bonuses.setRangeStrengthBonus(bonusValues[12]);
				equipment.bonuses = bonuses;
				
				if (slot == Equipment.WEAPON) {
					equipment.stance = stance;
					equipment.weaponClass = WeaponClass.values()[weaponClass];
					equipment.speed = speed;
					equipment.range = range;
					equipment.animations = animations;
				}
				int skill = 0;
				while ((skill = reader.read()) != 255) {
					equipment.requirements.addRequirement(new SkillRequirement(skill, reader.read() & 0xFF, false, slot == Equipment.WEAPON ? "wield that" : "wear that"));
				}
				definitions.put(id, equipment);
			}

			logger.info("Loaded " + definitions.size() + " equipment definitions.");
		}
	}

	public static EquipmentDefinition forId(int id) {
		return definitions.get(id);
	}

	public static EquipmentDefinition UNARMED = new EquipmentDefinition();
	
	static {
		UNARMED.slot = Equipment.WEAPON;
		UNARMED.weaponClass = WeaponClass.UNARMED;
		UNARMED.speed = 4;
		UNARMED.range = 1;
		UNARMED.animations = new int[]{422, 423, 422, -1, 424};
	}
	
	
	private int id, equipmentId, slot, stance, speed, range;
	private int[] animations;
	private boolean fullBody, fullMask, fullHelm, twoHanded;
	private CombatBonuses bonuses;
	private WeaponClass weaponClass;
	private Requirements requirements = new Requirements();

	public int getId() {
		return id;
	}

	public int getEquipmentId() {
		return equipmentId;
	}

	public int getSlot() {
		return slot;
	}

	public Requirements getRequirements() {
		return requirements;
	}
	
	public boolean isFullBody() {
		if (slot != Equipment.BODY)
			throw new IllegalStateException("Invalid body item: " + id);

		return fullBody;
	}

	public boolean isFullMask() {
		if (slot != Equipment.HEAD)
			throw new IllegalStateException("Invalid head item: " + id);

		return fullMask;
	}

	public boolean isFullHelm() {
		if (slot != Equipment.HEAD)
			throw new IllegalStateException("Invalid head item: " + id);

		return fullHelm;
	}

	public boolean isTwoHanded() {
		if (slot != Equipment.WEAPON)
			throw new IllegalStateException("Invalid weapon: " + id);

		return twoHanded;
	}

	public int getStance() {
		if (slot != Equipment.WEAPON)
			throw new IllegalStateException("Invalid weapon: " + id);

		return stance;
	}
	
	public int getSpeed() {
		if (slot != Equipment.WEAPON)
			throw new IllegalStateException("Invalid weapon: " + id);

		return speed;
	}
	
	public int getRange() {
		if (slot != Equipment.WEAPON)
			throw new IllegalStateException("Invalid weapon: " + id);

		return range;
	}
	
	public CombatBonuses getBonuses() {
		return bonuses;
	}
	
	public WeaponClass getWeaponClass() {
		if (slot != Equipment.WEAPON)
			throw new IllegalStateException("Invalid weapon: " + id);

		return weaponClass;
	}
	
	public Animation getAnimation(AttackStyle style, AttackType type) {
		if (slot != Equipment.WEAPON)
			throw new IllegalStateException("Invalid weapon: " + id);

		for(int i = 0; i < weaponClass.styles.length; i++) {
			if(weaponClass.styles[i] == style && weaponClass.types[i] == type) {
				return new Animation(animations[i]);
			}
		}
		return null;
	}
}
