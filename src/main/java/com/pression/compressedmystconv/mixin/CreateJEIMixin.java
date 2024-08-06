package com.pression.compressedmystconv.mixin;

import com.pression.compressedmystconv.recipe.CompressionRecipeTypes;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//The list of "Mysterious Conversion" recipes in Create is hardcoded in a static field.
//This mixin adds them to that list. This still works when reloading recipes. Somehow.
@Mixin(MysteriousItemConversionCategory.class)
public class CreateJEIMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectRecipes(CallbackInfo ci){
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.VOID_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.RADIANT_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
    }
}
