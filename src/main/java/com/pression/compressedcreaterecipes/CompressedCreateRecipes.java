package com.pression.compressedcreaterecipes;

import com.mojang.logging.LogUtils;
import com.pression.compressedcreaterecipes.recipe.CompressionRecipeTypes;
import com.pression.compressedcreaterecipes.recipe.RadiantConversionRecipe;
import com.pression.compressedcreaterecipes.recipe.VoidConversionRecipe;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CompressedCreateRecipes.MODID)
public class CompressedCreateRecipes
{
    public static final String MODID = "compressed_create_recipes";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CompressedCreateRecipes(){
        LOGGER.info("Hexagons are the bestagons!");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CompressionRecipeTypes.RECIPE_TYPES.register(modEventBus);
        CompressionRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    //Doing all 3 is probably overkill and won't be needed once the recipes are finalized.
    //But it is important to wipe the caches, otherwise newly added inputs won't be recognized.
    public static class EventHandler {
        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent e){
            VoidConversionRecipe.wipeCache();
            RadiantConversionRecipe.wipeCache();
        }
        @SubscribeEvent
        public static void onReloadServerResources(AddReloadListenerEvent e){
            VoidConversionRecipe.wipeCache();
            RadiantConversionRecipe.wipeCache();
        }
        @SubscribeEvent
        public static void onClientRecipesUpdated(RecipesUpdatedEvent e){
            VoidConversionRecipe.wipeCache();
            RadiantConversionRecipe.wipeCache();
        }
    }


}