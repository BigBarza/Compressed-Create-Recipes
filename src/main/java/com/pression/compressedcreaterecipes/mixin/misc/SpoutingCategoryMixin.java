package com.pression.compressedcreaterecipes.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.SpoutCategory;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.item.ItemStack;
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
    float capturedChance = 1;

     @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("HEAD"), remap = false)
     private void captureChance(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
         capturedChance = recipe.getRollableResults().get(0).getChance();
     }

    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;addSlot(Lmezz/jei/api/recipe/RecipeIngredientRole;II)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;"), remap = false)
    private IRecipeSlotBuilder moveNormalOutput(IRecipeLayoutBuilder instance, RecipeIngredientRole recipeIngredientRole, int x, int y){
        if(recipeIngredientRole == RecipeIngredientRole.OUTPUT) return instance.addSlot(recipeIngredientRole, 133, 51);
        else return instance.addSlot(recipeIngredientRole, x, y);
    }

    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;setBackground(Lmezz/jei/api/gui/drawable/IDrawable;II)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;", ordinal = 2), remap = false)
    private IRecipeSlotBuilder setFirstBG(IRecipeSlotBuilder instance, IDrawable iDrawable, int x, int y){
        return instance.setBackground(getRenderedSlot(capturedChance), x, y).addTooltipCallback(addStochasticTooltip(new ProcessingOutput(ItemStack.EMPTY, capturedChance)));
    }



    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At("TAIL"), remap = false)
    private void addSlots(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int i = 0;
        for (ProcessingOutput output : results) {
            if(i == 0){
                i++;
                continue;
            }
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = (i / 2) * -19;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset, 51 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addTooltipCallback(addStochasticTooltip(output));
            i++;
        }
    }

    @Redirect(method = "draw(Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V",
    at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/gui/AllGuiTextures;render(Lcom/mojang/blaze3d/vertex/PoseStack;II)V", ordinal = 1), remap = false)
    private void cancelArrow(AllGuiTextures instance, PoseStack ms, int x, int y){}

    @Inject(method = "draw(Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V", at = @At("HEAD"), remap = false)
    private void redrawArrow(FillingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY, CallbackInfo ci){
         if(recipe.getRollableResults().size() >= 3){
             AllGuiTextures.JEI_ARROW.bind();//.render(matrixStack, 100, 29);
             GuiComponent.blit(matrixStack, 105, 29, 39, 10, 22, 10, 256,256);
         }
         else AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 126, 29);
    }

}
