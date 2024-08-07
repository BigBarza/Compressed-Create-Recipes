package com.pression.compressedmystconv.mixin;

import com.pression.compressedmystconv.recipe.CompressionRecipeTypes;
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
@Mixin(MysteriousItemConversionCategory.class)
public class CreateJEIMixin {

    @Unique
    private static List<ConversionRecipe> originalRecipes = new ArrayList<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci){
        if(originalRecipes.isEmpty()){
            originalRecipes.addAll(MysteriousItemConversionCategory.RECIPES);
            addRecipes();
        }else{ //Turns out, it didn't actually wipe the recipes before readding them. So it would just...add a new set on top.
            MysteriousItemConversionCategory.RECIPES.clear();
            MysteriousItemConversionCategory.RECIPES.addAll(originalRecipes);
            addRecipes();
        }

    }

    @Unique
    private void addRecipes(){
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.VOID_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.RADIANT_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
    }
}
