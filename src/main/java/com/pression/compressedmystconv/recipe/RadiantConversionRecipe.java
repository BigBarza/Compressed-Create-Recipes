package com.pression.compressedmystconv.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//This is the recipe for chucking stuff into an active beacon. Nearly identical to the void one, but with a beacon level requirement.
public class RadiantConversionRecipe implements Recipe<Inventory> {
    private final ResourceLocation id;
    private final ItemStack input;
    private final ItemStack output;
    private final int minLevel;

    private static List<Item> inputsCache = new ArrayList<>();

    public RadiantConversionRecipe(ResourceLocation id, ItemStack in, ItemStack out, int minLevel){
        this.id = id;
        this.input = in;
        this.output = out;
        this.minLevel = minLevel;
    }

    @Nullable
    public static RadiantConversionRecipe getRecipe(Level level, ItemStack item, int beaconLevel){
        if(inputsCache.isEmpty()){ //This whole cache thing is blatantly borrowed from AE2. Minimizes the logic run every time an items crosses into the beacon beam.
            List<RadiantConversionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.RADIANT_CONVERSION_RECIPE_TYPE.get());
            for(RadiantConversionRecipe recipe : recipes) inputsCache.add(recipe.getInput().getItem());
        }
        if(!inputsCache.contains(item.getItem())) return null;
        //At this point we've established that a radiant conversion is about to happen, so it's ok to fetch recipes.
        List<RadiantConversionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.RADIANT_CONVERSION_RECIPE_TYPE.get());
        for(RadiantConversionRecipe recipe : recipes){
            if(recipe.getInput().getItem() == item.getItem() && beaconLevel >= recipe.getMinLevel()) return recipe;
        }
        return null; //This can happen if an item matches the cache, but fails the level check.
    }

    public static void wipeCache(){
        inputsCache = new ArrayList<>();
    }

    @Override public ResourceLocation getId(){ return id; }
    public ItemStack getInput(){ return input.copy(); }
    public ItemStack getOutput(){ return output.copy(); }
    public int getMinLevel(){ return minLevel; }
    @Override public ItemStack getResultItem(){ return output; }
    @Override public boolean matches(Inventory inv, Level world){ return false; }
    @Override public ItemStack assemble(Inventory inv){ return ItemStack.EMPTY; }
    @Override public boolean canCraftInDimensions(int w, int h){ return false; }
    @Override public RecipeSerializer<?> getSerializer(){
        return CompressionRecipeTypes.RADIANT_CONVERSION_SERIALIZER.get();
    }
    @Override public RecipeType<?> getType(){
        return CompressionRecipeTypes.RADIANT_CONVERSION_RECIPE_TYPE.get();
    }


    public static class Serializer implements RecipeSerializer<RadiantConversionRecipe>{
        @Override
        public RadiantConversionRecipe fromJson(ResourceLocation id, JsonObject json){
            ItemStack in = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack out = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int level = 1;
            if(json.has("level")) level = GsonHelper.getAsInt(json, "level");
            return new RadiantConversionRecipe(id, in, out, level);
        }
        @Override
        public @Nullable RadiantConversionRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            ItemStack in = buf.readItem();
            ItemStack out = buf.readItem();
            int level = buf.readInt();
            return new RadiantConversionRecipe(id, in, out, level);
        }
        @Override
        public void toNetwork(FriendlyByteBuf buf, RadiantConversionRecipe recipe){
            buf.writeItem(recipe.getInput());
            buf.writeItem(recipe.getOutput());
            buf.writeInt(recipe.getMinLevel());
        }

    }


}
