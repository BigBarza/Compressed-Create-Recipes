package com.pression.compressedmystconv.helpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

//Try to not mix this up with Create's ConversionRecipe for jei...
//This is a helper class to store bits that are common across all conversion recipes
public abstract class MystConversionRecipe implements Recipe<Inventory> {
    private final ResourceLocation id;
    private final ItemStack input;
    private final ItemStack output;

    public MystConversionRecipe(ResourceLocation id, ItemStack input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public ItemStack getInput(){
        return input.copy();
    }

    public ItemStack getOutput(){
        return output.copy();
    }

    @Override
    public boolean matches(Inventory p_44002_, Level p_44003_) {
        return false;
    }

    @Override
    public ItemStack assemble(Inventory p_44001_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
