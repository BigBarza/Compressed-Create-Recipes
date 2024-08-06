package com.pression.compressedmystconv;

import com.mojang.logging.LogUtils;
import com.pression.compressedmystconv.recipe.CompressionRecipeTypes;
import com.pression.compressedmystconv.recipe.RadiantConversionRecipe;
import com.pression.compressedmystconv.recipe.VoidConversionRecipe;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

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
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    //Doing all 3 is probably overkill and won't be needed once the recipes are finalized.
    //But it is important to wipe the caches, otherwise newly added inputs won't be recognized.
    public static class EventHandler {
        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent e){
            System.out.println("Server started, wiping caches...");
            VoidConversionRecipe.wipeCache();
            RadiantConversionRecipe.wipeCache();
        }
        @SubscribeEvent
        public static void onReloadServerResources(AddReloadListenerEvent e){
            System.out.println("Server reloaded, wiping caches...");
            VoidConversionRecipe.wipeCache();
            RadiantConversionRecipe.wipeCache();
        }
        @SubscribeEvent
        public static void onClientRecipesUpdated(RecipesUpdatedEvent e){
            System.out.println("Recipes updated, wiping caches...");
            VoidConversionRecipe.wipeCache();
            RadiantConversionRecipe.wipeCache();
        }
    }


}