package com.pression.compressedcreaterecipes.recipe;

import com.pression.compressedcreaterecipes.CompressedCreateRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

//Recipe registry stuff goes here. To add new ones, just copy one and change the names.
public class CompressionRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CompressedCreateRecipes.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CompressedCreateRecipes.MODID);

    //VOID CONVERSION
    public static final RegistryObject<RecipeType<VoidConversionRecipe>> VOID_CONVERSION_RECIPE_TYPE = RECIPE_TYPES.register("void_conversion",
            () -> new RecipeType<VoidConversionRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressedCreateRecipes.MODID, "void_conversion").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<VoidConversionRecipe>> VOID_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("void_conversion", VoidConversionRecipe.Serializer::new);

    //RADIANT CONVERSION
    public static final RegistryObject<RecipeType<RadiantConversionRecipe>> RADIANT_CONVERSION_RECIPE_TYPE = RECIPE_TYPES.register("radiant_conversion",
            () -> new RecipeType<RadiantConversionRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressedCreateRecipes.MODID, "radiant_conversion").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<RadiantConversionRecipe>> RADIANT_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("radiant_conversion", RadiantConversionRecipe.Serializer::new);


}
