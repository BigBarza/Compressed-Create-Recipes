package com.pression.compressedmystconv.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//This is the recipe for chucking stuff into the void.
public class VoidConversionRecipe implements Recipe<Inventory> {
    private final ResourceLocation id;
    private final ItemStack input;
    private final ItemStack output;

    private static List<Item> inputsCache = new ArrayList<>();

    public VoidConversionRecipe(ResourceLocation id, ItemStack in, ItemStack out){
        this.id = id;
        this.input = in;
        this.output = out;
    }

    @Nullable
    public static VoidConversionRecipe getRecipe(Level level, ItemStack item){
        if(inputsCache.isEmpty()){ //This whole cache thing is blatantly borrowed from AE2. Minimizes the logic run every time an items crosses into the void.
            List<VoidConversionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.VOID_CONVERSION_RECIPE_TYPE.get());
            for(VoidConversionRecipe recipe : recipes) inputsCache.add(recipe.getInput().getItem());
        }
        if(!inputsCache.contains(item.getItem())) return null;
        //At this point we've established that a void conversion is about to happen, so it's ok to fetch recipes.
        List<VoidConversionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.VOID_CONVERSION_RECIPE_TYPE.get());
        for(VoidConversionRecipe recipe : recipes){
            if(recipe.getInput().getItem() == item.getItem()) return recipe;
        }
        return null; //This should never happen, but we gotta return something in the end so...
    }

    public static void wipeCache(){
        inputsCache = new ArrayList<>();
    }

    @Override public ResourceLocation getId(){ return id; }
    public ItemStack getInput(){ return input.copy(); }
    public ItemStack getOutput(){ return output.copy(); }
    @Override public ItemStack getResultItem(){ return output; }
    @Override public boolean matches(Inventory inv, Level world){ return false; }
    @Override public ItemStack assemble(Inventory inv){ return ItemStack.EMPTY; }
    @Override public boolean canCraftInDimensions(int w, int h){ return false; }
    @Override public RecipeSerializer<?> getSerializer(){
        return CompressionRecipeTypes.VOID_CONVERSION_SERIALIZER.get();
    }
    @Override public RecipeType<?> getType(){
        return CompressionRecipeTypes.VOID_CONVERSION_RECIPE_TYPE.get();
    }


    public static class Serializer implements RecipeSerializer<VoidConversionRecipe>{
        @Override
        public VoidConversionRecipe fromJson(ResourceLocation id, JsonObject json){
            ItemStack in = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack out = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            return new VoidConversionRecipe(id, in, out);
        }
        @Override
        public @Nullable VoidConversionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            ItemStack in = buf.readItem();
            ItemStack out = buf.readItem();
            return new VoidConversionRecipe(id, in, out);
        }
        @Override
        public void toNetwork(FriendlyByteBuf buf, VoidConversionRecipe recipe){
            buf.writeItem(recipe.getInput());
            buf.writeItem(recipe.getOutput());
        }

    }


}
