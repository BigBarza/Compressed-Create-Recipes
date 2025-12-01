package com.pression.compressedcreaterecipes.recipe;

import com.google.gson.JsonObject;
import com.pression.compressedcreaterecipes.helpers.MystConversionRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class DummyConversionRecipe extends MystConversionRecipe {

    public DummyConversionRecipe(ResourceLocation id, ItemStack in, ItemStack out){
        super(id, in, out);
    }

    @Override public RecipeType<?> getType(){
        return CompressionRecipeTypes.DUMMY_CONVERSION_RECIPE_TYPE.get();
    }
    @Override public RecipeSerializer<?> getSerializer(){
        return CompressionRecipeTypes.DUMMY_CONVERSION_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<DummyConversionRecipe> {

        @Override
        public DummyConversionRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack in = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack out = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            return new DummyConversionRecipe(id, in, out);
        }

        @Override
        public @Nullable DummyConversionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack in = buf.readItem();
            ItemStack out = buf.readItem();
            return new DummyConversionRecipe(id, in, out);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DummyConversionRecipe recipe) {
            buf.writeItem(recipe.getInput());
            buf.writeItem(recipe.getOutput());
        }
    }
}
