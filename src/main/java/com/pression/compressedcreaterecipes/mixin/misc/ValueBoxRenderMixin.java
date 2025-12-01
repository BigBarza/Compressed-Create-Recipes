package com.pression.compressedcreaterecipes.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.pression.compressedcreaterecipes.CompressedCreateRecipes.NO_3D;

@Mixin(ValueBoxRenderer.class)
public class ValueBoxRenderMixin {

    @Redirect(method = "renderItemIntoValueBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;isGui3d()Z"))
    private static boolean correct3DTagging(BakedModel instance, @Local(argsOnly = true)ItemStack filter){
        if(filter.is(NO_3D)) return false;
        else return instance.isGui3d();
    }
}
