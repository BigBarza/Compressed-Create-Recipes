package com.pression.compressedmystconv;

import com.mojang.logging.LogUtils;
import com.pression.compressedmystconv.recipe.CompressionRecipeTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(CompressedMystConv.MODID)
public class CompressedMystConv
{
    public static final String MODID = "compressedmystconv";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CompressedMystConv(){
        LOGGER.info("Hexagons are the bestagons!");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CompressionRecipeTypes.RECIPE_TYPES.register(modEventBus);
        CompressionRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}