package net.scapeemulator.game.model.player;

import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackStyle;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.DelayedMagicHit;
import net.scapeemulator.game.model.mob.combat.DelayedRangeHit;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.EquipmentDefinition;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.EquipmentDefinition.WeaponClass;
import net.scapeemulator.game.model.player.skills.magic.AutoCastHandler;
import net.scapeemulator.game.model.player.skills.magic.CombatSpell;
import net.scapeemulator.game.model.player.skills.magic.EffectMobSpell;
import net.scapeemulator.game.model.player.skills.ranged.Arrow;
import net.scapeemulator.game.model.player.skills.ranged.Bolt;
import net.scapeemulator.game.model.player.skills.ranged.Bow;
import net.scapeemulator.game.model.player.skills.ranged.Crossbow;
import net.scapeemulator.game.msg.impl.inter.InterfaceTextMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;

public class PlayerCombatHandler extends CombatHandler<Player> {

	private Item weapon;
	private EquipmentDefinition weaponDefinition;
	private WeaponClass weaponClass;

	public PlayerCombatHandler(Player player) {
		super(player);
	}

	public boolean canAttack(Mob target) {
		if (!target.alive() || mob.actionsBlocked()) {
			return false;
		}
		return true;
	}

	public boolean attack() {
		// TODO hit chance / defense calculations etc
		if (combatDelay > 0) {
			return false;
		}
		if (nextSpell != null) {
			if (!nextSpell.getRequirements().hasRequirementsDisplayOne(mob)) {
				if (autoCast == nextSpell) {
					weaponChanged();
				}
				return true;
			}
			nextSpell.getRequirements().fulfillAll(mob);
			switch (nextSpell.getType()) {
			case COMBAT:
				CombatSpell cs = (CombatSpell) nextSpell;
				cs.cast(mob, target);
				int damage = (int) (Math.random() * (cs.getMaxHit() + 1));
				damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
				World.getWorld().getTaskScheduler().schedule(new DelayedMagicHit(mob, target, cs.getExplosionGraphic(), damage));
				break;
			case EFFECT_MOB:
				((EffectMobSpell) nextSpell).cast(mob, target);
				break;
			case ITEM:
			case TELEPORT:
				return true;
			}
			combatDelay = 8;
			nextSpell = autoCast;
		} else {
			if (attackType == AttackType.RANGE) {

				int damage = (int) (Math.random() * 10);
				damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
				Item ammo = mob.getEquipment().get(Equipment.AMMO);
				ammo = ammo == null ? new Item(-1, 0) : ammo;
				Bow bow = Bow.forId(weapon.getId());
				if (bow != null) {
					boolean dub = (bow == Bow.DARK_BOW);
					Arrow arrow = Arrow.forId(ammo.getId());
					if (!bow.validAmmo(arrow)) {
						mob.sendMessage("You do not have any valid arrows for your bow!");
						mob.stopAction();
						return true;
					}
					if (bow == Bow.CRYSTAL_BOW) {
						Arrow.CRYSTAL.createProjectile(mob, target, false);
						mob.playSpotAnimation(Arrow.CRYSTAL.getDrawbackGraphic(false));
					} else {
						if (dub && ammo.getAmount() < 2) {
							mob.sendMessage("You need at least two arrows to use the Dark Bow.");
							mob.stopAction();
							return true;
						}
						mob.getEquipment().remove(new Item(ammo.getId(), dub ? 2 : 1));
						arrow.createProjectile(mob, target, dub);
						mob.playSpotAnimation(arrow.getDrawbackGraphic(dub));
						World.getWorld().getTaskScheduler().schedule(new DelayedRangeHit(mob, target, damage, ammo.getId(), ((int) (Math.random() * 2) == 0) ? 1 : 0));
					}
					mob.playAnimation(weaponDefinition.getAnimation(attackStyle, attackType));
					combatDelay = weaponDefinition.getSpeed() - (attackStyle == AttackStyle.RAPID ? 1 : 0);
					return true;
				}
				Crossbow crossbow = Crossbow.forId(weapon.getId());
				if (crossbow != null) {
					Bolt bolt = Bolt.forId(ammo.getId());
					if (!crossbow.validAmmo(bolt)) {
						mob.sendMessage("You do not have any valid bolts for your crossbow!");
						mob.stopAction();
						return true;
					}
					bolt.createProjectile(mob, target);
					mob.getEquipment().remove(new Item(ammo.getId(), 1));
					World.getWorld().getTaskScheduler().schedule(new DelayedRangeHit(mob, target, damage, ammo.getId(), ((int) (Math.random() * 2) == 0) ? 1 : 0));
					mob.playAnimation(weaponDefinition.getAnimation(attackStyle, attackType));
					combatDelay = weaponDefinition.getSpeed() - (attackStyle == AttackStyle.RAPID ? 1 : 0);
					return true;
				}
				// TODO thrown weapons
			}
			mob.playAnimation(weaponDefinition.getAnimation(attackStyle, attackType));
			int damage = (int) (Math.random() * 10);
			damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
			hitTarget(damage);
			if (damage > 0) {
				target.playAnimation(target.getCombatHandler().getDefendAnimation());
			}
			combatDelay = weaponDefinition.getSpeed();
		}
		return true;
	}

	public boolean shouldRetaliate() {
		if (target == null && mob.getSettings().isAutoRetaliating() && noRetaliate < 1) {
			return true;
		}
		return false;
	}

	public Animation getDefendAnimation() {
		return weaponDefinition.getAnimation(attackStyle, attackType);
	}

	public int getAttackRange() {
		if (nextSpell != null) {
			return 8;
		}
		return weaponDefinition.getRange() + ((attackStyle == AttackStyle.DEFENSIVE && attackType == AttackType.RANGE) ? 1 : 0);
	}

	public void attackTabClick(int tabId, int childId) {
		if (tabId != weaponClass.getTabId()) {
			return;
		}
		if (tabId == WeaponClass.MAGIC_STAFF.getTabId()) {
			childId -= 1;
			if (childId == 3) {
				mob.sendMessage("Defensive autocasting has not been added yet. Sorry!");
				return;
			} else if (childId == 4) {
				AutoCastHandler.openSpellSelection(mob);
				return;
			} else if (childId <= 2) {
				if (autoCast != null) {
					mob.setInterfaceText(90, 11, "Spell");
					mob.send(new InterfaceVisibleMessage(90, 83, true));
					mob.send(new InterfaceVisibleMessage(90, autoCast.getAutoCastConfig(), false));
					autoCast = null;
				}
			}
		} else {
			childId -= 2;
		}
		if (childId < 4 && childId >= 0) {
			switch (tabId) {
			case 75:
			case 78:
				childId = (childId == 0 ? 0 : 4 - childId);
				break;
			case 76:
			case 77:
			case 79:
				childId = (childId == 0 ? 0 : 3 - childId);
				break;
			}
			AttackStyle newStyle = weaponClass.getAttackStyle(childId);
			if (newStyle != null) {
				attackStyle = newStyle;
				mob.getSettings().setAttackStyle(childId);
			}
		} else {
			mob.getSettings().toggleAutoRetaliating();
		}
	}

	public void init() {
		weapon = mob.getEquipment().get(Equipment.WEAPON);
		String name = "Unarmed";
		weaponClass = WeaponClass.UNARMED;
		combatDelay = 5;
		weaponDefinition = EquipmentDefinition.UNARMED;
		if (weapon != null) {
			name = weapon.getDefinition().getName();
			weaponDefinition = weapon.getEquipmentDefinition();
			weaponClass = weaponDefinition.getWeaponClass();
		}
		int tab = weaponClass.getTabId();
		mob.getInterfaceSet().openAttackTab(tab);
		mob.send(new InterfaceTextMessage(tab, 0, name));
		attackStyle = weaponClass.getAttackStyle(mob.getSettings().getAttackStyle());
		attackType = weaponClass.getAttackType(mob.getSettings().getAttackStyle());
		mob.getSettings().setAttackStyle(mob.getSettings().getAttackStyle());
	}

	public void restoreTab() {
		mob.getInterfaceSet().openAttackTab(weaponClass.getTabId());
		weapon = mob.getEquipment().get(Equipment.WEAPON);
		String name = "Unarmed";
		if (weapon != null) {
			name = weapon.getDefinition().getName();
		}
		mob.send(new InterfaceTextMessage(weaponClass.getTabId(), 0, name));
	}

	public void weaponChanged() {
		autoCast = null;
		nextSpell = null;
		weapon = mob.getEquipment().get(Equipment.WEAPON);
		String name = "Unarmed";
		weaponClass = WeaponClass.UNARMED;
		combatDelay = 5;
		if (weaponClass == WeaponClass.MAGIC_STAFF) {
			mob.setInterfaceText(90, 11, "Spell");
			mob.send(new InterfaceVisibleMessage(90, 83, true));
			mob.send(new InterfaceVisibleMessage(90, autoCast.getAutoCastConfig(), false));
		}
		weaponDefinition = EquipmentDefinition.UNARMED;
		if (weapon != null) {
			name = weapon.getDefinition().getName();
			weaponDefinition = weapon.getEquipmentDefinition();
			weaponClass = weaponDefinition.getWeaponClass();
			combatDelay = weaponDefinition.getSpeed();
		}
		int tab = weaponClass.getTabId();
		mob.getInterfaceSet().openAttackTab(tab);
		mob.send(new InterfaceTextMessage(tab, 0, name));
		if (attackStyle != null) {
			for (int i = 0; i < 4; i++) { // Check for an exact match first
				if (weaponClass.getAttackStyle(i) == attackStyle && weaponClass.getAttackType(i) == attackType) {
					attackStyle = weaponClass.getAttackStyle(i);
					attackType = weaponClass.getAttackType(i);
					mob.getSettings().setAttackStyle(i);
					return;
				}
			}
			for (int i = 0; i < 4; i++) { // Check for at least an XP type match
				if (weaponClass.getAttackStyle(i) == null) {
					continue;
				}
				if (weaponClass.getAttackStyle(i) == attackStyle) {
					attackStyle = weaponClass.getAttackStyle(i);
					attackType = weaponClass.getAttackType(i);
					mob.getSettings().setAttackStyle(i);
					return;
				}
			}
		}
		// No match, just go for the default style
		attackStyle = weaponClass.getAttackStyle(0);
		attackType = weaponClass.getAttackType(0);
		mob.getSettings().setAttackStyle(0);
	}

	@Override
	public int getMaxHit() {
		return 0;
	}

}
