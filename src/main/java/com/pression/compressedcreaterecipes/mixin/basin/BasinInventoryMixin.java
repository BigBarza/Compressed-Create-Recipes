package com.pression.compressedcreaterecipes.mixin.basin;

import com.simibubi.create.content.processing.basin.BasinInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BasinInventory.class)
public class BasinInventoryMixin {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/item/SmartInventory;<init>(ILcom/simibubi/create/foundation/blockEntity/SyncedBlockEntity;IZ)V"), index = 2, remap = false)
    private static int setNewMaxStackSize(int slots){
        return 64;
    }
}
