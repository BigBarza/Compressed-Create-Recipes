package com.pression.compressedcreaterecipes.mixin.misc;

import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.pression.compressedcreaterecipes.CompressedCreateRecipes.NO_3D;

@Mixin(ValueBox.ItemValueBox.class)
public class ValueBoxMixin {
    @Shadow(remap = false) private ItemStack stack;

    @Redirect(method = "renderContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;isGui3d()Z"))
    private boolean correct3DTagging(BakedModel instance){
        if(stack != null && stack.is(NO_3D)) return false;
        else return instance.isGui3d();
    }
}
