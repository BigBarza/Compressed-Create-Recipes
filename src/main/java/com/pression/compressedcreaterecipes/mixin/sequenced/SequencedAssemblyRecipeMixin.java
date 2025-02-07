package com.pression.compressedcreaterecipes.mixin.sequenced;

import com.pression.compressedcreaterecipes.helpers.ISequencedProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SequencedAssemblyRecipe.class)
public class SequencedAssemblyRecipeMixin implements ISequencedProcessingRecipe {

    //This flag will determine whether to move the first output to the bottom as well.
    @Unique private boolean processingFlag = false;
    @Unique public void setProcessing(boolean flag){this.processingFlag = flag;}
    @Unique public boolean isProcessing(){return processingFlag;}

    //This is a helper method in SequencedAssemblyRecipe that returns the chance of the first output.
    //It is ONLY used in jei to decide whether to move things around and draw the random salvage thing.
    //It does not affect the actual product of sequenced assembly.
    @Inject(method = "getOutputChance", at = @At("HEAD")
    , remap = false, cancellable = true)
    private void spoofOutputChance(CallbackInfoReturnable<Float> cir){
        cir.setReturnValue(1f);
    }
}
