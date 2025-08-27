package com.pression.compressedcreaterecipes.mixin.conversions;

import com.pression.compressedcreaterecipes.CompressedCreateRecipes;
import com.pression.compressedcreaterecipes.recipe.CompressionRecipeTypes;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//The list of "Mysterious Conversion" recipes in Create is hardcoded in a static field.
//This mixin adds them to that list. This still works when reloading recipes. Somehow.
//If Create is not there, this just throws an error once and does nothing. Which also means the recipes won't show up in jei.
@Mixin(MysteriousItemConversionCategory.class)
//@Implements(@Interface(iface = IRecipeCategory.class, prefix = "jei$", remap = Interface.Remap.NONE))
public abstract class ConversionCategoryMixin implements IRecipeCategory<ConversionRecipe>{

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
        Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CompressionRecipeTypes.DUMMY_CONVERSION_RECIPE_TYPE.get()).forEach(recipe -> {
            MysteriousItemConversionCategory.RECIPES.add(ConversionRecipe.create(recipe.getInput(), recipe.getOutput()));
        });
    }

    @Override
     public @NotNull List<Component> getTooltipStrings(@NotNull ConversionRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltips = new ArrayList<>();
        if(mouseX < 44 || mouseX > 115) return tooltips; //bounds checking
        int index = 1;
        while (true) { //please don't break.
            String tooltipID = CompressedCreateRecipes.MODID+".jei."+recipe.getId().getPath()+".line"+index;
            if(!I18n.exists(tooltipID)) break;
            else {
                tooltips.add(Component.translatable(tooltipID));
            }
            index++;
        }
        return tooltips;
    }

}
