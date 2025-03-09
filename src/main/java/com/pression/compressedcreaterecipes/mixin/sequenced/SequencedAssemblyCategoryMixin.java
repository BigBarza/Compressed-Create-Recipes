package com.pression.compressedcreaterecipes.mixin.sequenced;

import com.pression.compressedcreaterecipes.helpers.ISequencedProcessingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.SequencedAssemblyCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SequencedAssemblyCategory.class)
public abstract class SequencedAssemblyCategoryMixin {

    @Shadow(remap = false) protected abstract MutableComponent chanceComponent(float chance);
    //Just a tweak to let the builtin chance component support 100%
    @Inject(method = "chanceComponent", at = @At("RETURN"), remap = false, cancellable = true)
    private void tweakChanceComponent(float chance, CallbackInfoReturnable<MutableComponent> cir){
        if(chance == 1) cir.setReturnValue(Component.translatable("create.recipe.processing.chance", "100")
                .withStyle(ChatFormatting.GOLD));
    }

    //This captures the addition of the input and first output slots.
    //If it's a processing recipe, we need to move the first output in place.
    //Note that the methods chained afterwards still fire, so we don't have control over those.
    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;addSlot(Lmezz/jei/api/recipe/RecipeIngredientRole;II)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;"),
    remap = false)
    private IRecipeSlotBuilder onAddSlot(IRecipeLayoutBuilder instance, RecipeIngredientRole recipeIngredientRole, int x, int y, IRecipeLayoutBuilder builder, SequencedAssemblyRecipe recipe, IFocusGroup focuses){
        if(recipeIngredientRole == RecipeIngredientRole.OUTPUT){
            //If it's a processing recipe, move the output slot. But only if there's more than 1 output
            if(((ISequencedProcessingRecipe)recipe).isProcessing() && recipe.resultPool.size() > 1){
                y = 118;
                x = 82 - (recipe.resultPool.size()-1)*9;
            }
            return builder.addSlot(RecipeIngredientRole.OUTPUT, x, y);
        }
        return builder.addSlot(RecipeIngredientRole.INPUT, x, y);
    }

    //This is because EMI fucking overwrites the tooltip with the first call.
    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;addTooltipCallback(Lmezz/jei/api/gui/ingredient/IRecipeSlotTooltipCallback;)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;"),
    remap = false)
    private IRecipeSlotBuilder onTooltipCallback(IRecipeSlotBuilder instance, IRecipeSlotTooltipCallback iRecipeSlotTooltipCallback, IRecipeLayoutBuilder builder, SequencedAssemblyRecipe recipe, IFocusGroup focuses){
        return instance.addTooltipCallback((recipeSlotView, tooltip) -> {
            tooltip.add(1, chanceComponent(recalcChance(recipe, recipe.resultPool.get(0).getChance())));
        });
    }

    //This inject adds all the slots at the bottom. Note that the first output is already taken care of, but must be accounted for in positioning
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
    at = @At("TAIL"), remap = false)
    private void addOtherOutputs(IRecipeLayoutBuilder builder, SequencedAssemblyRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        boolean processing = ((ISequencedProcessingRecipe)recipe).isProcessing();

        int xOffset = 82 - (recipe.resultPool.size()-1)*9 + (processing ? 18 : 9);
        for(int i = 1; i<recipe.resultPool.size(); i++){
            ProcessingOutput out = recipe.resultPool.get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset, 118)
                    .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                    .addItemStack(out.getStack())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        tooltip.add(1, chanceComponent(recalcChance(recipe, out.getChance())));
                        if(!processing) tooltip.add(2, Component.translatable("create.recipe.assembly.junk"));
                    });
            xOffset+=18;
        }
    }

    //This inject adds an indicator slot where the output used to be if it's a processing recipe
    //It also adds some filler text if there's nothing to display down there.
    @Inject(method = "draw(Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
    at = @At("TAIL"), remap = false)
    private void onDraw(SequencedAssemblyRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci){
        boolean processing = ((ISequencedProcessingRecipe)recipe).isProcessing();
        Font font = Minecraft.getInstance().font;
        if(processing && recipe.resultPool.size() > 1){
            //Yes, this is the ? we tricked it to not display. We want it at the normal output position to fill it in.
            AllGuiTextures.JEI_CHANCE_SLOT.render(graphics, 131, 90);
            Component component = Component.literal("?").withStyle(ChatFormatting.BOLD);
            graphics.drawString(font, component, font.width(component) / -2 + 8 + 150 - 18, 2 + 93, 0xefefef);
            //font.drawShadow(graphics, component, (float) font.width(component) / -2 + 8 + 150 - 18, 2 + 93, 0xefefef);
        }
        if(recipe.resultPool.size() <= 1){ //If there's nothing to display down there, put some filler text
            Component noSideOutputs = Component.translatable("compressedcreaterecipes.jei.nosideoutputs");
            graphics.drawString(font, noSideOutputs.getVisualOrderText(), 90-((float) font.width(noSideOutputs.getString()) /2), 120, 0x888888, false);
            //font.draw(matrixStack, noSideOutputs, 90-((float) font.width(noSideOutputs.getString()) /2), 120, 0x888888);
        }
    }

    @Inject(method = "getTooltipStrings(Lcom/simibubi/create/content/processing/sequenced/SequencedAssemblyRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;DD)Ljava/util/List;",
    at = @At("HEAD"), remap = false, cancellable = true)
    private void addProcessingTooltip(SequencedAssemblyRecipe recipe, IRecipeSlotsView iRecipeSlotsView, double mouseX, double mouseY, CallbackInfoReturnable<List<Component>> cir){
        List<Component> tooltip = new ArrayList<>();
        boolean processing = ((ISequencedProcessingRecipe)recipe).isProcessing();

        if(processing && recipe.resultPool.size() > 1 && mouseX >= 131 && mouseX < 149 && mouseY >= 90 && mouseY < 108){
            tooltip.add(Component.translatable("compressedcreaterecipes.jei.processing_1"));
            tooltip.add(Component.translatable("compressedcreaterecipes.jei.processing_2"));
            tooltip.add(Component.translatable("compressedcreaterecipes.jei.processing_3"));
            cir.setReturnValue(tooltip);
        }
    }

    @Unique
    private float recalcChance(SequencedAssemblyRecipe recipe, float chance){
        float totalWeight = 0;
        for(ProcessingOutput output : recipe.resultPool){
            totalWeight += output.getChance();
        }
        return chance/totalWeight;
    }

}
