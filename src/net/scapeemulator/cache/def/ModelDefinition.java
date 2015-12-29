package net.scapeemulator.cache.def;

import java.nio.ByteBuffer;

import net.scapeemulator.cache.util.ByteBufferUtils;

/**
 * A class that can decode and encode model definitions from the cache.
 * 
 * @author Davidi2
 */
public final class ModelDefinition {

    public static int[] hsl2rgb;

    static {
        int[] out = hsl2rgb = new int[65536];
        double d = 0.7D;
        int i = 0;
        for (int i1 = 0; i1 != 512; ++i1) {
            float f = ((float) (i1 >> 3) / 64.0F + 0.0078125F) * 360.0F;
            float f1 = 0.0625F + (float) (0x7 & i1) / 8.0F;
            for (int i2 = 0; i2 != 128; ++i2) {
                float f2 = (float) i2 / 128.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = f / 60.0F;
                int i3 = (int) f6;
                int i4 = i3 % 6;
                float f7 = f6 - (float) i3;
                float f8 = f2 * (-f1 + 1.0F);
                float f9 = f2 * (-(f7 * f1) + 1.0F);
                float f10 = (1.0F - f1 * (-f7 + 1.0F)) * f2;
                if (i4 == 0) {
                    f3 = f2;
                    f5 = f8;
                    f4 = f10;
                } else if (i4 == 1) {
                    f5 = f8;
                    f3 = f9;
                    f4 = f2;
                } else if (i4 == 2) {
                    f3 = f8;
                    f4 = f2;
                    f5 = f10;
                } else if (i4 == 3) {
                    f4 = f9;
                    f3 = f8;
                    f5 = f2;
                } else if (i4 == 4) {
                    f5 = f2;
                    f3 = f10;
                    f4 = f8;
                } else {
                    f4 = f8;
                    f5 = f9;
                    f3 = f2;
                }
                out[i++] = ((int) ((float) Math.pow((double) f3, d) * 256.0F) << 16) | ((int) ((float) Math.pow((double) f4, d) * 256.0F) << 8) | (int) ((float) Math.pow((double) f5, d) * 256.0F);
            }
        }
    }

    private int width;
    private int height;
    private int depth;
    byte[] triangleAlphas;
    private short aShort2844;
    byte[] particleLifespanX;
    short[] texTrianglesB;
    int[] triangleSkins;
    byte aByte2848 = 0;
    int triangleCount = 0;
    private short aShort2850;
    short[] particleDirectionZ;
    byte[] texturePrimaryColor;
    private boolean isNewFormat = false;
    private short aShort2854;
    short[] aShortArray2855;
    int[][] anIntArrayArray2856;
    byte[] textureRenderTypes;
    short[] faceTextures;
    byte[] faceRenderTypes;
    int[] vertexSkins;
    // private static int[] anIntArray2861 = new int[10000];
    int texTriangleCount;
    // private static int[] anIntArray2863 = Class51.anIntArray851;
    int[] trianglesC;
    int[] trianglesA;
    byte[] textureCoords;
    byte[] particleLifespanY;
    // private static int anInt2868 = 0;
    byte[] textureSecondaryColor;
    short[] triangleColors;
    // private static int[] anIntArray2871 = Class51.anIntArray840;
    // Class50[] aClass50Array2872;
    private short aShort2873;
    private short aShort2874;
    private static int[] anIntArray2875 = new int[10000];
    short aShort2876;
    byte[] particleLifespanZ;
    int[] trianglesB;
    short aShort2879;
    private short aShort2880;
    int[] verticesY;
    short[] particleDirectionY;
    // Class50[] aClass50Array2883;
    short[] texTrianglesA;
    int[] verticesX;
    // Class120[] aClass120Array2886;
    int vertexCount = 0;
    short[] particleDirectionX;
    byte[] trianglePriorities;
    int[][] anIntArrayArray2890;
    short[] texTrianglesC;
    int[] verticesZ;
    short[] aShortArray2893;

    public static ModelDefinition decode(ByteBuffer buffer) {
        buffer.position(buffer.limit() - 2);
        boolean newFormat = buffer.get() == -1 && buffer.get() == -1;
        buffer.position(0);
        if (newFormat) {
            return decodeNewFormat(buffer);
        } else {
            return decodeOldFormat(buffer);
        }
    }

    private static ModelDefinition decodeNewFormat(ByteBuffer data) {
        ModelDefinition def = new ModelDefinition();
        def.isNewFormat = true;
        ByteBuffer var2 = data.asReadOnlyBuffer();
        ByteBuffer var3 = data.asReadOnlyBuffer();
        ByteBuffer var4 = data.asReadOnlyBuffer();
        ByteBuffer var5 = data.asReadOnlyBuffer();
        ByteBuffer var6 = data.asReadOnlyBuffer();
        ByteBuffer var7 = data.asReadOnlyBuffer();
        ByteBuffer var8 = data.asReadOnlyBuffer();
        var2.position(var2.limit() - 23);
        int vertexCount = var2.getShort() & 0xFFFF;
        int triangleCount = var2.getShort() & 0xFFFF;
        int texTriangleCount = var2.get();

        int flag = var2.get() & 0xFF;
        boolean fillAttributeFlag = (flag & 1) == 1;
        boolean flag2 = (flag & 2) == 2;
        int var15 = var2.get() & 0xFF;
        int var16 = var2.get() & 0xFF;
        int var17 = var2.get() & 0xFF;
        int var18 = var2.get() & 0xFF;
        int var19 = var2.get() & 0xFF;
        int var20 = var2.getShort() & 0xFFFF;
        int var21 = var2.getShort() & 0xFFFF;
        int var22 = var2.getShort() & 0xFFFF;
        int var23 = var2.getShort() & 0xFFFF;
        int var24 = var2.getShort() & 0xFFFF;
        int textureAmount = 0;
        int particleAmount = 0;
        int particleColors = 0;
        int var28;
        if (texTriangleCount > 0) {
            def.textureRenderTypes = new byte[texTriangleCount];
            var2.position(0);

            for (var28 = 0; var28 < texTriangleCount; ++var28) {
                byte type = def.textureRenderTypes[var28] = var2.get();
                if (type == 0) {
                    ++textureAmount;
                }

                if (type >= 1 && type <= 3) {
                    ++particleAmount;
                }

                if (type == 2) {
                    ++particleColors;
                }
            }
        }

        var28 = texTriangleCount + vertexCount;
        int var30 = var28;
        if (fillAttributeFlag) {
            var28 += triangleCount;
        }

        int var31 = var28;
        var28 += triangleCount;
        int var32 = var28;
        if (var15 == 255) {
            var28 += triangleCount;
        }

        int var33 = var28;
        if (var17 == 1) {
            var28 += triangleCount;
        }

        int var34 = var28;
        if (var19 == 1) {
            var28 += vertexCount;
        }

        int var35 = var28;
        if (var16 == 1) {
            var28 += triangleCount;
        }

        int var36 = var28;
        var28 += var23;
        int var37 = var28;
        if (var18 == 1) {
            var28 += triangleCount * 2;
        }

        int var38 = var28;
        var28 += var24;
        int var39 = var28;
        var28 += triangleCount * 2;
        int var40 = var28;
        var28 += var20;
        int var41 = var28;
        var28 += var21;
        int var42 = var28;
        var28 += var22;
        int var43 = var28;
        var28 += textureAmount * 6;
        int var44 = var28;
        var28 += particleAmount * 6;
        int var45 = var28;
        var28 += particleAmount * 6;
        int var46 = var28;
        var28 += particleAmount;
        int var47 = var28;
        var28 += particleAmount;
        int var48 = var28;
        var28 += particleAmount + particleColors * 2;
        def.vertexCount = vertexCount;
        def.triangleCount = triangleCount;
        def.texTriangleCount = texTriangleCount;
        def.verticesX = new int[vertexCount];
        def.verticesY = new int[vertexCount];
        def.verticesZ = new int[vertexCount];
        def.trianglesA = new int[triangleCount];
        def.trianglesB = new int[triangleCount];
        def.trianglesC = new int[triangleCount];
        if (var19 == 1) {
            def.vertexSkins = new int[vertexCount];
        }

        if (fillAttributeFlag) {
            def.faceRenderTypes = new byte[triangleCount];
        }

        if (var15 == 255) {
            def.trianglePriorities = new byte[triangleCount];
        } else {
            def.aByte2848 = (byte) var15;
        }

        if (var16 == 1) {
            def.triangleAlphas = new byte[triangleCount];
        }

        if (var17 == 1) {
            def.triangleSkins = new int[triangleCount];
        }

        if (var18 == 1) {
            def.faceTextures = new short[triangleCount];
        }

        if (var18 == 1 && texTriangleCount > 0) {
            def.textureCoords = new byte[triangleCount];
        }

        def.triangleColors = new short[triangleCount];
        if (texTriangleCount > 0) {
            def.texTrianglesA = new short[texTriangleCount];
            def.texTrianglesB = new short[texTriangleCount];
            def.texTrianglesC = new short[texTriangleCount];
            if (particleAmount > 0) {
                def.particleDirectionX = new short[particleAmount];
                def.particleDirectionY = new short[particleAmount];
                def.particleDirectionZ = new short[particleAmount];
                def.particleLifespanX = new byte[particleAmount];
                def.particleLifespanY = new byte[particleAmount];
                def.particleLifespanZ = new byte[particleAmount];
            }

            if (particleColors > 0) {
                def.texturePrimaryColor = new byte[particleColors];
                def.textureSecondaryColor = new byte[particleColors];
            }
        }

        var2.position(texTriangleCount);
        var3.position(var40);
        var4.position(var41);
        var5.position(var42);
        var6.position(var34);
        int var50 = 0;
        int var51 = 0;
        int var52 = 0;

        int var55;
        int var54;
        int var53;
        int var57;
        int var56;
        for (var53 = 0; var53 < vertexCount; ++var53) {
            var54 = var2.get() & 0xFF;
            var55 = 0;
            if ((var54 & 1) != 0) {
                var55 = ByteBufferUtils.method797(var3);
            }

            var56 = 0;
            if ((var54 & 2) != 0) {
                var56 = ByteBufferUtils.method797(var4);
            }

            var57 = 0;
            if ((var54 & 4) != 0) {
                var57 = ByteBufferUtils.method797(var5);
            }

            def.verticesX[var53] = var50 + var55;
            def.verticesY[var53] = var51 + var56;
            def.verticesZ[var53] = var52 + var57;
            var50 = def.verticesX[var53];
            var51 = def.verticesY[var53];
            var52 = def.verticesZ[var53];
            if (var19 == 1) {
                def.vertexSkins[var53] = var6.get() & 0xFF;
            }
        }

        var2.position(var39);
        var3.position(var30);
        var4.position(var32);
        var5.position(var35);
        var6.position(var33);
        var7.position(var37);
        var8.position(var38);

        for (var53 = 0; var53 < triangleCount; ++var53) {
            def.triangleColors[var53] = (short) (var2.getShort() & 0xFFFF);
            if (fillAttributeFlag) {
                def.faceRenderTypes[var53] = var3.get();
            }

            if (var15 == 255) {
                def.trianglePriorities[var53] = var4.get();
            }

            if (var16 == 1) {
                def.triangleAlphas[var53] = var5.get();
            }

            if (var17 == 1) {
                def.triangleSkins[var53] = var6.get() & 0xFF;
            }

            if (var18 == 1) {
                def.faceTextures[var53] = (short) (var7.getShort() & 0xFFFF - 1);
            }

            if (def.textureCoords != null) {
                if (def.faceTextures[var53] != -1) {
                    def.textureCoords[var53] = (byte) (var8.get() & 0xFF - 1);
                } else {
                    def.textureCoords[var53] = -1;
                }
            }
        }

        var2.position(var36);
        var3.position(var31);
        var53 = 0;
        var54 = 0;
        var55 = 0;
        var56 = 0;

        int var58;
        for (var57 = 0; var57 < triangleCount; ++var57) {
            var58 = var3.get() & 0xFF;
            if (var58 == 1) {
                var53 = ByteBufferUtils.method797(var2) + var56;
                var54 = ByteBufferUtils.method797(var2) + var53;
                var55 = ByteBufferUtils.method797(var2) + var54;
                var56 = var55;
                def.trianglesA[var57] = var53;
                def.trianglesB[var57] = var54;
                def.trianglesC[var57] = var55;
            }

            if (var58 == 2) {
                var54 = var55;
                var55 = ByteBufferUtils.method797(var2) + var56;
                var56 = var55;
                def.trianglesA[var57] = var53;
                def.trianglesB[var57] = var54;
                def.trianglesC[var57] = var55;
            }

            if (var58 == 3) {
                var53 = var55;
                var55 = ByteBufferUtils.method797(var2) + var56;
                var56 = var55;
                def.trianglesA[var57] = var53;
                def.trianglesB[var57] = var54;
                def.trianglesC[var57] = var55;
            }

            if (var58 == 4) {
                int var59 = var53;
                var53 = var54;
                var54 = var59;
                var55 = ByteBufferUtils.method797(var2) + var56;
                var56 = var55;
                def.trianglesA[var57] = var53;
                def.trianglesB[var57] = var59;
                def.trianglesC[var57] = var55;
            }
        }

        var2.position(var43);
        var3.position(var44);
        var4.position(var45);
        var5.position(var46);
        var6.position(var47);
        var7.position(var48);

        for (var57 = 0; var57 < texTriangleCount; ++var57) {
            var58 = def.textureRenderTypes[var57] & 255;
            if (var58 == 0) {
                def.texTrianglesA[var57] = (short) (var2.getShort() & 0xFFFF);
                def.texTrianglesB[var57] = (short) (var2.getShort() & 0xFFFF);
                def.texTrianglesC[var57] = (short) (var2.getShort() & 0xFFFF);
            }

            if (var58 == 1) {
                def.texTrianglesA[var57] = (short) (var3.getShort() & 0xFFFF);
                def.texTrianglesB[var57] = (short) (var3.getShort() & 0xFFFF);
                def.texTrianglesC[var57] = (short) (var3.getShort() & 0xFFFF);
                def.particleDirectionX[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleDirectionY[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleDirectionZ[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleLifespanX[var57] = var5.get();
                def.particleLifespanY[var57] = var6.get();
                def.particleLifespanZ[var57] = var7.get();
            }

            if (var58 == 2) {
                def.texTrianglesA[var57] = (short) (var3.getShort() & 0xFFFF);
                def.texTrianglesB[var57] = (short) (var3.getShort() & 0xFFFF);
                def.texTrianglesC[var57] = (short) (var3.getShort() & 0xFFFF);
                def.particleDirectionX[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleDirectionY[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleDirectionZ[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleLifespanX[var57] = var5.get();
                def.particleLifespanY[var57] = var6.get();
                def.particleLifespanZ[var57] = var7.get();
                def.texturePrimaryColor[var57] = var7.get();
                def.textureSecondaryColor[var57] = var7.get();
            }

            if (var58 == 3) {
                def.texTrianglesA[var57] = (short) (var3.getShort() & 0xFFFF);
                def.texTrianglesB[var57] = (short) (var3.getShort() & 0xFFFF);
                def.texTrianglesC[var57] = (short) (var3.getShort() & 0xFFFF);
                def.particleDirectionX[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleDirectionY[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleDirectionZ[var57] = (short) (var4.getShort() & 0xFFFF);
                def.particleLifespanX[var57] = var5.get();
                def.particleLifespanY[var57] = var6.get();
                def.particleLifespanZ[var57] = var7.get();
            }
        }

        if (flag2) {
            var2.position(var28);
            var57 = var2.get() & 0xFF;
            if (var57 > 0) {
                var2.position(var2.position() + (4 * var57));
            }

            var58 = var2.get() & 0xFF;
            if (var58 > 0) {
                var2.position(var2.position() + (4 * var58));
            }
        }
        return def;
    }

    private final static ModelDefinition decodeOldFormat(ByteBuffer data) {
        ModelDefinition def = new ModelDefinition();
        boolean var2 = false;
        boolean var3 = false;
        ByteBuffer var4 = data.asReadOnlyBuffer();
        ByteBuffer var5 = data.asReadOnlyBuffer();
        ByteBuffer var6 = data.asReadOnlyBuffer();
        ByteBuffer var7 = data.asReadOnlyBuffer();
        ByteBuffer var8 = data.asReadOnlyBuffer();
        var4.position(data.limit() - 18);
        int var9 = var4.getShort() & 0xFFFF;
        int var10 = var4.getShort() & 0xFFFF;
        int var11 = var4.get() & 0xFF;
        int var12 = var4.get() & 0xFF;
        int var13 = var4.get() & 0xFF;
        int var14 = var4.get() & 0xFF;
        int var15 = var4.get() & 0xFF;
        int var16 = var4.get() & 0xFF;
        int var17 = var4.getShort() & 0xFFFF;
        int var18 = var4.getShort() & 0xFFFF;
        int var19 = var4.getShort() & 0xFFFF;
        int var20 = var4.getShort() & 0xFFFF;
        byte var21 = 0;
        int var45 = var21 + var9;
        int var23 = var45;
        var45 += var10;
        int var24 = var45;
        if (var13 == 255) {
            var45 += var10;
        }

        int var25 = var45;
        if (var15 == 1) {
            var45 += var10;
        }

        int var26 = var45;
        if (var12 == 1) {
            var45 += var10;
        }

        int var27 = var45;
        if (var16 == 1) {
            var45 += var9;
        }

        int var28 = var45;
        if (var14 == 1) {
            var45 += var10;
        }

        int var29 = var45;
        var45 += var20;
        int var30 = var45;
        var45 += var10 * 2;
        int var31 = var45;
        var45 += var11 * 6;
        int var32 = var45;
        var45 += var17;
        int var33 = var45;
        var45 += var18;
        def.vertexCount = var9;
        def.triangleCount = var10;
        def.texTriangleCount = var11;
        def.verticesX = new int[var9];
        def.verticesY = new int[var9];
        def.verticesZ = new int[var9];
        def.trianglesA = new int[var10];
        def.trianglesB = new int[var10];
        def.trianglesC = new int[var10];
        if (var11 > 0) {
            def.textureRenderTypes = new byte[var11];
            def.texTrianglesA = new short[var11];
            def.texTrianglesB = new short[var11];
            def.texTrianglesC = new short[var11];
        }

        if (var16 == 1) {
            def.vertexSkins = new int[var9];
        }

        if (var12 == 1) {
            def.faceRenderTypes = new byte[var10];
            def.textureCoords = new byte[var10];
            def.faceTextures = new short[var10];
        }

        if (var13 == 255) {
            def.trianglePriorities = new byte[var10];
        } else {
            def.aByte2848 = (byte) var13;
        }

        if (var14 == 1) {
            def.triangleAlphas = new byte[var10];
        }

        if (var15 == 1) {
            def.triangleSkins = new int[var10];
        }

        def.triangleColors = new short[var10];
        var4.position(var21);
        var5.position(var32);
        var6.position(var33);
        var7.position(var45);
        var8.position(var27);
        int var35 = 0;
        int var36 = 0;
        int var37 = 0;

        int var38;
        int var39;
        int var42;
        int var40;
        int var41;
        for (var38 = 0; var38 < var9; ++var38) {
            var39 = var4.get() & 0xFF;
            var40 = 0;
            if ((var39 & 1) != 0) {
                var40 = ByteBufferUtils.method797(var5);
            }

            var41 = 0;
            if ((var39 & 2) != 0) {
                var41 = ByteBufferUtils.method797(var6);
            }

            var42 = 0;
            if ((var39 & 4) != 0) {
                var42 = ByteBufferUtils.method797(var7);
            }

            def.verticesX[var38] = var35 + var40;
            def.verticesY[var38] = var36 + var41;
            def.verticesZ[var38] = var37 + var42;
            var35 = def.verticesX[var38];
            var36 = def.verticesY[var38];
            var37 = def.verticesZ[var38];
            if (var16 == 1) {
                def.vertexSkins[var38] = var8.get() & 0xFF;
            }
        }

        var4.position(var30);
        var5.position(var26);
        var6.position(var24);
        var7.position(var28);
        var8.position(var25);

        for (var38 = 0; var38 < var10; ++var38) {
            def.triangleColors[var38] = (short) (var4.getShort() & 0xFFFF);
            if (var12 == 1) {
                var39 = var5.get() & 0xFF;
                if ((var39 & 1) == 1) {
                    def.faceRenderTypes[var38] = 1;
                    var2 = true;
                } else {
                    def.faceRenderTypes[var38] = 0;
                }

                if ((var39 & 2) == 2) {
                    def.textureCoords[var38] = (byte) (var39 >> 2);
                    def.faceTextures[var38] = def.triangleColors[var38];
                    def.triangleColors[var38] = 127;
                    if (def.faceTextures[var38] != -1) {
                        var3 = true;
                    }
                } else {
                    def.textureCoords[var38] = -1;
                    def.faceTextures[var38] = -1;
                }
            }

            if (var13 == 255) {
                def.trianglePriorities[var38] = var6.get();
            }

            if (var14 == 1) {
                def.triangleAlphas[var38] = var7.get();
            }

            if (var15 == 1) {
                def.triangleSkins[var38] = var8.get() & 0xFF;
            }
        }

        var4.position(var29);
        var5.position(var23);
        var38 = 0;
        var39 = 0;
        var40 = 0;
        var41 = 0;

        int var43;
        int var44;
        for (var42 = 0; var42 < var10; ++var42) {
            var43 = var5.get() & 0xFF;
            if (var43 == 1) {
                var38 = ByteBufferUtils.method797(var4) + var41;
                var39 = ByteBufferUtils.method797(var4) + var38;
                var40 = ByteBufferUtils.method797(var4) + var39;
                var41 = var40;
                def.trianglesA[var42] = var38;
                def.trianglesB[var42] = var39;
                def.trianglesC[var42] = var40;
            }

            if (var43 == 2) {
                var39 = var40;
                var40 = ByteBufferUtils.method797(var4) + var41;
                var41 = var40;
                def.trianglesA[var42] = var38;
                def.trianglesB[var42] = var39;
                def.trianglesC[var42] = var40;
            }

            if (var43 == 3) {
                var38 = var40;
                var40 = ByteBufferUtils.method797(var4) + var41;
                var41 = var40;
                def.trianglesA[var42] = var38;
                def.trianglesB[var42] = var39;
                def.trianglesC[var42] = var40;
            }

            if (var43 == 4) {
                var44 = var38;
                var38 = var39;
                var39 = var44;
                var40 = ByteBufferUtils.method797(var4) + var41;
                var41 = var40;
                def.trianglesA[var42] = var38;
                def.trianglesB[var42] = var44;
                def.trianglesC[var42] = var40;
            }
        }

        var4.position(var31);

        for (var42 = 0; var42 < var11; ++var42) {
            def.textureRenderTypes[var42] = 0;
            def.texTrianglesA[var42] = (short) (var4.getShort() & 0xFFFF);
            def.texTrianglesB[var42] = (short) (var4.getShort() & 0xFFFF);
            def.texTrianglesC[var42] = (short) (var4.getShort() & 0xFFFF);
        }

        if (def.textureCoords != null) {
            boolean var46 = false;

            for (var43 = 0; var43 < var10; ++var43) {
                var44 = def.textureCoords[var43] & 255;
                if (var44 != 255) {
                    if ((def.texTrianglesA[var44] & '\uffff') == def.trianglesA[var43] && (def.texTrianglesB[var44] & '\uffff') == def.trianglesB[var43]
                            && (def.texTrianglesC[var44] & '\uffff') == def.trianglesC[var43]) {
                        def.textureCoords[var43] = -1;
                    } else {
                        var46 = true;
                    }
                }
            }

            if (!var46) {
                def.textureCoords = null;
            }
        }

        if (!var3) {
            def.faceTextures = null;
        }

        if (!var2) {
            def.faceRenderTypes = null;
        }
        return def;
    }

    public void calcDimms(boolean force) {
        if (!force && width >= 0 && height >= 0 && depth >= 0)
            return;

        if (triangleCount == 0) {
            width = 0;
            height = 0;
            depth = 0;
            return;
        }

        int minX = 0x7fffffff;
        int maxX = -0x7fffffff;
        int minY = 0x7fffffff;
        int maxY = -0x7fffffff;
        int minZ = 0x7fffffff;
        int maxZ = -0x7fffffff;
        for (int i = 0; i != triangleCount; ++i) {
            int t = trianglesA[i];
            if (maxX < verticesX[t])
                maxX = verticesX[t];

            if (minX > verticesX[t])
                minX = verticesX[t];

            if (maxY < verticesY[t])
                maxY = verticesY[t];

            if (minY > verticesY[t])
                minY = verticesY[t];

            if (maxZ < verticesZ[t])
                maxZ = verticesZ[t];

            if (minZ > verticesZ[t])
                minZ = verticesZ[t];

            t = trianglesB[i];
            if (maxX < verticesX[t])
                maxX = verticesX[t];

            if (minX > verticesX[t])
                minY = verticesX[t];

            if (maxY < verticesY[t])
                maxY = verticesY[t];

            if (minY > verticesY[t])
                minY = verticesY[t];

            if (maxZ < verticesZ[t])
                maxZ = verticesZ[t];

            if (minZ > verticesZ[t])
                minZ = verticesZ[t];

            t = trianglesC[i];
            if (maxX < verticesX[t])
                maxX = verticesX[t];

            if (minX > verticesX[t])
                minX = verticesX[t];

            if (maxY < verticesY[t])
                maxY = verticesY[t];

            if (minY > verticesY[t])
                minY = verticesY[t];

            if (maxZ < verticesZ[t])
                maxZ = verticesZ[t];

            if (minZ > verticesZ[t])
                minZ = verticesZ[t];

        }

        width = maxX - minX;
        height = maxY - minY;
        depth = maxZ - minZ;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public int getWidth() {
        return width;
    }

    public byte[] getTriangleAlphas() {
        return triangleAlphas;
    }

    public short getaShort2844() {
        return aShort2844;
    }

    public byte[] getParticleLifespansX() {
        return particleLifespanX;
    }

    public short[] getTexTrianglesB() {
        return texTrianglesB;
    }

    public int[] getTriangleSkins() {
        return triangleSkins;
    }

    public byte getaByte2848() {
        return aByte2848;
    }

    public int getTriangleCount() {
        return triangleCount;
    }

    public short getaShort2850() {
        return aShort2850;
    }

    public short[] getParticleDirectionsZ() {
        return particleDirectionZ;
    }

    public byte[] getTexturePrimaryColors() {
        return texturePrimaryColor;
    }

    public boolean isNewFormat() {
        return isNewFormat;
    }

    public short getaShort2854() {
        return aShort2854;
    }

    public short[] getaShortArray2855() {
        return aShortArray2855;
    }

    public int[][] getAnIntArrayArray2856() {
        return anIntArrayArray2856;
    }

    public byte[] getTextureRenderTypes() {
        return textureRenderTypes;
    }

    public short[] getFaceTextures() {
        return faceTextures;
    }

    public byte[] getFaceRenderTypes() {
        return faceRenderTypes;
    }

    public int[] getVertexSkins() {
        return vertexSkins;
    }

    public int getAnInt2862() {
        return texTriangleCount;
    }

    public int[] getTrianglesC() {
        return trianglesC;
    }

    public int[] getTrianglesA() {
        return trianglesA;
    }

    public byte[] getTextureCoords() {
        return textureCoords;
    }

    public byte[] getParticleLifespansY() {
        return particleLifespanY;
    }

    public byte[] getTextureSecondaryColors() {
        return textureSecondaryColor;
    }

    public short[] getTriangleColors() {
        return triangleColors;
    }

    public short getaShort2873() {
        return aShort2873;
    }

    public short getaShort2874() {
        return aShort2874;
    }

    public static int[] getAnIntArray2875() {
        return anIntArray2875;
    }

    public short getaShort2876() {
        return aShort2876;
    }

    public byte[] getParticleLifespansZ() {
        return particleLifespanZ;
    }

    public int[] getTrianglesB() {
        return trianglesB;
    }

    public short getaShort2879() {
        return aShort2879;
    }

    public short getaShort2880() {
        return aShort2880;
    }

    public int[] getVerticesY() {
        return verticesY;
    }

    public short[] getParticleDirectionsY() {
        return particleDirectionY;
    }

    public short[] getTexTrianglesA() {
        return texTrianglesA;
    }

    public int[] getVerticesX() {
        return verticesX;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public short[] getParticleDirectionsX() {
        return particleDirectionX;
    }

    public byte[] getTrianglePriorities() {
        return trianglePriorities;
    }

    public int[][] getAnIntArrayArray2890() {
        return anIntArrayArray2890;
    }

    public short[] getTexTrianglesC() {
        return texTrianglesC;
    }

    public int[] getVerticesZ() {
        return verticesZ;
    }

    public short[] getaShortArray2893() {
        return aShortArray2893;
    }
}
