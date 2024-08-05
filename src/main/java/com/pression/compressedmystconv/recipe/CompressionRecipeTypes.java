package com.pression.compressedmystconv.recipe;

import com.pression.compressedmystconv.CompressedMystConv;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CompressionRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CompressedMystConv.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CompressedMystConv.MODID);

    public static final RegistryObject<RecipeType<VoidConversionRecipe>> VOID_CONVERSION_RECIPE_TYPE = RECIPE_TYPES.register("void_conversion",
            () -> new RecipeType<VoidConversionRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressedMystConv.MODID, "void_conversion").toString();
                }
            }
            );
    public static final RegistryObject<RecipeSerializer<VoidConversionRecipe>> VOID_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("void_conversion", VoidConversionRecipe.Serializer::new);


}
