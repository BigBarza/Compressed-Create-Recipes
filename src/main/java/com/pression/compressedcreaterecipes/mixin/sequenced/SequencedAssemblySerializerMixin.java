package com.pression.compressedcreaterecipes.mixin.sequenced;

import com.google.gson.JsonObject;
import com.pression.compressedcreaterecipes.helpers.ISequencedProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This Mixin adds the capability to read the "processing" flag to sequenced assembly recipes
@Mixin(SequencedAssemblyRecipeSerializer.class)
public class SequencedAssemblySerializerMixin {
    @Inject(method = "writeToJson", at = @At("HEAD"), remap = false)
    private void onWriteToJson(JsonObject json, SequencedAssemblyRecipe recipe, CallbackInfo ci){
        boolean processing = ((ISequencedProcessingRecipe) recipe).isProcessing();
        json.addProperty("isProcessing", processing);
    }

    @Inject(method = "readFromJson", at = @At("RETURN"), remap = false, cancellable = true)
    private void onReadFromJson(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<SequencedAssemblyRecipe> cir){
        SequencedAssemblyRecipe recipe = cir.getReturnValue();
        //IF the flag is there AND it is true
        boolean processing = json.has("isProcessing") && GsonHelper.getAsBoolean(json, "isProcessing");
        ((ISequencedProcessingRecipe) recipe).setProcessing(processing);
        cir.setReturnValue(recipe);
    }

    @Inject(method = "writeToBuffer", at = @At("TAIL"), remap = false)
    private void onWriteToBuffer(FriendlyByteBuf buffer, SequencedAssemblyRecipe recipe, CallbackInfo ci){
        boolean processing = ((ISequencedProcessingRecipe)recipe).isProcessing();
        buffer.writeBoolean(processing);
    }

    @Inject(method = "readFromBuffer", at = @At("RETURN"), remap = false, cancellable = true)
    private void onReadFromBuffer(ResourceLocation recipeId, FriendlyByteBuf buffer, CallbackInfoReturnable<SequencedAssemblyRecipe> cir){
        SequencedAssemblyRecipe recipe = cir.getReturnValue();
        boolean processing = buffer.readBoolean();
        ((ISequencedProcessingRecipe) recipe).setProcessing(processing);
        cir.setReturnValue(recipe);
    }
}
