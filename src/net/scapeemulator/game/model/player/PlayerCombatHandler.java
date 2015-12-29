package net.scapeemulator.game.model.player;

import net.scapeemulator.game.GameServer;
import net.scapeemulator.game.dispatcher.button.ButtonDispatcher;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.mob.Animation;
import net.scapeemulator.game.model.mob.Mob;
import net.scapeemulator.game.model.mob.combat.AttackStyle;
import net.scapeemulator.game.model.mob.combat.AttackType;
import net.scapeemulator.game.model.mob.combat.CombatHandler;
import net.scapeemulator.game.model.mob.combat.DelayedRangeHit;
import net.scapeemulator.game.model.mob.combat.Hits.Hit;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.EquipmentDefinition;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.EquipmentDefinition.WeaponClass;
import net.scapeemulator.game.model.player.interfaces.CombatTabHandler;
import net.scapeemulator.game.model.player.skills.Skill;
import net.scapeemulator.game.model.player.skills.magic.AutoCastHandler;
import net.scapeemulator.game.model.player.skills.magic.DamageSpell;
import net.scapeemulator.game.model.player.skills.magic.EffectMobSpell;
import net.scapeemulator.game.model.player.skills.magic.SpellType;
import net.scapeemulator.game.model.player.skills.prayer.HeadIcon;
import net.scapeemulator.game.model.player.skills.ranged.Arrow;
import net.scapeemulator.game.model.player.skills.ranged.Bolt;
import net.scapeemulator.game.model.player.skills.ranged.Bow;
import net.scapeemulator.game.model.player.skills.ranged.Crossbow;
import net.scapeemulator.game.model.player.skills.ranged.Thrown;
import net.scapeemulator.game.msg.impl.CreateProjectileMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceTextMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage;

public class PlayerCombatHandler extends CombatHandler<Player> {

    static {
        ButtonDispatcher.getInstance().bind(new CombatTabHandler());
    }

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

        { // TODO - if not multicombat
            Hit hit = mob.getHits().getMostRecentCombatHit();

            if (hit != null) {
                if (hit.getSource() != target) {
                    if (GameServer.getInstance().getTickTimestamp() - hit.getTimestamp() <= 8) {
                        mob.sendMessage("You are already under attack!");
                        return false;
                    }
                }
            }

            hit = target.getHits().getMostRecentCombatHit();
            if (hit != null) {
                if (hit.getSource() != mob) {
                    if (GameServer.getInstance().getTickTimestamp() - hit.getTimestamp() <= 8) {
                        mob.sendMessage("Someone is already fighting that.");
                        return false;
                    }
                }
            }
        } // TODO end if not multicombat

        return true;
    }

    public boolean attack() {
        if (combatDelay > 0) {
            return false;
        }

        int damage = 0;
        boolean shouldHit = shouldHit();
        switch (getNextAttackType()) {
            case MAGIC:
                if (!nextSpell.getRequirements().hasRequirementsDisplayOne(mob)) {
                    if (autoCast == nextSpell) {
                        // We don't have the requirements, cancel our auto cast and switch back to melee styles
                        weaponChanged();
                    }
                    return true;
                }

                // Removes runes, gives base XP
                nextSpell.getRequirements().fulfillAll(mob);

                if (nextSpell.getType() == SpellType.DAMAGE) {
                    if (shouldHit) {
                        damage = 1 + (int) (Math.random() * ((DamageSpell) nextSpell).getMaxHit());
                        if (target.getHeadIcon() == HeadIcon.MAGIC) {
                            damage *= 0.6;
                        }
                        if (damage > target.getCurrentHitpoints()) {
                            damage = target.getCurrentHitpoints();
                        }
                        nextSpell.cast(mob, target, damage);
                        addMagicExperience(damage);
                    } else {
                        nextSpell.cast(mob, target, 0);
                    }

                } else if (nextSpell.getType() == SpellType.EFFECT_MOB) {
                    ((EffectMobSpell) nextSpell).cast(mob, target, shouldHit);
                }
                combatDelay = 5;
                nextSpell = autoCast;
                return true;
            case RANGE:
                // Calculate damage dealt
                if (shouldHit) {
                    damage = !shouldHit ? 0 : 1 + (int) (Math.random() * getRangeMaxHit());
                    damage = target.getHeadIcon() == HeadIcon.RANGED ? damage *= 0.6 : damage;
                    damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
                }

                // Find ammo; if null set id to -1 for crystal arrow data
                Item ammo = mob.getEquipment().get(Equipment.AMMO) != null ? mob.getEquipment().get(Equipment.AMMO) : new Item(-1, 0);

                // Check if using bow
                Bow bow = Bow.forId(weapon.getId());
                if (bow != null) {
                    Arrow arrow = Arrow.forId(ammo.getId());
                    if (!bow.validAmmo(arrow)) {
                        mob.sendMessage("You do not have any valid arrows for your bow!");
                        mob.stopAction();
                        return true;
                    }
                    if (bow == Bow.CRYSTAL_BOW) {
                        mob.playSpotAnimation(Arrow.CRYSTAL.getDrawbackGraphic(false));
                        CreateProjectileMessage cpm = Arrow.CRYSTAL.createProjectile(mob, target);
                        World.getWorld().createGlobalProjectile(mob.getPosition(), cpm);
                        World.getWorld().getTaskScheduler().schedule(new DelayedRangeHit(mob, target, cpm.getDurationTicks(), damage, 0, 0));
                    } else {
                        boolean twoArrows = (bow == Bow.DARK_BOW);
                        if (twoArrows && ammo.getAmount() < 2) {
                            mob.sendMessage("You need at least two arrows to use the Dark Bow.");
                            mob.stopAction();
                            return true;
                        }
                        mob.getEquipment().remove(new Item(ammo.getId(), twoArrows ? 2 : 1));
                        mob.playSpotAnimation(arrow.getDrawbackGraphic(twoArrows));
                        CreateProjectileMessage cpm = arrow.createProjectile(mob, target);
                        World.getWorld().createGlobalProjectile(mob.getPosition(), cpm);
                        boolean shouldDrop = (int) (Math.random() * 2) == 0;
                        World.getWorld().getTaskScheduler()
                                .schedule(new DelayedRangeHit(mob, target, cpm.getDurationTicks(), damage, ammo.getId(), shouldDrop ? 1 : 0));
                    }
                    mob.playAnimation(weaponDefinition.getAnimation(attackStyle, getRawAttackType()));
                    combatDelay = weaponDefinition.getSpeed() - (attackStyle == AttackStyle.RAPID ? 1 : 0);
                    addRangeExperience(damage);
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
                    CreateProjectileMessage cpm = bolt.createProjectile(mob, target);
                    World.getWorld().createGlobalProjectile(mob.getPosition(), cpm);
                    boolean shouldDrop = (int) (Math.random() * 2) == 0;
                    World.getWorld().getTaskScheduler()
                            .schedule(new DelayedRangeHit(mob, target, cpm.getDurationTicks(), damage, ammo.getId(), shouldDrop ? 1 : 0));
                    mob.getEquipment().remove(new Item(ammo.getId(), 1));
                    mob.playAnimation(weaponDefinition.getAnimation(attackStyle, getRawAttackType()));
                    combatDelay = weaponDefinition.getSpeed() - (attackStyle == AttackStyle.RAPID ? 1 : 0);
                    addRangeExperience(damage);
                    return true;
                }
                Thrown thrown = Thrown.forId(weapon.getId());
                if (thrown != null) {

                }
                break;
            default:
                mob.playAnimation(weaponDefinition.getAnimation(attackStyle, getRawAttackType()));
                damage = !shouldHit ? 0 : 1 + (int) (Math.random() * getMeleeMaxHit());
                damage = target.getHeadIcon() == HeadIcon.MELEE ? damage *= 0.6 : damage;
                damage = damage > target.getCurrentHitpoints() ? target.getCurrentHitpoints() : damage;
                hitTarget(damage);
                if (damage > 0) {
                    target.playAnimation(target.getCombatHandler().getDefendAnimation());
                    addMeleeExperience(damage);
                }
                combatDelay = weaponDefinition.getSpeed();
                break;

        }
        return true;
    }

    private void addMeleeExperience(int damage) {
        double xp = damage * 1.33;
        mob.getSkillSet().addExperience(Skill.HITPOINTS, xp);
        switch (attackStyle) {
            case ACCURATE:
                mob.getSkillSet().addExperience(Skill.ATTACK, damage * 4);
                return;
            case AGGRESSIVE:
                mob.getSkillSet().addExperience(Skill.STRENGTH, damage * 4);
                return;
            case DEFENSIVE:
                mob.getSkillSet().addExperience(Skill.DEFENCE, damage * 4);
                return;
            case SHARED:
                mob.getSkillSet().addExperience(Skill.DEFENCE, xp);
                mob.getSkillSet().addExperience(Skill.ATTACK, xp);
                mob.getSkillSet().addExperience(Skill.STRENGTH, xp);
                return;
            default: // Should never happen...
                return;
        }
    }

    private void addRangeExperience(int damage) {
        double xp = damage * 1.33;
        mob.getSkillSet().addExperience(Skill.HITPOINTS, xp);
        switch (attackStyle) {
            case ACCURATE:
            case RAPID:
                mob.getSkillSet().addExperience(Skill.RANGED, damage * 4);
                return;
            case DEFENSIVE:
                mob.getSkillSet().addExperience(Skill.RANGED, damage * 2);
                mob.getSkillSet().addExperience(Skill.DEFENCE, damage * 2);
                return;
            default: // Should never happen...
                return;
        }
    }

    private void addMagicExperience(int damage) {
        mob.getSkillSet().addExperience(Skill.HITPOINTS, damage * 1.33);
        mob.getSkillSet().addExperience(Skill.MAGIC, damage * 2);
    }

    @Override
    public boolean shouldRetaliate() {
        if (target == null && mob.getSettings().isAutoRetaliating() && noRetaliate < 1) {
            return true;
        }
        return false;
    }

    @Override
    public Animation getDefendAnimation() {
        return weaponDefinition.getAnimation(attackStyle, getRawAttackType());
    }

    @Override
    public int getAttackRange() {
        switch (getNextAttackType()) {
            case MAGIC:
                return 8;
            case RANGE:
                return weaponDefinition.getRange() + (attackStyle == AttackStyle.DEFENSIVE ? 1 : 0);
            default:
                return weaponDefinition.getRange();
        }
    }

    @Override
    public int attackRoll() {
        int level = 0;
        int equipmentBonus = mob.getCombatBonuses().getAttackBonus(getNextAttackType());

        switch (getNextAttackType()) {
            case MAGIC:
                level = mob.getSkillSet().getCurrentLevel(Skill.MAGIC);
                level *= mob.getPrayers().getPrayerBonus(Skill.MAGIC);
                break;
            case RANGE:
                level = mob.getSkillSet().getCurrentLevel(Skill.RANGED);
                level *= mob.getPrayers().getPrayerBonus(Skill.RANGED);
                if (attackStyle == AttackStyle.ACCURATE) {
                    level += 3;
                }
                break;
            case SLASH:
            case CRUSH:
            case STAB:
                level = mob.getSkillSet().getCurrentLevel(Skill.ATTACK);
                level *= mob.getPrayers().getPrayerBonus(Skill.ATTACK);
                if (attackStyle == AttackStyle.ACCURATE) {
                    level += 3;
                } else if (attackStyle == AttackStyle.SHARED) {
                    level += 1;
                }
                break;

        }
        level += 8;
        level *= 1.0; // TODO void bonus
        return level * (64 + equipmentBonus);
    }

    @Override
    public int defenceRoll(AttackType other) {
        int level = mob.getSkillSet().getCurrentLevel(Skill.DEFENCE);
        level *= mob.getPrayers().getPrayerBonus(Skill.DEFENCE);
        if (attackStyle == AttackStyle.DEFENSIVE) {
            level += 3;
        } else if (attackStyle == AttackStyle.SHARED) {
            level += 1;
        }
        level += 8;

        if (other == AttackType.MAGIC) {
            level *= 0.3;
            level += mob.getSkillSet().getCurrentLevel(Skill.MAGIC) * 0.7;
        }
        return level * (64 + mob.getCombatBonuses().getDefenceBonus(other));
    }

    /**
     * Calculates the players maximum melee hit taking into account active prayers, equipment strength bonus, strength level (including boosts from
     * potions), special attacks, attack style, and extra bonuses such as a full void melee set.
     * 
     * @return the players flat maximum hit, not randomized
     */
    private int getMeleeMaxHit() {
        int strLevel = mob.getSkillSet().getCurrentLevel(Skill.STRENGTH);
        strLevel *= mob.getPrayers().getPrayerBonus(Skill.STRENGTH);
        if (attackStyle == AttackStyle.AGGRESSIVE) {
            strLevel += 3;
        } else if (attackStyle == AttackStyle.SHARED) {
            strLevel += 1;
        }
        strLevel += 8;
        strLevel *= 1.0; // TODO other bonus (slayer helm 1.15, void 1.2)

        int damage = (int) (0.5 + ((strLevel * (64 + mob.getCombatBonuses().getStrengthBonus())) / 640.0));
        damage *= 1.0; // TODO special attack bonuses
        return damage;
    }

    /**
     * Calculates the players maximum range hit taking into account active prayers, equipment range strength bonus, range level (including boosts from
     * potions), special attacks and bolts, attack style, and extra bonuses such as the full void ranger set.
     * 
     * @return the players flat maximum hit, not randomized
     */
    private int getRangeMaxHit() {
        int level = mob.getSkillSet().getCurrentLevel(Skill.RANGED);
        level *= mob.getPrayers().getPrayerBonus(Skill.RANGED);
        if (attackStyle == AttackStyle.ACCURATE) {
            level += 3;
        }
        level += 8;
        level *= 1.0; // TODO other bonus (slayer helm 1.15, void 1.2)

        int damage = (int) (0.5 + ((level * (64 + mob.getCombatBonuses().getRangeStrengthBonus())) / 640.0));
        damage *= 1.0; // TODO special attack bonuses
        return damage;
    }

    /**
     * Handles a player click onto the attack tab for things such as style switching, auto retaliating, and auto casting. The tabId parameter is cross
     * checked with players active tab to avoid packet editing.
     * 
     * @param tabId the attack tab the player clicked on
     * @param childId the button the player clicked on
     */
    public void attackTabClick(int tabId, int childId) {
        if (tabId != weaponClass.getTabId()) {
            return;
        }
        if (tabId == WeaponClass.MAGIC_STAFF.getTabId()) {
            childId -= 1;
            if (childId == 3) {
                mob.sendMessage("Defensive autocasting has not been enabled.");
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
                setRawAttackType(weaponClass.getAttackType(childId));
                mob.getSettings().setAttackStyle(childId);
            }
        } else {
            mob.getSettings().toggleAutoRetaliating();
        }
    }

    /**
     * Only called at login, initiates the attack tab and attack styles.
     */
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
        setRawAttackType(weaponClass.getAttackType(mob.getSettings().getAttackStyle()));
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

    /**
     * Called whenever the players weapon changes, reassigns our weapon variables and attack style variables. Searches for style match to avoid
     * accidental XP gain switching.
     */
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

            // Check for an exact match first
            for (int i = 0; i < 4; i++) {
                if (weaponClass.getAttackStyle(i) == attackStyle && weaponClass.getAttackType(i) == getRawAttackType()) {
                    attackStyle = weaponClass.getAttackStyle(i);
                    setRawAttackType(weaponClass.getAttackType(i));
                    mob.getSettings().setAttackStyle(i);
                    return;
                }
            }

            // Check for at least an XP type match
            for (int i = 0; i < 4; i++) {
                if (weaponClass.getAttackStyle(i) == attackStyle) {
                    attackStyle = weaponClass.getAttackStyle(i);
                    setRawAttackType(weaponClass.getAttackType(i));
                    mob.getSettings().setAttackStyle(i);
                    return;
                }
            }
        }

        // No match, just go for the default style
        attackStyle = weaponClass.getAttackStyle(0);
        setRawAttackType(weaponClass.getAttackType(0));
        mob.getSettings().setAttackStyle(0);
    }

}
