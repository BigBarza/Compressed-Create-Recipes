package com.pression.compressedcreaterecipes.mixin.misc;

import com.simibubi.create.compat.jei.category.SpoutCategory;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.addStochasticTooltip;
import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

@Mixin(SpoutCategory.class)
public class SpoutingCategoryMixin {
    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;addSlot(Lmezz/jei/api/recipe/RecipeIngredientRole;II)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;"), remap = false)
    private IRecipeSlotBuilder blockNormalOutput(IRecipeLayoutBuilder instance, RecipeIngredientRole recipeIngredientRole, int x, int y){
        if(recipeIngredientRole == RecipeIngredientRole.OUTPUT) return null;
        else return instance.addSlot(recipeIngredientRole, x, y-16);
    }
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At("TAIL"), remap = false)
    private void addSlots(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int i = 0;
        for (ProcessingOutput output : results) {
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = (i / 2) * -19;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset, 27 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addTooltipCallback(addStochasticTooltip(output));

            i++;
        }
    }
}
