package net.scapeemulator.game.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.FileStore;
import net.scapeemulator.cache.def.ItemDefinition;
import net.scapeemulator.game.model.definition.ItemDefinitions;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.EquipmentDefinition;
import net.scapeemulator.game.model.player.EquipmentDefinition.WeaponClass;
import net.scapeemulator.game.model.player.skills.Skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EquipmentDumper {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentDumper.class);
    private static final String[] TWO_HANDED_WEAPONS = { "giant's hand", "longbow", "scythe", "shortbow", "2h", "saradomin sword", "godsword", "claws", "guthan", "verac", "karil", "dharok",
            "halberd", "maul", "seercul", "crystal bow", "dark bow", "tzhaar-ket-om", "lizard", "salamander" };

    public static void main(String[] args) throws IOException {

        Cache cache = new Cache(FileStore.open("data/game/cache"));
        ItemDefinitions.init(cache);
        int[] equipIds = new int[ItemDefinitions.count()];
        int[][] equipmentBonuses = new int[ItemDefinitions.count()][12];
        logger.info("Loading equip ids...");
        try (BufferedReader reader = new BufferedReader(new FileReader("data/game/dumps/equipids"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split("\t");
                    int id = Integer.parseInt(data[0]);
                    int eqId = Integer.parseInt(data[1]);
                    if (eqId >= 0)
                        equipIds[id] = eqId;
                } catch (Exception e) {
                }
            }
        }
        logger.info("Loading bonuses...");
        try (BufferedReader reader = new BufferedReader(new FileReader("data/game/dumps/bonuses"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split("\t");
                    int id = Integer.parseInt(data[0]);
                    for (int i = 0; i < 12; i++) {
                        equipmentBonuses[id][i] = Integer.parseInt(data[i + 6]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("Dumping equipment data...");
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream("data/game/equipment.dat"))) {
            for (int id = 0; id < ItemDefinitions.count(); id++) {
                ItemDefinition def = ItemDefinitions.forId(id);
                if (def != null) {
                    if (isEquipment(id, def)) {
                        output.writeShort(id);
                        int flags = 0;
                        int slot = getSlot(id, def);
                        if (isTwoHanded(def))
                            flags |= EquipmentDefinition.FLAG_TWO_HANDED;
                        if (isFullHelm(id, def))
                            flags |= EquipmentDefinition.FLAG_FULL_HELM;
                        if (isFullMask(id, def))
                            flags |= EquipmentDefinition.FLAG_FULL_MASK;
                        if (isFullBody(id, def))
                            flags |= EquipmentDefinition.FLAG_FULL_BODY;
                        output.writeByte(flags);
                        output.writeByte(slot);
                        output.writeShort(equipIds[id]);
                        for (int i = 0; i < 12; i++) {
                            output.writeShort(equipmentBonuses[id][i]);
                        }
                        output.writeShort(getRangeStrengthBonus(id));
                        if (slot == Equipment.WEAPON) {
                            output.writeShort(getStance(id, def));
                            output.writeByte(getWeaponClass(def).ordinal());
                            output.writeByte(getSpeed(id, def));
                            output.writeByte(getRange(id, def));
                            for (int i = 0; i < 4; i++) {
                                output.writeShort(getAttackAnimation(id, def, i));
                            }
                            output.writeShort(getDefendAnimation(id, def));
                        }
                        for (int i = 0; i < Skill.SKILL_NAMES.length; i++) {
                            int req = getEquipRequirement(i, id, def, slot);
                            if (req > 1) {
                                output.writeByte(i);
                                output.writeByte(req);
                            }
                        }
                        output.writeByte(-1);
                    }
                }
            }
            output.writeShort(-1);
        }

        logger.info("Successfully dumped equipment data.");
    }

    private static boolean isEquipment(int id, ItemDefinition definition) {
        return definition.getMaleWearModel1() >= 0 || getSlot(id, definition) == Equipment.AMMO;
    }

    private static int getSlot(int id, ItemDefinition definition) {
        switch (id) {
        case 546: // Shade robe top
        case 7390: // Wizard robe (g) top
        case 10822: // Yak body
            return Equipment.BODY;
        case 548: // Shade robe bottom
        case 7398: // Enchanted bottom
        case 10340: // 3rd age robe bottom
        case 10824: // Yak legs
            return Equipment.LEGS;
        }
        if (definition.getName() == null)
            return Equipment.WEAPON;
        String name = definition.getName().toLowerCase();

        if (name.contains("claws"))
            return Equipment.WEAPON;
        if (name.contains("sword"))
            return Equipment.WEAPON;
        if (name.contains("dagger"))
            return Equipment.WEAPON;
        if (name.contains("mace"))
            return Equipment.WEAPON;
        if (name.contains("whip"))
            return Equipment.WEAPON;
        if (name.contains("bow"))
            return Equipment.WEAPON;
        if (name.contains("staff"))
            return Equipment.WEAPON;
        if (name.contains("dart"))
            return Equipment.WEAPON;

        if (name.contains("glove"))
            return Equipment.HANDS;
        if (name.contains("vamb"))
            return Equipment.HANDS;
        if (name.contains("gaunt"))
            return Equipment.HANDS;
        if (name.contains("bracelet"))
            return Equipment.HANDS;
        if (name.contains("crab claw"))
            return Equipment.HANDS;
        if (name.contains("hook"))
            return Equipment.HANDS;

        if (name.contains(" ring"))
            return Equipment.RING;
        if (name.contains("ring "))
            return Equipment.RING;

        if (name.contains("amulet"))
            return Equipment.NECK;
        if (name.contains("necklace"))
            return Equipment.NECK;
        if (name.contains("scarf"))
            return Equipment.NECK;
        if (name.contains("stole"))
            return Equipment.NECK;
        if (name.contains("pendant"))
            return Equipment.NECK;
        if (name.contains("symbol"))
            return Equipment.NECK;
        if (name.contains("void seal"))
            return Equipment.NECK;
        if (name.contains("logo"))
            return Equipment.NECK;

        if (name.contains("leg"))
            return Equipment.LEGS;
        if (name.contains("bottom"))
            return Equipment.LEGS;
        if (name.contains("skirt"))
            return Equipment.LEGS;
        if (name.contains("chaps"))
            return Equipment.LEGS;
        if (name.contains("tassets"))
            return Equipment.LEGS;
        if (name.contains("trousers"))
            return Equipment.LEGS;
        if (name.contains("cuisse"))
            return Equipment.LEGS;

        if (name.contains("body"))
            return Equipment.BODY;
        if (name.contains("top"))
            return Equipment.BODY;
        if (name.contains("chest"))
            return Equipment.BODY;
        if (name.contains("apron"))
            return Equipment.BODY;
        if (name.contains("blouse"))
            return Equipment.BODY;
        if (name.contains("brassard"))
            return Equipment.BODY;
        if (name.contains("hauberk"))
            return Equipment.BODY;
        if (name.contains("torso"))
            return Equipment.BODY;
        if (name.contains("shirt"))
            return Equipment.BODY;
        if (name.contains("tunic"))
            return Equipment.BODY;

        if (name.contains("arrow"))
            return Equipment.AMMO;
        if (name.contains("bolt"))
            return Equipment.AMMO;

        if (name.contains("shield"))
            return Equipment.SHIELD;
        if (name.contains("defender"))
            return Equipment.SHIELD;
        if (name.contains("book"))
            return Equipment.SHIELD;
        if (name.contains("toktz-ket"))
            return Equipment.SHIELD;

        if (name.contains("ring"))
            return Equipment.RING;

        if (name.contains("cape"))
            return Equipment.CAPE;
        if (name.contains("cloak"))
            return Equipment.CAPE;
        if (name.contains("ava's"))
            return Equipment.CAPE;
        if (name.contains("bonesack"))
            return Equipment.CAPE;
        if (name.contains("diving apparatus"))
            return Equipment.CAPE;

        if (name.contains("boot"))
            return Equipment.FEET;
        if (name.contains("sandal"))
            return Equipment.FEET;
        if (name.contains("shoe"))
            return Equipment.FEET;
        if (name.contains("flipper"))
            return Equipment.FEET;

        if (name.contains("hat"))
            return Equipment.HEAD;
        if (name.contains("helm"))
            return Equipment.HEAD;
        if (name.contains("mask"))
            return Equipment.HEAD;
        if (name.contains("hood"))
            return Equipment.HEAD;
        if (name.contains("coif"))
            return Equipment.HEAD;
        if (name.contains(" wig"))
            return Equipment.HEAD;
        if (name.contains("tiara"))
            return Equipment.HEAD;
        if (name.contains("mitre"))
            return Equipment.HEAD;
        if (name.contains("head"))
            return Equipment.HEAD;
        if (name.contains("eyepatch"))
            return Equipment.HEAD;
        if (name.contains("beret"))
            return Equipment.HEAD;
        if (name.contains("ears"))
            return Equipment.HEAD;
        if (name.contains("cavalier"))
            return Equipment.HEAD;
        if (name.contains("boater"))
            return Equipment.HEAD;
        if (name.contains("sallet"))
            return Equipment.HEAD;
        if (name.contains("sleeping cap"))
            return Equipment.HEAD;

        return Equipment.WEAPON;
    }

    private static boolean isTwoHanded(ItemDefinition def) {
        if (def.getName() == null)
            return false;
        String name = def.getName().toLowerCase();
        for (String s : TWO_HANDED_WEAPONS) {
            if (name.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFullBody(int id, ItemDefinition def) {
        if (def.getName() == null)
            return false;

        if (getSlot(id, def) != Equipment.BODY)
            return false;

        String name = def.getName().toLowerCase();
        if (name.contains("platebody"))
            return true;
        if (name.contains("robe"))
            return true;
        if (name.contains("chestplate"))
            return true;
        if (name.contains("clown shirt"))
            return true;
        if (id == 10824)
            return true;
        return false;
    }

    private static boolean isFullHelm(int id, ItemDefinition def) {
        if (def.getName() == null)
            return false;

        if (getSlot(id, def) != Equipment.HEAD)
            return false;

        String name = def.getName().toLowerCase();
        if (name.contains("full"))
            return true;
        if (name.contains("clown hat"))
            return true;
        if (name.contains("coif"))
            return true;
        return false;
    }

    private static boolean isFullMask(int id, ItemDefinition def) {
        if (def.getName() == null)
            return false;

        if (getSlot(id, def) != Equipment.HEAD)
            return false;

        String name = def.getName().toLowerCase();
        if (name.contains("full"))
            return true;
        return false;
    }

    private static WeaponClass getWeaponClass(ItemDefinition def) {
        if (def.getName() == null)
            return WeaponClass.SWORD;

        String name = def.getName().toLowerCase();
        if (name.contains("scythe"))
            return WeaponClass.SCYTHE;
        if (name.contains("pickaxe"))
            return WeaponClass.PICKAXE;
        if (name.contains("axe"))
            return WeaponClass.AXE;
        if (name.contains("godsword"))
            return WeaponClass.TWO_H_SWORD;
        if (name.contains("2h"))
            return WeaponClass.TWO_H_SWORD;
        if (name.contains("claws"))
            return WeaponClass.CLAWS;
        if (name.contains("longsword"))
            return WeaponClass.SWORD;
        if (name.contains("scimitar"))
            return WeaponClass.SWORD;
        if (name.contains("sword"))
            return WeaponClass.SWORD;
        if (name.contains("dagger"))
            return WeaponClass.DAGGER;
        if (name.contains("mace"))
            return WeaponClass.MACE;
        if (name.contains("maul"))
            return WeaponClass.HAMMER;
        if (name.contains("hammer"))
            return WeaponClass.HAMMER;
        if (name.contains("whip"))
            return WeaponClass.WHIP;
        if (name.contains("longbow"))
            return WeaponClass.LONGBOW;
        if (name.contains("crossbow"))
            return WeaponClass.LONGBOW;
        if (name.contains("bow"))
            return WeaponClass.BOW;
        if (name.contains("staff"))
            return WeaponClass.MAGIC_STAFF;
        if (name.contains("spear"))
            return WeaponClass.SPEAR;
        if (name.contains("dart"))
            return WeaponClass.THROWN;
        if (name.contains("salamander") || name.contains("lizard")) {
            // return WeaponClass.
        }

        return WeaponClass.SWORD;
    }

    private static int getStance(int id, ItemDefinition def) {
        if (def.getName() == null)
            return 1426;

        String name = def.getName().toLowerCase();
        if (name.contains("lizard") || name.contains("salamander"))
            return 5246;
        if (name.contains("scimitar") || name.contains("longsword") || name.contains(" sword"))
            return 1381;
        if (name.contains("whip"))
            return 620;
        if (name.contains("2h") || name.contains("godsword"))
            return 7047;
        if (name.contains("maul"))
            return 27;

        return 1426;
    }

    private static int getSpeed(int id, ItemDefinition def) {
        switch (id) {
        case 11235: // Dark bow
            return 10;
        case 2883: // Normal ogre bow
            return 9;
        case 4153: // Granite maul
        case 4718: // Dharoks greataxe
        case 6528: // Tzhaar-ket-om
            return 8; // also need two-h swords, halberds
        case 8880: // Dorgeshuun c'bow
        case 10887: // Barrelchest anchor
            return 7;
        case 2415: // Saradomin staff
        case 2416: // Guthix staff
        case 2417: // Zamorak staff
        case 4151: // Abyssal whip
        case 4170: // Slayer's staff
        case 4675: // Ancient staff
        case 4734: // Karils crossbow (full, others are x-bow)
        case 6522: // Toktz-xil-ul
        case 6523: // Toktz-xil-ak
        case 6525: // Toktz-xil-ek
        case 6526: // Toktz-mej-tal
        case 11716: // Zamorakian spear
        case 11730: // Saradomin sword
            return 5;
        }
        String name = def.getName();
        if (name == null) {
            return 6;
        }
        name = name.toLowerCase();
        if (name.contains("halberd") || name.contains("2h")) {
            return 8;
        }
        if (name.contains("battleaxe") || name.contains("warhammer") || name.contains("godsword") || name.contains("javelin") || name.contains("ahrims") || name.contains("longbow")
                || (name.contains("crossbow") && !name.contains("karils"))) {
            return 7;
        }
        if (name.contains("dagger") || name.contains("scimitar") || name.contains("claw") || name.contains("shortbow") || name.contains(" sword") || name.contains("karils x-xbow")) {
            return 5;
        }
        if (name.contains("dart") || name.contains("knife")) {
            return 4;
        }
        return 6;
    }

    private static int getRange(int id, ItemDefinition def) {
        String name = def.getName();
        if (name == null) {
            return 1;
        }
        name = name.toLowerCase();
        if (name.contains("halberd")) {
            return 2;
        }
        if (name.contains("dart") || name.contains("throw")) {
            return 4;
        }
        if (name.contains("javelin") || name.contains("knife")) {
            return 5;
        }
        if (name.contains("crossbow") || name.contains("shortbow") || name.contains("x-bow") || name.contains("seercull") || name.contains("crystal bow")) {
            return 7;
        }
        if (name.contains("longbow")) {
            return 8;
        }
        if (name.contains("comp")) {
            return 8;
        }
        return 1;
    }

    private static int getAttackAnimation(int id, ItemDefinition def, int index) {
        String name = def.getName();
        if (name == null) {
            return -1;
        }
        switch (id) {
        case 4151:
            return 1658;
        }

        name = name.toLowerCase();
        if (name.contains("karil")) {
            return 2075;
        }
        if (name.contains("crossbow")) {
            return 4230;
        }
        if (name.contains("bow")) {
            return 426;
        }
        if (name.contains("dagger")) {
            switch (index) {
            case 0:
            case 3:
                return 412;
            case 1:
                return 376;
            case 2:
                return 377;
            }
        }
        if (name.contains("2h") || name.contains("godsword")) {
            switch (index) {
            case 0:
            case 1:
                return 7041;
            case 2:
                return 7048;
            case 3:
                return 7049;
            }
        }
        if (name.contains("staff")) {
            return 419;
        }

        return -1;
    }

    private static int getDefendAnimation(int id, ItemDefinition def) {
        String name = def.getName();
        if (name == null) {
            return 378;
        }
        name = name.toLowerCase();
        if (name.contains("2h") || name.contains("godsword")) {
            return 7050;
        }
        if (name.contains("dagger")) {
            return 378;
        }
        if (name.contains("staff")) {
            return 520;
        }
        return 404;
    }

    private static int getEquipRequirement(int skill, int id, ItemDefinition def, int slot) {
        String name = def.getName();
        if (name == null) {
            return 99;
        }
        name = name.toLowerCase();
        switch (skill) {
        case Skill.ATTACK:
            if (slot != Equipment.WEAPON) {
                return 1;
            }
            if (name.contains("dart") || name.contains("throw") || name.contains("knife") || name.contains("javelin")) {
                return 1;
            }
            if (name.contains("steel"))
                return 5;
            if (name.contains("black") || name.contains("white"))
                return 10;
            if (name.contains("mith"))
                return 20;
            if (name.contains("adam"))
                return 30;
            if (name.contains("rune"))
                return 40;
            if (name.contains("dragon"))
                return 60;
            if (name.contains("ahrim") || name.contains("verac") || name.contains("karil") || name.contains("torag") || name.contains("dharok") || name.contains("guthan"))
                return 70;

            if (name.contains("battlestaff")) {
                return 30;
            }
            if (name.contains("mystic")) {
                return 40;
            }
            if (name.contains("godsword")) {
                return 75;
            }
            switch (id) {
            case 12570: // Ogre club
                return 5;
            case 11061: // Ancient mace
                return 15;
            case 35: // Excalibur
                return 20;
            case 3757: // Fremennik blade
            case 10149: // Swamp lizard
                return 30;
            case 11037: // Brine sabre
                return 40;
            case 1409: // Iban's staff
            case 4153: // Granite maul
            case 4158: // Leaf bladed spear
            case 4675: // Ancient staff
            case 10146: // Orange salamander
            case 13290: // Leaf bladed sword
                return 50;
            case 6523: // Toktz-xil-ak
            case 6526: // Toktz-mej-tal
            case 6527: // Tzhaar-ket-em
            case 10147: // Red salamander
            case 10887: // Barrelchest anchor
                return 60;
            case 4151: // Abyssal whip
            case 10148: // Black salamander
            case 11716: // Zamorakian spear
            case 11730: // Saradomin sword
                return 70;
            }
        case Skill.STRENGTH:
            if (name.contains("granite")) {
                return 50;
            }
            if (slot == Equipment.WEAPON && (name.contains("dharok") || name.contains("torag"))) {
                return 70;
            }
            if (name.contains("halberd")) {
                if (name.contains("black") || name.contains("white"))
                    return 5;
                if (name.contains("mith"))
                    return 10;
                if (name.contains("adam"))
                    return 15;
                if (name.contains("rune"))
                    return 20;
                if (name.contains("drag"))
                    return 30;
            }
            switch (id) {
            case 10887: // Barrelchest anchor
                return 40;
            case 6528: // Tzhaar-ket-om
                return 60;
            }
            break;
        case Skill.DEFENCE:
            if (name.contains("dagon") || name.contains("initiate") || (name.contains("mystic") && slot != Equipment.WEAPON) || (name.contains("studded") && slot == Equipment.BODY)) {
                return 20;
            }
            if (name.contains("infinity") || name.contains("frog-leather")) {
                return 25;
            }
            if (id >= 10338 && id <= 10344) { // 3rd age magic
                return 30;
            }
            if (name.contains("snakeskin") || name.contains("proselyte")) {
                return 30;
            }
            if (name.contains("lunar") || name.contains("skeletal") || name.contains("splitbark") || name.contains("spined") || (name.contains("d'hide") && slot == Equipment.BODY)
                    || name.contains("rock-shell")) {
                return 40;
            }
            if (id >= 10330 && id <= 10336) { // 3rd age range
                return 45;
            }
            if (name.contains("granite") && slot != Equipment.WEAPON) {
                return 50;
            }
            if (name.contains("neitiznot")) {
                return 55;
            }
            if (id >= 10346 && id <= 10352 || (name.contains("bandos") && slot != Equipment.WEAPON)) { // 3rd
                                                                                                       // age
                                                                                                       // melee
                return 65;
            }
            if (name.contains("crystal shield") || name.contains("ahrim") || name.contains("verac") || name.contains("karil") || name.contains("torag") || name.contains("dharok")
                    || name.contains("guthan")) {
                return 70;
            }
            if (name.contains("plate") || name.contains("defender") || name.contains("chain") || name.contains("helm") || name.contains("shield") || name.contains("gauntlet")
                    || name.contains("boots")) {
                if (name.contains("steel"))
                    return 5;
                if (name.contains("black") || name.contains("white"))
                    return 10;
                if (name.contains("mith"))
                    return 20;
                if (name.contains("adam"))
                    return 30;
                if (name.contains("rune") || name.contains("gilded") || name.contains("guthix") || name.contains("zamorak") || name.contains("saradomin"))
                    return 40;
                if (name.contains("drag"))
                    return 60;
            }
            switch (id) {
            case 4551: // Spiny helmet
                return 5;
            case 1131: // Hardleather body
                return 10;
            case 4156: // Mirror shield
            case 7398:
            case 7399:
            case 7400: // Enchanted armor
            case 10824: // Yak legs
            case 10822: // Yak body
                return 20;
            case 10826: // Fremennik round shield
                return 25;
            case 3748: // Fremennik helm
            case 3758: // Fremennik shield
            case 4567: // Gold helmet
            case 7917: // Ram skull helm
                return 30;
            case 10551: // Fighter torso
            case 10552: // Runner boots
            case 10553: // Penance gloves
            case 10555: // Penance skirt
            case 13734: // Spirit shield
                return 40;
            case 3749: // Archer helm
            case 3751: // Berserker helm
            case 3753: // Warrior helm
            case 10547: // Healer hat
            case 10548: // Fighter hat
            case 10549: // Runner hat
            case 10550: // Ranger hat
                return 45;
            case 11200: // Dwarven helmet
                return 50;
            case 6524:
                return 60;
            case 11718: // Arma helmet
            case 12670: // Arma helmet (e)
            case 12671: // Arma helmet (charged)
            case 11720: // Arma chest
            case 11722: // Arma skirt
            case 13736: // Blessed spirit shield
                return 70;
            case 11283: // Dragonfire shield
            case 13738:
            case 13740:
            case 13742:
            case 13744: // Sigiled spirit shields
                return 75;
            }
            break;
        case Skill.MAGIC:
            if (name.contains("battlestaff")) {
                return 30;
            }
            if (name.contains("mitre") || name.contains("mystic") || name.contains("skeletal") || name.contains("splitbark") || name.contains("dagon")) {
                return 40;
            }
            if (name.contains("infinity")) {
                return 50;
            }
            if (id >= 2412 && id <= 2417) { // God capes/staves
                return 60;
            }
            if (id >= 10338 && id <= 10344) { // 3rd age magic
                return 65;
            }
            if (name.contains("lunar")) {
                return 65;
            }
            if (name.contains("ahrim")) {
                return 70;
            }
            switch (id) {
            case 2579: // Wizard boots
                return 20;
            case 10149: // Swamp lizard
                return 30;
            case 7398:
            case 7399:
            case 7400: // Enchanted armor
                return 40;
            case 6908: // Beginner wand
                return 45;
            case 1409: // Iban's staff
            case 4170: // Slayer's staff
            case 4675: // Ancient staff
            case 6910: // Apprentice wand
            case 10146: // Orange salamander
                return 50;
            case 6912: // Teacher wand
                return 55;
            case 6526: // Toktz-mej-tal
            case 6889: // Mage's book
            case 6914: // Master wand
            case 10147: // Red salamander
                return 60;
            case 13738: // Arcane
            case 13744: // Spectral
                return 65;
            case 10148: // Black salamander
                return 70;
            }
            break;
        case Skill.RANGED:
            // TODO dhide
            if (name.contains("dart") || name.contains("throw") || name.contains("knife") || name.contains("javelin")) {
                if (name.contains("steel"))
                    return 5;
                if (name.contains("black"))
                    return 10;
                if (name.contains("mith"))
                    return 20;
                if (name.contains("adam"))
                    return 30;
                if (name.contains("rune"))
                    return 40;
                if (name.contains("drag"))
                    return 60;
            }
            if (name.contains("bow")) {
                if (name.contains("oak"))
                    return 5;
                if (name.contains("willow"))
                    return 20;
                if (name.contains("maple"))
                    return 30;
                if (name.contains("ogre"))
                    return 30;
                if (name.contains("yew"))
                    return 40;
                if (name.contains("magic"))
                    return 60;
                if (name.contains("crystal"))
                    return 70;
            }
            if (name.contains("hide") || name.contains("chaps") || name.contains("vamb") || name.contains("coif")) {
                if (name.contains("green"))
                    return 40;
                if (name.contains("blue"))
                    return 50;
                if (name.contains("red"))
                    return 60;
                if (name.contains("black") || name.contains("saradomin") || name.contains("zamorak") || name.contains("guthix"))
                    return 70;
            }
            if (name.contains("studded")) {
                return 20;
            }
            if (name.contains("frog-leather")) {
                return 25;
            }
            if (name.contains("snakeskin")) {
                return 30;
            }
            if (name.contains("spined")) {
                return 40;
            }
            if (id >= 10330 && id <= 10336) { // 3rd age range
                return 65;
            }
            if (name.contains("karil")) {
                return 70;
            }
            switch (id) {
            case 9176: // Blurite crossbow
                return 16;
            case 1169: // Coif
                return 20;
            case 9177: // Iron crossbow
                return 26;
            case 8880: // Dorgeshuun crossbow
                return 28;
            case 10149: // Swamp lizard
            case 10498: // Ava's attractor
                return 30;
            case 9179: // Steel crossbow
                return 31;
            case 13081:
                return 33;
            case 9181: // Mith crossbow
                return 36;
            case 7370: // Green body (g)
            case 7372: // Green body (t)
            case 7378: // Green chaps (g)
            case 7380: // Green chaps (t)
                return 40;
            case 2577: // Ranger boots
            case 2581: // Robin hoot hat
            case 9976: // Chinchompa
                return 45;
            case 9183: // Adamant crossbow
                return 46;
            case 6724: // Seercull
            case 7374: // Blue body(g)
            case 7376: // Blue body(t)
            case 7382: // Blue chaps(g)
            case 7384: // Blue chaps(t)
            case 10146: // Orange salamander
            case 10156: // Hunters crossbow
            case 10499: // Ava's accumulator
                return 50;
            case 9977: // Red chinchompa
                return 55;
            case 6522: // Toktz-xil-ul
            case 10147: // Red salamander
            case 10555: // Penance skirt
            case 11234: // Dark bow
                return 60;
            case 9185: // Rune crossbow
                return 61;
            case 10148: // Black salamander
            case 11718: // Arma helmet
            case 12670: // Arma helmet (e)
            case 12671: // Arma helmet (charged)
            case 11720: // Arma chest
            case 11722: // Arma skirt
                return 70;
            }
            break;
        case Skill.PRAYER:
            if (name.contains("initiate")) {
                return 10;
            }
            if (name.contains("proselyte")) {
                return 20;
            }
            if (id >= 10458 && id <= 10468) { // God robe tops/legs
                return 20;
            }
            if (name.contains("mitre")) {
                return 40;
            }
            if (name.contains("stole") || name.contains("crozier")) {
                return 60;
            }
            switch (id) {
            case 11061: // Ancient mace
                return 25;
            case 10446:
            case 10448:
            case 10450: // God cloaks
                return 40;
            case 13734: // Spirit shield
                return 55;
            case 13736: // Blessed spirit shield
                return 60;
            case 13738: // Arcane
            case 13744: // Spectral
                return 70;
            case 13740: // Divine
            case 13742: // Elysian
                return 75;
            }
            break;
        case Skill.RUNECRAFTING:
            switch (id) {
            case 12863: // Air runecrafting gloves
                return 10;
            case 12864: // Water runecrafting gloves
                return 20;
            case 12865: // Earth runecrafting gloves
                return 30;
            }
            break;
        case Skill.HUNTER:
            break;
        case Skill.SUMMONING:
            switch (id) {
            case 12680:
            case 12681:
                return 45;
            case 12670: // Arma helmet (e)
            case 12671: // Arma helmet (charged)
                return 60;
            }
            break;
        case Skill.SLAYER:
            switch (id) {
            case 4158: // Leaf bladed spear
            case 4170: // Slayer staff
            case 13290: // Leaf bladed sword
                return 55;
            }
            break;
        case Skill.AGILITY:
            if (name.contains("crystal bow") || name.contains("crystal shield")) {
                return 50;
            }
            break;
        case Skill.FIREMAKING:
            switch (id) {
            case 13661: // Inferno adze
                return 92;
            }
            break;
        }
        return 1;
    }

    // Credits to Xenorune
    private static final int[][] RANGE_STRENGTH_BONUS = { { 890, // addy arrow
            9143, // addy bolt
            810, // addy dart
            829, // addy jav
            867, // addy knife
            804, // addy thrownaxe
            881, // barbed bolts
            13803, // black bolts
            3093, // black dart
            869, // black knife
            9139, // blurite bolt
            4740, // bolt rack
            8882, // bone bolts
            13280, // broad tipped bolts
            882, // bronze arrow
            877, // bronze bolts
            806, // bronze dart
            825, // bronze jav
            864, // bronze knife
            800, // bronze thrownaxe
            4214, // full crystal bow
            13953, // corrupt morr jav
            13957, // corrupt morr thrownaxe
            9340, // diamond bolt
            11212, // dragon arrow
            9341, // dragon bolts
            11230, // dragon dart
            9338, // emerald bolts
            10142, // guam tar
            10145, // harralander tar
            78, // ice arrows
            884, // iron arrows
            9140, // iron bolts
            807, // iron dart
            826, // iron javelin
            863, // iron knife
            801, // iron thrownaxe
            10158, // kebbit bolts
            10159, // long kebbit bolts
            888, // mith arrow
            9142, // mith bolts
            809, // mithril dart
            828, // mithril javelin
            866, // mith knife
            803, // mith thrownaxe
            13879, // morrigans javelin
            13883, // morrigans thrownaxe
            2866, // ogre arrow
            9342, // onyx bolts
            880, // pearl bolts
            10034, // red chinchompa
            9339, // ruby bolts
            892, // rune arrow
            893, // rune arrow (p)
            811, // rune dart
            830, // rune javelin
            868, // rune knife
            805, // rune thrownaxe
            9144, // rune bolts
            9337, // sapphire bolts
            9145, // silver bolts
            886, // steel arrow
            9141, // steel bolts
            808, // steel dart
            827, // steel javelin
            865, // steel knife
            802, // steel thrownaxe
            10144, // tarromin tar
            6522, // obsidian ring
            9336, // topaz bolts
            9706, // training arrows
            879, // opal bolts
            9236, // opal bolts (e)
            9335, // jade bolts
            9237, // jade bolts (e)
            9238, // pearl bolts(e)
            9239, // topaz bolts (e)
            9241, // emerald bolts (e)
            9240, // sapphire bolts (e)
            9242, // ruby bolts (e)
            9243, // diamond bolts (e)
            9244, // dragon bolts (e)
            9245, // onyx bolts (e)
    }, { 31, // addy arrow
            100, // addy bolt
            10, // addy dart
            28, // addy jav
            14, // addy knife
            23, // addy thrownaxe
            12, // barbed bolts
            75, // black bolts
            6, // black dart
            8, // black knife
            28, // blurite bolt
            55, // bolt rack
            49, // bone bolts
            100, // broad tipped bolts
            7, // bronze arrow
            10, // bronze bolts
            1, // bronze dart
            6, // bronze jav
            3, // bronze knife
            5, // bronze thrownaxe
            70, // full crystal bow
            145, // corrupt morr jav
            117, // corrupt morr thrownaxe
            105, // diamond bolt
            60, // dragon arrow
            117, // dragon bolts
            20, // dragon dart
            85, // emerald bolts
            16, // guam tar
            49, // harralander tar
            16, // ice arrows
            10, // iron arrows
            46, // iron bolts
            3, // iron dart
            10, // iron javelin
            4, // iron knife
            7, // iron thrownaxe
            28, // kebbit bolts
            38, // long kebbit bolts
            22, // mith arrow
            82, // mith bolts
            7, // mithril dart
            18, // mithril javelin
            10, // mith knife
            16, // mith thrownaxe
            145, // morrigans javelin
            117, // morrigans thrownaxe
            22, // ogre arrow
            120, // onyx bolts
            48, // pearl bolts
            15, // red chinchompa
            103, // ruby bolts
            49, // rune arrow
            49, // rune arrow (p)
            14, // rune dart
            42, // rune javelin
            24, // rune knife
            36, // rune thrownaxe
            115, // rune bolts
            83, // sapphire bolts
            36, // silver bolts
            16, // steel arrow
            64, // steel bolts
            4, // steel dart
            12, // steel javelin
            7, // steel knife
            11, // steel thrownaxe
            31, // tarromin tar
            49, // obsidian ring
            66, // topaz bolts
            7, // training arrows
            10, // opal bolts
            10, // opal bolts (e)
            28, // jade bolts
            28, // jade bolts (e)
            46, // pearl bolts(e)
            64, // topaz bolts (e)
            82, // emerald bolts (e)
            82, // sapphire bolts (e)
            100, // ruby bolts (e)
            100, // diamond bolts (e)
            117, // dragon bolts (e)
            115, // onyx bolts (e)
    } };

    private static int getRangeStrengthBonus(int id) {
        for(int i = 0; i < RANGE_STRENGTH_BONUS[0].length; i++) {
            if(RANGE_STRENGTH_BONUS[0][i] == id) {
                return RANGE_STRENGTH_BONUS[1][i];
            }
        }
        return 0;
    }
}
