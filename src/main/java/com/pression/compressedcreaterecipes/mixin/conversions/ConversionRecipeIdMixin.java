package com.pression.compressedcreaterecipes.mixin.conversions;

import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.ConversionRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//This mixin changes the recipe id of the conversion recipes destined for JEI from conversion_<number> to conversion_<input item id>_to_<output item_id>
@Mixin(ConversionRecipe.class)
public class ConversionRecipeIdMixin {
    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/Create;asResource(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"), remap = false)
    private static ResourceLocation redoID(String path, ItemStack from, ItemStack to){
        ResourceLocation inputRL = ForgeRegistries.ITEMS.getKey(from.getItem());
        ResourceLocation outputRL = ForgeRegistries.ITEMS.getKey(to.getItem());
        return Create.asResource("conversion_"+
        inputRL.getNamespace()+"_"+
        inputRL.getPath()+"_to_"+
        outputRL.getNamespace()+"_"+
        outputRL.getPath()
        );
    }
}
