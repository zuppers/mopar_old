package net.scapeemulator.cache.def;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.scapeemulator.cache.util.ByteBufferUtils;

/**
 * A class that can decode and decode item definitions from the cache.
 * 
 * @author Graham
 * @author `Discardedx2
 * @author Davidi2
 */
public final class ItemDefinition {

    private int id;
    private String name;
    private String examine;
    private int inventoryModelId;
    private int modelZoom;
    private int modelRotation1;
    private int modelRotation2;
    private int modelOffset1;
    private int modelOffset2;

    private boolean stackable;
    private int value;
    private boolean membersOnly;

    private Map<Integer, Object> scriptData;

    private int maleWearModel1;
    private int maleWearModel2;
    private int femaleWearModel1;
    private int femaleWearModel2;
    private int maleDialogueHat;
    private int femaleDialogueHat;
    private int maleDialogueModel;
    private int femaleDialogueModel;
    private int maleModel3;
    private int femaleModel3;

    private String[] groundOptions;
    private String[] inventoryOptions;

    private byte[] recolorPalette;
    private short[] originalModelColors;
    private short[] modifiedModelColors;
    private short[] originalTextureColors;
    private short[] modifiedTextureColors;

    private boolean unnoted;
    private int swapId;
    private int notedTemplateId;
    private int[] stackIds;
    private int[] stackAmountTriggers;
    private int teamId;
    private int lendId;
    private int weight;
    private int lendTemplateId;

    private int modelVerticesX;
    private int modelVerticesY;
    private int modelVerticesZ;
    private int modelLighting;

    /* unknowns */
    private int anInt752;
    private int anInt756;
    private int anInt758;
    private int anInt760;
    private int anInt767;
    private int anInt768;
    private int anInt775;
    private int anInt777;
    private int anInt778;
    private int anInt788;
    private int anInt790;
    private int anInt800;
    private int anInt802;

    public ItemDefinition() {
        name = "null";
        scriptData = new HashMap<Integer, Object>();
        maleModel3 = -1;
        femaleModel3 = -1;
        maleDialogueHat = -1;
        notedTemplateId = -1;
        femaleDialogueModel = -1;
        modelVerticesY = 128;
        femaleDialogueHat = -1;
        anInt756 = -1;
        anInt767 = -1;
        anInt758 = -1;
        lendTemplateId = -1;
        lendId = -1;
        maleWearModel2 = -1;
        femaleWearModel1 = -1;
        stackable = false;
        swapId = -1;
        femaleWearModel2 = -1;
        anInt788 = -1;
        modelVerticesZ = 128;
        membersOnly = false;
        maleDialogueModel = -1;
        maleWearModel1 = -1;
        modelVerticesX = 128;
        groundOptions = new String[] { null, null, "take", null, null };
        inventoryOptions = new String[] { null, null, null, null, "drop" };
        modelZoom = 2000;
        unnoted = false;
        value = 1;
    }

    /**
     * @param buffer A {@link ByteBuffer} that contains information such as the items location.
     * @return a new ItemDefinition.
     */
    public static ItemDefinition decode(int id, ByteBuffer buffer) {
        ItemDefinition def = new ItemDefinition();
        def.id = id;
        def.swapId = id;

        while (true) {
            int opcode = buffer.get() & 0xFF;
            if (opcode == 0)
                break;
            if (opcode == 1)
                def.inventoryModelId = buffer.getShort() & 0xFFFFF;
            else if (opcode == 2) {
                def.name = ByteBufferUtils.getJagexString(buffer);
                def.examine = def.name + " " + id;
            } else if (opcode == 4)
                def.modelZoom = buffer.getShort() & 0xFFFFF;
            else if (opcode == 5)
                def.modelRotation1 = buffer.getShort() & 0xFFFFF;
            else if (opcode == 6)
                def.modelRotation2 = buffer.getShort() & 0xFFFFF;
            else if (opcode == 7) {
                def.modelOffset1 = buffer.getShort() & 0xFFFFF;
                if (def.modelOffset1 > 32767)
                    def.modelOffset1 -= 65536;
                def.modelOffset1 <<= 0;
            } else if (opcode == 8) {
                def.modelOffset2 = buffer.getShort() & 0xFFFFF;
                if (def.modelOffset2 > 32767)
                    def.modelOffset2 -= 65536;
                def.modelOffset2 <<= 0;
            } else if (opcode == 11)
                def.stackable = true;
            else if (opcode == 12)
                def.value = buffer.getInt();
            else if (opcode == 16)
                def.membersOnly = true;
            else if (opcode == 18)
                buffer.getShort();
            else if (opcode == 23)
                def.maleWearModel1 = buffer.getShort() & 0xFFFFF;
            else if (opcode == 24)
                def.femaleWearModel1 = buffer.getShort() & 0xFFFFF;
            else if (opcode == 25)
                def.maleWearModel2 = buffer.getShort() & 0xFFFFF;
            else if (opcode == 26)
                def.femaleWearModel2 = buffer.getShort() & 0xFFFFF;
            else if (opcode >= 30 && opcode < 35)
                def.groundOptions[opcode - 30] = ByteBufferUtils.getJagexString(buffer);
            else if (opcode >= 35 && opcode < 40)
                def.inventoryOptions[opcode - 35] = ByteBufferUtils.getJagexString(buffer);
            else if (opcode == 40) {
                int length = buffer.get() & 0xFF;
                def.originalModelColors = new short[length];
                def.modifiedModelColors = new short[length];
                for (int index = 0; index < length; index++) {
                    def.originalModelColors[index] = (short) (buffer.getShort() & 0xFFFFF);
                    def.modifiedModelColors[index] = (short) (buffer.getShort() & 0xFFFFF);
                }
            } else if (opcode == 41) {
                int length = buffer.get() & 0xFF;
                def.originalTextureColors = new short[length];
                def.modifiedTextureColors = new short[length];
                for (int index = 0; index < length; index++) {
                    def.originalTextureColors[index] = (short) (buffer.getShort() & 0xFFFFF);
                    def.modifiedTextureColors[index] = (short) (buffer.getShort() & 0xFFFFF);
                }
            } else if (opcode == 42) {
                int length = buffer.get() & 0xFF;
                def.recolorPalette = new byte[length];
                for (int index = 0; index < length; index++) {
                    def.recolorPalette[index] = buffer.get();
                }
            } else if (opcode == 65) {
                def.unnoted = true;
            } else if (opcode == 78) {
                def.maleModel3 = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 79) {
                def.femaleModel3 = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 90) {
                def.maleDialogueModel = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 91) {
                def.femaleDialogueModel = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 92) {
                def.maleDialogueHat = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 93) {
                def.femaleDialogueHat = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 95) {
                def.anInt768 = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 96) {
                def.anInt800 = buffer.get() & 0xFF;
            } else if (opcode == 97) {
                def.swapId = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 98) {
                def.notedTemplateId = buffer.getShort() & 0xFFFFF;
            } else if (opcode >= 100 && opcode < 110) {
                if (def.stackIds == null) {
                    def.stackIds = new int[10];
                    def.stackAmountTriggers = new int[10];
                }
                def.stackIds[opcode - 100] = buffer.getShort() & 0xFFFFF;
                def.stackAmountTriggers[opcode - 100] = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 110) {
                def.modelVerticesX = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 111) {
                def.modelVerticesY = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 112) {
                def.modelVerticesZ = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 113) {
                def.modelLighting = buffer.get();
            } else if (opcode == 114) {
                def.anInt790 = buffer.get() * 5;
            } else if (opcode == 115) {
                def.teamId = buffer.get() & 0xFF;
            } else if (opcode == 121) {
                def.lendId = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 122) {
                def.lendTemplateId = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 125) {
                def.anInt760 = buffer.get() << 0;
                def.anInt778 = buffer.get() << 0;
                def.anInt775 = buffer.get() << 0;
            } else if (opcode == 126) {
                def.anInt777 = buffer.get() << 0;
                def.anInt802 = buffer.get() << 0;
                def.anInt752 = buffer.get() << 0;
            } else if (opcode == 127) {
                def.anInt767 = buffer.get() & 0xFF;
                def.anInt758 = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 128) {
                def.anInt788 = buffer.get() & 0xFF;
                def.anInt756 = buffer.getShort() & 0xFFFFF;
            } else if (opcode == 129) {
                buffer.get();// & 0xFF;
                buffer.getShort();// & 0xFFFFF;
            } else if (opcode == 130) {
                buffer.get();// & 0xFF;
                buffer.getShort();// & 0xFFFFF;
            } else if (opcode == 132) {
                int len = buffer.get() & 0xFF;
                for (int index = 0; index < len; index++) {
                    buffer.getShort();// & 0xFFFFF;
                }
            } else if (opcode == 249) {
                int length = buffer.get() & 0xFF;
                for (int index = 0; index < length; index++) {
                    boolean isString = buffer.get() == 1;
                    int key = ByteBufferUtils.getTriByte(buffer);
                    Object value = isString ? ByteBufferUtils.getJagexString(buffer) : buffer.getInt();
                    def.scriptData.put(key, value);
                }
            }
        }
        return def;
    }

    public ByteBuffer encode() throws IOException {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream(); DataOutputStream os = new DataOutputStream(bout)) {
            os.write(1);
            os.writeShort(inventoryModelId);
            if (!name.equals("null")) {
                os.write(2);
                ByteBufferUtils.putJagexString(os, name);
            }
            if (modelZoom != 2000) {
                os.write(4);
                os.writeShort(modelZoom);
            }
            if (modelRotation1 != 0) {
                os.write(5);
                os.writeShort(modelRotation1);
            }
            if (modelRotation2 != 0) {
                os.write(6);
                os.writeShort(modelRotation2);
            }
            if (modelOffset1 != 0) {
                os.write(7);
                os.writeShort(modelOffset1 < 0 ? modelOffset1 + 65536 : modelOffset1);
            }
            if (modelOffset2 != 0) {
                os.write(8);
                os.writeShort(modelOffset2 < 0 ? modelOffset2 + 65536 : modelOffset2);
            }
            if (stackable) {
                os.write(11);
            }
            if (value != 1) {
                os.write(12);
                os.writeInt(value);
            }
            if (membersOnly) {
                os.write(16);
            }
            if (maleWearModel1 != -1) {
                os.write(23);
                os.writeShort(maleWearModel1);
            }
            if (femaleWearModel1 != -1) {
                os.write(24);
                os.writeShort(femaleWearModel1);
            }
            if (maleWearModel2 != -1) {
                os.write(25);
                os.writeShort(maleWearModel2);
            }
            if (femaleWearModel2 != -1) {
                os.write(26);
                os.writeShort(femaleWearModel2);
            }
            for (int i = 0; i < groundOptions.length; i++) {
                String option = groundOptions[i];
                if (option == null || (i == 2 && option.equals("take"))) {
                    continue;
                }
                os.write(30 + i);
                ByteBufferUtils.putJagexString(os, option);
            }
            for (int i = 0; i < inventoryOptions.length; i++) {
                String option = inventoryOptions[i];
                if (option == null || (i == 4 && option.equals("drop"))) {
                    continue;
                }
                os.write(35 + i);
                ByteBufferUtils.putJagexString(os, option);
            }
            if (originalModelColors != null) {
                os.write(40);
                os.write(originalModelColors.length);
                for (int i = 0; i < originalModelColors.length; i++) {
                    os.writeShort(originalModelColors[i]);
                    os.writeShort(modifiedModelColors[i]);
                }
            }
            if (originalTextureColors != null) {
                os.write(41);
                os.write(originalTextureColors.length);
                for (int i = 0; i < originalTextureColors.length; i++) {
                    os.writeShort(originalTextureColors[i]);
                    os.writeShort(modifiedTextureColors[i]);
                }
            }
            if (recolorPalette != null) {
                os.write(42);
                os.write(recolorPalette.length);
                for (byte b : recolorPalette) {
                    os.write(b);
                }
            }
            if (unnoted) {
                os.write(65);
            }
            if (maleModel3 != -1) {
                os.write(78);
                os.writeShort(maleModel3);
            }
            if (femaleModel3 != -1) {
                os.write(79);
                os.writeShort(femaleModel3);
            }
            if (maleDialogueModel != -1) {
                os.write(90);
                os.writeShort(maleDialogueModel);
            }
            if (femaleDialogueModel != -1) {
                os.write(91);
                os.writeShort(femaleDialogueModel);
            }
            if (maleDialogueHat != -1) {
                os.write(92);
                os.writeShort(maleDialogueHat);
            }
            if (femaleDialogueHat != -1) {
                os.write(93);
                os.writeShort(femaleDialogueHat);
            }
            if (anInt768 != 0) {
                os.write(95);
                os.writeShort(anInt768);
            }
            if (anInt800 != 0) {
                os.write(96);
                os.write(anInt800);
            }
            if (swapId != -1) {
                os.write(97);
                os.writeShort(swapId);
            }
            if (notedTemplateId != -1) {
                os.write(98);
                os.writeShort(notedTemplateId);
            }
            if (stackIds != null) {
                for (int i = 0; i < stackIds.length; i++) {
                    if (stackIds[i] != 0 || stackAmountTriggers[i] != 0) {
                        os.write(100 + i);
                        os.writeShort(stackIds[i]);
                        os.writeShort(stackAmountTriggers[i]);
                    }
                }
            }
            if (modelVerticesX != 128) {
                os.write(110);
                os.writeShort(modelVerticesX);
            }
            if (modelVerticesY != 128) {
                os.write(111);
                os.writeShort(modelVerticesY);
            }
            if (modelVerticesZ != 128) {
                os.write(112);
                os.writeShort(modelVerticesZ);
            }
            if (modelLighting != 0) {
                os.write(113);
                os.write(modelLighting);
            }
            if (anInt790 != 0) {
                os.write(114);
                os.write(anInt790);
            }
            if (teamId != 0) {
                os.write(115);
                os.write(teamId);
            }
            if (lendId != -1) {
                os.write(121);
                os.writeShort(lendId);
            }
            if (lendTemplateId != -1) {
                os.write(122);
                os.writeShort(lendTemplateId);
            }
            if (anInt760 != 0 || anInt778 != 0 || anInt775 != 0) {
                os.write(125);
                os.write(anInt760);
                os.write(anInt778);
                os.write(anInt775);
            }
            if (anInt777 != 0 || anInt802 != 0 || anInt752 != 0) {
                os.write(126);
                os.write(anInt777);
                os.write(anInt802);
                os.write(anInt752);
            }
            if (anInt767 != -1 && anInt758 != -1) {
                os.write(127);
                os.write(anInt767);
                os.writeShort(anInt758);
            }
            if (anInt788 != -1 && anInt756 != -1) {
                os.write(128);
                os.write(anInt788);
                os.writeShort(anInt756);
            }
            if (scriptData.size() > 0) {
                os.write(249);
                os.write(scriptData.size());
                for (Entry<Integer, Object> entry : scriptData.entrySet()) {
                    Object value = entry.getValue();
                    boolean string = value instanceof String;
                    os.write(string ? 1 : 0);
                    os.write(entry.getKey() >> 16);
                    os.write(entry.getKey() >> 8);
                    os.write(entry.getKey());
                    if (string) {
                        ByteBufferUtils.putJagexString(os, (String) value);
                    } else {
                        os.writeInt((int) value);
                    }
                }
            }
            os.write(0);
            byte[] bytes = bout.toByteArray();
            return ByteBuffer.wrap(bytes);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExamine() {
        return examine;
    }

    public int getInventoryModelId() {
        return inventoryModelId;
    }

    public int getModelZoom() {
        return modelZoom;
    }

    public int getModelRotation1() {
        return modelRotation1;
    }

    public int getModelRotation2() {
        return modelRotation2;
    }

    public int getModelOffset1() {
        return modelOffset1;
    }

    public int getModelOffset2() {
        return modelOffset2;
    }

    /**
     * Returns the raw boolean in the cache about whether this item is stackable. To get more
     * accurate information server sided, use isStackable()
     * 
     * @return the raw boolean in the cache about whether this item is stackable
     */
    public boolean getStackable() {
        return stackable;
    }

    /**
     * Uses a combination of item information to determine if this item should stack in a player
     * inventory.
     * 
     * @return true if the item should stack in a player inventory, false otherwise
     */
    public boolean isStackable() {
        return notedTemplateId == 799 || stackable;
    }

    public int getValue() {
        return value;
    }

    public int getHighAlchemyValue() {
        return (int) (value * 0.6);
    }

    public int getLowAlchemyValue() {
        return (int) (value * 0.4);
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public int getMaleWearModel1() {
        return maleWearModel1;
    }

    public int getMaleWearModel2() {
        return maleWearModel2;
    }

    public int getFemaleWearModel1() {
        return femaleWearModel1;
    }

    public int getFemaleWearModel2() {
        return femaleWearModel2;
    }

    public String[] getGroundOptions() {
        return groundOptions;
    }

    public String[] getInventoryOptions() {
        return inventoryOptions;
    }

    public short[] getOriginalModelColors() {
        return originalModelColors;
    }

    public short[] getModifiedModelColors() {
        return modifiedModelColors;
    }

    public short[] getTextureColour1() {
        return originalTextureColors;
    }

    public short[] getTextureColour2() {
        return modifiedTextureColors;
    }

    public int getMaleModel3() {
        return maleModel3;
    }

    public int getFemaleModel3() {
        return femaleModel3;
    }

    public int getSwapId() {
        return swapId;
    }

    public boolean canBank() {
        return true;
    }

    public boolean getUnnoted() {
        return unnoted;
    }

    public int getNotedItemId() {
        return notedTemplateId == 779 ? id : swapId;
    }

    public int getUnnotedItemId() {
        return unnoted ? id : swapId;
    }

    public int getNotedTemplateId() {
        return notedTemplateId;
    }

    public int[] getStackableIds() {
        return stackIds;
    }

    public int[] getStackableAmounts() {
        return stackAmountTriggers;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getLendId() {
        return lendId;
    }

    public int getWeight() {
        return weight;
    }

    public int getLendTemplateId() {
        return lendTemplateId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public void setInventoryModelId(int inventoryModelId) {
        this.inventoryModelId = inventoryModelId;
    }

    public void setModelZoom(int modelZoom) {
        this.modelZoom = modelZoom;
    }

    public void setModelRotation1(int modelRotation1) {
        this.modelRotation1 = modelRotation1;
    }

    public void setModelRotation2(int modelRotation2) {
        this.modelRotation2 = modelRotation2;
    }

    public void setModelOffset1(int modelOffset1) {
        this.modelOffset1 = modelOffset1;
    }

    public void setModelOffset2(int modelOffset2) {
        this.modelOffset2 = modelOffset2;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public void setScriptData(Map<Integer, Object> scriptData) {
        this.scriptData = scriptData;
    }

    public void setMaleWearModel1(int maleWearModel1) {
        this.maleWearModel1 = maleWearModel1;
    }

    public void setMaleWearModel2(int maleWearModel2) {
        this.maleWearModel2 = maleWearModel2;
    }

    public void setFemaleWearModel1(int femaleWearModel1) {
        this.femaleWearModel1 = femaleWearModel1;
    }

    public void setFemaleWearModel2(int femaleWearModel2) {
        this.femaleWearModel2 = femaleWearModel2;
    }

    public void setMaleDialogueHat(int maleDialogueHat) {
        this.maleDialogueHat = maleDialogueHat;
    }

    public void setFemaleDialogueHat(int femaleDialogueHat) {
        this.femaleDialogueHat = femaleDialogueHat;
    }

    public void setMaleDialogueModel(int maleDialogueModel) {
        this.maleDialogueModel = maleDialogueModel;
    }

    public void setFemaleDialogueModel(int femaleDialogueModel) {
        this.femaleDialogueModel = femaleDialogueModel;
    }

    public void setMaleModel3(int maleModel3) {
        this.maleModel3 = maleModel3;
    }

    public void setFemaleModel3(int femaleModel3) {
        this.femaleModel3 = femaleModel3;
    }

    public void setGroundOptions(String[] groundOptions) {
        this.groundOptions = groundOptions;
    }

    public void setInventoryOptions(String[] inventoryOptions) {
        this.inventoryOptions = inventoryOptions;
    }

    public void setRecolorPalette(byte[] recolorPalette) {
        this.recolorPalette = recolorPalette;
    }

    public void setOriginalModelColors(short[] originalModelColors) {
        this.originalModelColors = originalModelColors;
    }

    public void setModifiedModelColors(short[] modifiedModelColors) {
        this.modifiedModelColors = modifiedModelColors;
    }

    public void setOriginalTextureColors(short[] originalTextureColors) {
        this.originalTextureColors = originalTextureColors;
    }

    public void setModifiedTextureColors(short[] modifiedTextureColors) {
        this.modifiedTextureColors = modifiedTextureColors;
    }

    public void setUnnoted(boolean unnoted) {
        this.unnoted = unnoted;
    }

    public void setSwapId(int swapId) {
        this.swapId = swapId;
    }

    public void setNotedTemplateId(int notedTemplateId) {
        this.notedTemplateId = notedTemplateId;
    }

    public void setStackIds(int[] stackIds) {
        this.stackIds = stackIds;
    }

    public void setStackAmountTriggers(int[] stackAmountTriggers) {
        this.stackAmountTriggers = stackAmountTriggers;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setLendId(int lendId) {
        this.lendId = lendId;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setLendTemplateId(int lendTemplateId) {
        this.lendTemplateId = lendTemplateId;
    }

    public void setModelVerticesY(int modelVerticesY) {
        this.modelVerticesY = modelVerticesY;
    }

    public void setModelLighting(int modelLighting) {
        this.modelLighting = modelLighting;
    }

    public void setModelVerticesZ(int modelVerticesZ) {
        this.modelVerticesZ = modelVerticesZ;
    }

    public void setModelVerticesX(int modelVerticesX) {
        this.modelVerticesX = modelVerticesX;
    }

}
