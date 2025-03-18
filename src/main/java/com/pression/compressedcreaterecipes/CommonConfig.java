package com.pression.compressedcreaterecipes;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> BASIN_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> SANDING_SPEED;
    public static final ForgeConfigSpec.ConfigValue<Integer> UNVOID_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Double> RADIANT_DELTA_MULT;
    public static final ForgeConfigSpec.ConfigValue<Integer> RADIANT_PICKUP_DELAY;

    static {
        BUILDER.push("Compressed Create Recipes Config");
        BASIN_CAPACITY = BUILDER.comment("Capacity of the basin's tanks, in millibuckets. (Default: 1000)")
                .defineInRange("Basin Capacity", 1000, 1, Integer.MAX_VALUE);
        SANDING_SPEED = BUILDER.comment("Duration of the sandpaper polishing animation in ticks. Lower values mean you polish faster. (Default: 32)")
                .defineInRange("Sandpaper Speed", 32, 1, Integer.MAX_VALUE);
        UNVOID_RANGE = BUILDER.comment("The radius in which items rising from the void will search for an available spot. Low values might get the item stuck under bedrock. High values might cause performance issues. (Default: 20)")
                .defineInRange("Un-Voiding Radius", 20, 1, 100);
        RADIANT_DELTA_MULT = BUILDER.comment("A multiplier on the momentum applied to items created from radiant conversion (Default: 1)")
                .defineInRange("Radiant Momentum Multiplier", 1d, 0d, 50d);
        RADIANT_PICKUP_DELAY = BUILDER.comment("The delay in ticks before an item created from radiant conversion can be picked up. Set to 0 to allow instant pickup. Very high values are not recommended. (Default: 10)")
                .defineInRange("Radiant Pickup Delay", 10, 0, Integer.MAX_VALUE);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }


}
