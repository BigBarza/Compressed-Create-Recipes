package com.pression.compressedcreaterecipes.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SpoutBlockEntity.class)
public abstract class FillingOutputMixin {
    @Shadow(remap = false) protected abstract FluidStack getCurrentFluidInTank();

    @Unique SpoutBlockEntity self = (SpoutBlockEntity)(Object) this;
    @Unique List<ItemStack> extraOutput = new ArrayList<>();

    @Inject(method = "whenItemHeld", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour;handleProcessingOnItem(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour$TransportedResult;)V"),
            remap = false)
    private void addExtraOutputs(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, CallbackInfoReturnable<BeltProcessingBehaviour.ProcessingResult> cir, @Local List<TransportedItemStack> outList, @Local(ordinal = 1) TransportedItemStack held){
        outList.clear();
        if(extraOutput.isEmpty()) return;
        TransportedItemStack holder = held.copy();
        extraOutput.forEach(item -> {
            holder.stack = item.copy();
            outList.add(holder.getSimilar());
        });
    }



    @Inject(method = "whenItemHeld", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour;handleProcessingOnItem(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour$TransportedResult;)V"), remap = false)
    private void calcExtraOutputs(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, CallbackInfoReturnable<BeltProcessingBehaviour.ProcessingResult> cir){
        extraOutput.clear();
        RecipeWrapper WRAPPER = new RecipeWrapper(new ItemStackHandler(1));
        WRAPPER.setItem(0, transported.stack);
        FillingRecipe fillingRecipe =
                SequencedAssemblyRecipe.getRecipe(self.getLevel(), WRAPPER, AllRecipeTypes.FILLING.getType(), FillingRecipe.class)
                        .filter(fr -> fr.getRequiredFluid()
                                .test(getCurrentFluidInTank()))
                        .orElseGet(() -> {
                            for (Recipe<RecipeWrapper> recipe : self.getLevel().getRecipeManager()
                                    .getRecipesFor(AllRecipeTypes.FILLING.getType(), WRAPPER, self.getLevel())) {
                                FillingRecipe fr = (FillingRecipe) recipe;
                                FluidIngredient requiredFluid = fr.getRequiredFluid();
                                if (requiredFluid.test(getCurrentFluidInTank()))
                                    return fr;
                            }
                            return null;
                        });
        if(fillingRecipe != null){
            extraOutput.addAll(fillingRecipe.rollResults());
        }
    }
}
