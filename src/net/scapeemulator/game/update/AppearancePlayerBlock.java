package net.scapeemulator.game.update;

import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.EquipmentDefinition;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.appearance.Appearance;
import net.scapeemulator.game.model.player.appearance.Appearance.Feature;
import net.scapeemulator.game.model.player.appearance.Gender;
import net.scapeemulator.game.model.player.inventory.Inventory;
import net.scapeemulator.game.msg.impl.PlayerUpdateMessage;
import net.scapeemulator.game.net.game.DataTransformation;
import net.scapeemulator.game.net.game.DataType;
import net.scapeemulator.game.net.game.GameFrameBuilder;
import net.scapeemulator.util.Base37Utils;

public final class AppearancePlayerBlock extends PlayerBlock {

	private final String username;
	private final boolean hidden;
	private final Appearance appearance;
	private final Inventory equipment;
	private final int stance, combat, skill, pnpc;

	public AppearancePlayerBlock(Player player) {
		super(0x4);
		username = player.getUsername();
		hidden = player.isHidden();
		appearance = player.getAppearance();
		equipment = new Inventory(player.getEquipment());
		stance = player.getStance();
		combat = player.getSkillSet().getCombatLevel();
		skill = player.getSkillSet().getTotalLevel();
		pnpc = player.getPNPC();
	}

	@Override
	public void encode(PlayerUpdateMessage message, GameFrameBuilder builder) {
		Gender gender = appearance.getGender();

		GameFrameBuilder propertiesBuilder = new GameFrameBuilder(builder.getAllocator());

		/*
		 * flags field: bit 0 - gender (0 = male, 1 = female) bit 1 - unused bit 2 - show skill
		 * level instead of combat level bit 3-5 - unknown bit 6-7 - unknown
		 */
		int flags = gender.ordinal();
		propertiesBuilder.put(DataType.BYTE, flags);
		propertiesBuilder.put(DataType.BYTE, -1); // pk icon
		propertiesBuilder.put(DataType.BYTE, -1); // prayer icon
		
		if (hidden) {
			for (int i = 0; i < 12; i++) {
				propertiesBuilder.put(DataType.BYTE, 0);
			}
		} else {
			if (pnpc > -1) {
				propertiesBuilder.put(DataType.SHORT, -1);
				propertiesBuilder.put(DataType.SHORT, pnpc);
				propertiesBuilder.put(DataType.BYTE, 255);
			} else {
				Item item = equipment.get(Equipment.HEAD);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				item = equipment.get(Equipment.CAPE);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				item = equipment.get(Equipment.NECK);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				item = equipment.get(Equipment.WEAPON);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 + EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				item = equipment.get(Equipment.BODY);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.TORSO));
				}

				item = equipment.get(Equipment.SHIELD);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				boolean fullBody = false;
				item = equipment.get(Equipment.BODY);
				if (item != null)
					fullBody = EquipmentDefinition.forId(item.getId()).isFullBody();

				if (!fullBody) {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.ARMS));
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				item = equipment.get(Equipment.LEGS);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.LEGS));
				}

				boolean fullHelm = false, fullMask = false;
				item = equipment.get(Equipment.HEAD);
				if (item != null) {
					fullHelm = EquipmentDefinition.forId(item.getId()).isFullHelm();
					fullMask = EquipmentDefinition.forId(item.getId()).isFullMask();
				}
				if (!fullHelm) {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.HAIR));
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}

				item = equipment.get(Equipment.HANDS);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.WRISTS));
				}

				item = equipment.get(Equipment.FEET);
				if (item != null) {
					propertiesBuilder.put(DataType.SHORT, 0x8000 | EquipmentDefinition.forId(item.getId()).getEquipmentId());
				} else {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.FEET));
				}

				item = equipment.get(Equipment.HEAD); // TODO check
				if (gender == Gender.MALE && !fullMask) {
					propertiesBuilder.put(DataType.SHORT, 0x100 | appearance.getStyle(Feature.FACIAL_HAIR));
				} else {
					propertiesBuilder.put(DataType.BYTE, 0);
				}
			}
		}
		
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Feature.HAIR));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Feature.TORSO));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Feature.LEGS));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Feature.FEET));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Feature.SKIN));
		propertiesBuilder.put(DataType.SHORT, stance);
		propertiesBuilder.put(DataType.LONG, Base37Utils.encodeBase37(username));
		propertiesBuilder.put(DataType.BYTE, combat);
		if ((flags & 0x4) != 0) {
			propertiesBuilder.put(DataType.SHORT, skill);
		} else {
			propertiesBuilder.put(DataType.BYTE, 0);
			propertiesBuilder.put(DataType.BYTE, 0);
		}
		propertiesBuilder.put(DataType.BYTE, 0);
		/* if the above byte is non-zero, four unknown shorts are written */

		builder.put(DataType.BYTE, DataTransformation.ADD, propertiesBuilder.getLength());
		builder.putRawBuilder(propertiesBuilder);
	}

}
