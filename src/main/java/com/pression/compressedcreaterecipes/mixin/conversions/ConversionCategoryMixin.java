package com.pression.compressedcreaterecipes.mixin.conversions;

import com.pression.compressedcreaterecipes.recipe.CompressionRecipeTypes;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//The list of "Mysterious Conversion" recipes in Create is hardcoded in a static field.
//This mixin adds them to that list. This still works when reloading recipes. Somehow.
//If Create is not there, this just throws an error once and does nothing. Which also means the recipes won't show up in jei.
@Mixin(MysteriousItemConversionCategory.class)
public class ConversionCategoryMixin {

    @Unique //This serves as a sort of cache for the original recipes, as we need to rebuild the list on a /reload
    private static List<ConversionRecipe> originalRecipes = new ArrayList<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci){
        if(originalRecipes.isEmpty()){
            originalRecipes.addAll(MysteriousItemConversionCategory.RECIPES);
            addRecipes();
        }else{ //Turns out, it didn't actually wipe the recipes before readding them. So it would just...add a new set on top. whoops.
            MysteriousItemConversionCategory.RECIPES.clear();
            MysteriousItemConversionCategory.RECIPES.addAll(originalRecipes);
            addRecipes();
        }

    }

    @Unique //Whenever new recipe types get added, just add more of these.
    private void addRecipes(){
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.VOID_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.RADIANT_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
    }
}
