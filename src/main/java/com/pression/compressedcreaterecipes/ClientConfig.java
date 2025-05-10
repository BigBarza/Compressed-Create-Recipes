package com.pression.compressedcreaterecipes;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SALVAGE_DISPLAY;


    static {
        BUILDER.push("Compressed Create Recipes Config");
        ENABLE_SALVAGE_DISPLAY = BUILDER.comment("Whether to modify the JEI category for sequenced assembly to show random salvage. Does nothing if Create: Cyber Goggles is present")
                        .define("Random Salvage Display", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}