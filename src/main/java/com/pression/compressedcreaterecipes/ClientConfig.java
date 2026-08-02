package com.pression.compressedcreaterecipes;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SALVAGE_DISPLAY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TICK_ATLAS_IN_PONDERS;


    static {
        BUILDER.push("Compressed Create Recipes Config");
        ENABLE_SALVAGE_DISPLAY = BUILDER.comment("Whether to modify the JEI category for sequenced assembly to show random salvage. Does nothing if Create: Cyber Goggles is present")
                        .define("Random Salvage Display", true);
        TICK_ATLAS_IN_PONDERS = BUILDER.comment("Whether to force animated textures to keep animating in ponders. Only affects singleplayer.")
                .define("Force Animated Textures in Ponders", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}