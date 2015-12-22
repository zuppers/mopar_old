package net.scapeemulator.game.model.player.skills.magic;

import static net.scapeemulator.game.model.player.skills.magic.Rune.*;
import net.scapeemulator.game.model.area.QuadArea;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.requirement.EquipmentRequirement;
import net.scapeemulator.game.model.player.requirement.RuneRequirement;
import net.scapeemulator.game.model.player.requirement.SkillRequirement;
import net.scapeemulator.game.model.player.skills.Skill;

public class Spellbook {

    public static final Spellbook NORMAL_SPELLBOOK = new Spellbook(Magic.NORMAL_SPELLBOOK_ID);
    public static final Spellbook ANCIENT_SPELLBOOK = new Spellbook(Magic.ANCIENT_SPELLBOOK_ID);
    public static final Spellbook LUNAR_SPELLBOOK = new Spellbook(Magic.LUNAR_SPELLBOOK_ID);

    private final int interfaceId;
    private Spell[] spells = new Spell[100];

    private Spellbook(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Spell getSpell(int childId) {
        if (childId < 0 || childId >= spells.length) {
            return null;
        }
        return spells[childId];
    }

    // pls dont judge me, i will change this
    public static void loadBooks() {
        CombatSpell cs = null;

        cs = new DamageSpell("Wind Strike", 45, 2, 711, 90);
        cs.setProjectileInformation(91, 92, 45, 31, 64);
        cs.getRequirements().addRequirements(level(1, 5.5), rune(AIR), rune(MIND));
        NORMAL_SPELLBOOK.spells[1] = cs;

        cs = new DamageSpell("Water Strike", 47, 4, 711, 93);
        cs.setProjectileInformation(94, 95, 45, 31, 64);
        cs.getRequirements().addRequirements(level(5, 7.5), rune(WATER), rune(AIR), rune(MIND));
        NORMAL_SPELLBOOK.spells[4] = cs;

        cs = new DamageSpell("Earth Strike", 49, 6, 711, 96);
        cs.setProjectileInformation(97, 98, 45, 31, 64);
        cs.getRequirements().addRequirements(level(9, 9.5), rune(EARTH, 2), rune(AIR), rune(MIND));
        NORMAL_SPELLBOOK.spells[6] = cs;

        cs = new DamageSpell("Fire Strike", 51, 8, 711, 99);
        cs.setProjectileInformation(100, 101, 45, 31, 64);
        cs.getRequirements().addRequirements(level(13, 11.5), rune(FIRE, 3), rune(AIR, 2), rune(MIND));
        NORMAL_SPELLBOOK.spells[8] = cs;

        cs = new DamageSpell("Wind Bolt", 53, 9, 711, 117);
        cs.setProjectileInformation(118, 119, 45, 31, 64);
        cs.getRequirements().addRequirements(level(17, 13.5), rune(AIR, 2), rune(CHAOS));
        NORMAL_SPELLBOOK.spells[10] = cs;

        cs = new DamageSpell("Water Bolt", 55, 10, 711, 120);
        cs.setProjectileInformation(121, 122, 45, 31, 64);
        cs.getRequirements().addRequirements(level(23, 16.5), rune(WATER, 2), rune(AIR, 2), rune(CHAOS));
        NORMAL_SPELLBOOK.spells[14] = cs;

        cs = new DamageSpell("Earth Bolt", 57, 11, 711, 123);
        cs.setProjectileInformation(124, 125, 45, 31, 64);
        cs.getRequirements().addRequirements(level(29, 19.5), rune(EARTH, 3), rune(AIR, 2), rune(CHAOS));
        NORMAL_SPELLBOOK.spells[17] = cs;

        cs = new DamageSpell("Fire Bolt", 59, 12, 711, 126);
        cs.setProjectileInformation(127, 128, 45, 31, 64);
        cs.getRequirements().addRequirements(level(35, 21.5), rune(FIRE, 4), rune(AIR, 3), rune(CHAOS));
        NORMAL_SPELLBOOK.spells[20] = cs;

        cs = new DamageSpell("Wind Blast", 61, 13, 711, 132);
        cs.setProjectileInformation(133, 134, 45, 31, 64);
        cs.getRequirements().addRequirements(level(41, 25.5), rune(AIR, 3), rune(DEATH));
        NORMAL_SPELLBOOK.spells[24] = cs;

        cs = new DamageSpell("Water Blast", 63, 14, 711, 135);
        cs.setProjectileInformation(136, 137, 45, 31, 64);
        cs.getRequirements().addRequirements(level(47, 28.5), rune(WATER, 3), rune(AIR, 3), rune(DEATH));
        NORMAL_SPELLBOOK.spells[27] = cs;

        cs = new DamageSpell("Earth Blast", 65, 15, 711, 138);
        cs.setProjectileInformation(139, 140, 45, 31, 64);
        cs.getRequirements().addRequirements(level(53, 31.5), rune(EARTH, 4), rune(AIR, 3), rune(DEATH));
        NORMAL_SPELLBOOK.spells[33] = cs;

        cs = new DamageSpell("Fire Blast", 67, 16, 711, 129);
        cs.setProjectileInformation(130, 131, 45, 31, 64);
        cs.getRequirements().addRequirements(level(59, 34.5), rune(FIRE, 5), rune(AIR, 4), rune(DEATH));
        NORMAL_SPELLBOOK.spells[38] = cs;

        cs = new DamageSpell("Wind Wave", 69, 17, 727, 158);
        cs.setProjectileInformation(159, 160, 45, 31, 32);
        cs.getRequirements().addRequirements(level(62, 36), rune(AIR, 5), rune(BLOOD));
        NORMAL_SPELLBOOK.spells[45] = cs;

        cs = new DamageSpell("Water Wave", 71, 18, 727, 161);
        cs.setProjectileInformation(162, 163, 45, 31, 32);
        cs.getRequirements().addRequirements(level(65, 37.5), rune(WATER, 7), rune(AIR, 5), rune(BLOOD));
        NORMAL_SPELLBOOK.spells[48] = cs;

        cs = new DamageSpell("Earth Wave", 73, 19, 727, 164);
        cs.setProjectileInformation(165, 166, 45, 31, 32);
        cs.getRequirements().addRequirements(level(70, 40), rune(EARTH, 7), rune(AIR, 5), rune(BLOOD));
        NORMAL_SPELLBOOK.spells[52] = cs;

        cs = new DamageSpell("Fire Wave", 75, 20, 727, 155);
        cs.setProjectileInformation(156, 157, 45, 31, 32);
        cs.getRequirements().addRequirements(level(75, 42.5), rune(FIRE, 7), rune(AIR, 5), rune(BLOOD));
        NORMAL_SPELLBOOK.spells[55] = cs;

        cs = new DamageSpell("Crumble Undead", 77, 15, 724, 145);
        cs.setProjectileInformation(146, 147, 45, 31, 64);
        cs.getRequirements().addRequirements(level(39, 24.5), rune(EARTH, 2), rune(AIR, 2), rune(CHAOS));
        NORMAL_SPELLBOOK.spells[22] = cs;

        cs = new DamageSpell("Slayer Dart", 79, 10, 1576, -1);
        cs.setProjectileInformation(328, 329, 45, 31, 64);
        cs.getRequirements().addRequirement(new EquipmentRequirement(Equipment.WEAPON, "You must equip the Slayer's Staff to cast that spell.", 4170));
        cs.getRequirements().addRequirements(level(50, 30), rune(DEATH), rune(MIND, 4));
        NORMAL_SPELLBOOK.spells[31] = cs;

        cs = new DamageSpell("Claws of Guthix", 81, 20, 710, 177);
        cs.setProjectileInformation(178, 179, 45, 31, 64);
        cs.getRequirements().addRequirement(new EquipmentRequirement(Equipment.WEAPON, "You must equip a Guthix Staff or Void Mace to cast that spell.", 2416, 8841));
        cs.getRequirements().addRequirements(level(60, 35), rune(FIRE), rune(BLOOD, 2), rune(AIR, 4));
        NORMAL_SPELLBOOK.spells[42] = cs;

        TeleportSpell ts = null;

        // Home teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(3220, 3218, 3225, 3219), 0);
        ts.getRequirements().addRequirements(/* TODO not in combat, time since last use 30m */);
        NORMAL_SPELLBOOK.spells[0] = ts;

        // Varrock teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(3209, 3422, 3218, 3425), 0);
        ts.getRequirements().addRequirements(level(25, 35.0), rune(FIRE), rune(AIR, 3), rune(LAW));
        NORMAL_SPELLBOOK.spells[15] = ts;

        // Lumbridge teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(3220, 3218, 3225, 3219), 0);
        ts.getRequirements().addRequirements(level(31, 41.0), rune(EARTH), rune(AIR, 3), rune(LAW));
        NORMAL_SPELLBOOK.spells[18] = ts;

        // Falador teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(2962, 3378, 2968, 3380), 0);
        ts.getRequirements().addRequirements(level(37, 48.0), rune(WATER), rune(AIR, 3), rune(LAW));
        NORMAL_SPELLBOOK.spells[21] = ts;

        // House teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(2954, 3221, 2957, 3226), 0);
        ts.getRequirements().addRequirements(level(40, 30.0), rune(LAW), rune(AIR), rune(EARTH));
        NORMAL_SPELLBOOK.spells[23] = ts;

        // Camelot teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(2756, 3475, 2758, 3480), 0);
        ts.getRequirements().addRequirements(level(45, 55.5), rune(AIR, 5), rune(LAW));
        NORMAL_SPELLBOOK.spells[26] = ts;

        // Ardougne teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(2658, 3305, 2661, 3309), 0);
        // TODO plague city requirement
        ts.getRequirements().addRequirements(level(51, 61.0), rune(WATER, 2), rune(LAW, 2));
        NORMAL_SPELLBOOK.spells[32] = ts;

        // Watchtower teleport
        ts = new TeleportSpell(TeleportType.STANDARD, new QuadArea(2928, 4717, 2933, 4718), 2);
        // TODO watchtower requirement
        ts.getRequirements().addRequirements(level(58, 68.0), rune(EARTH, 2), rune(LAW, 2));
        NORMAL_SPELLBOOK.spells[37] = ts;

        JewleryEnchantmentSpell jes = null;
        
        //jes = new JewleryEnchantmentSpell();
        //jes.getRequirements().addRequirements(level(7), rune(WATER), rune(COSMIC));
        //NORMAL_SPELLBOOK.spells[5] = jes;
        
        // Trollheim teleport TODO get coords
        // ts = new TeleportSpell(TeleportType.STANDARD, 68.0, new QuadArea(2928, 4717, 2933, 4718),
        // 2);
        // TODO Eadgars ruse requirement
        // ts.getRequirements().addRequirements(level(61), rune(FIRE, 2), rune(LAW, 2));
        // NORMAL_SPELLBOOK.spells[44] = ts;

        // Ape teleport TODO get coords
        // ts = new TeleportSpell(TeleportType.STANDARD, 74.0, new QuadArea(2928, 4717, 2933, 4718),
        // 2);
        // TODO RFD requirement
        // TODO banana requirement
        // ts.getRequirements().addRequirements(level(64), rune(FIRE, 2), rune(WATER, 2), rune(LAW,
        // 2));
        // NORMAL_SPELLBOOK.spells[44] = ts;
    }

    private static RuneRequirement rune(Rune rune) {
        return rune(rune, 1);
    }

    private static RuneRequirement rune(Rune rune, int amount) {
        return new RuneRequirement(rune, amount);
    }

    private static SkillRequirement level(int level) {
        return level(level, 0);
    }
    
    private static SkillRequirement level(int level, double xp) {
        return new SkillRequirement(Skill.MAGIC, level, true, "cast that spell", xp);
    }
    
    public int getInterfaceId() {
        return interfaceId;
    }

}
