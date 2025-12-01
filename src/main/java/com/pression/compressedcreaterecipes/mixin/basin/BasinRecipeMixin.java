package com.pression.compressedcreaterecipes.mixin.basin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import net.minecraft.world.Container;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//This mixin "backports" a fix from 1.20.
//When items that have a crafting remaining items were tossed in along a mixing recipe, they would not be consumed, as expected.
//BUT also create the remaining item. Which is...not good. This fixes that issue.
@Mixin(BasinRecipe.class)
public class BasinRecipeMixin {

    @ModifyArg(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z",
    at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;getRemainingItems(Lnet/minecraft/world/Container;)Lnet/minecraft/core/NonNullList;"), index = 0)
    private static Container filterInv(Container par1, @Local(index = 4) IItemHandler availableItems, @Local(index = 15) int[] extractedItemsFromSlot){
        return new DummyCraftingContainer(availableItems, extractedItemsFromSlot);
    }

}
