package com.pression.compressedcreaterecipes.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//This mixin "backports" a fix from 1.20.
//When items that have a crafting remaining items were tossed in along a mixing recipe, they would not be consumed, as expected.
//BUT also create the remaining item. Which is...not good. This fixes that issue.
@Mixin(BasinRecipe.class)
public class BasinRecipeMixin {

    @Redirect(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", remap = true,
    at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;getRemainingItems(Lnet/minecraft/world/Container;)Lnet/minecraft/core/NonNullList;"))
    //I don't know why intellij complains that it can't find those locals. They work. For future reference, to get the index like that, you need a bytecode viewer.
    private static NonNullList<ItemStack> fixContainer(BasinRecipe instance, Container container, @Local(index = 4) IItemHandler availableItems, @Local(index = 15) int[] extractedItemsFromSlot){
        CraftingContainer dummy = new DummyCraftingContainer(availableItems, extractedItemsFromSlot);
        //We rebuild the list and pass it like that because it normally expects a SmartInventory since unlike the true 1.20 fix, this doesn't change the type parameter of the recipe.
        NonNullList<ItemStack> remainingItems = NonNullList.create();

        for(int i = 0;i<dummy.getContainerSize();i++){
            ItemStack item = dummy.getItem(i);
            if(item.hasCraftingRemainingItem()){
                remainingItems.add(item.getCraftingRemainingItem());
            }
        }

        return remainingItems;
    }


}
