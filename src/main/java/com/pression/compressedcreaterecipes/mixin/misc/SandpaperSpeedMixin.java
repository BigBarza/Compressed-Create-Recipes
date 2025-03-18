package com.pression.compressedcreaterecipes.mixin.misc;

import com.pression.compressedcreaterecipes.CommonConfig;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin overwrites the use duration of create's sandpaper with a value from config.
@Mixin(SandPaperItem.class)
public class SandpaperSpeedMixin {
    @Inject(method = "getUseDuration", at = @At("HEAD"),
            cancellable = true)
    private void setUseDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(CommonConfig.SANDING_SPEED.get());
    }
}