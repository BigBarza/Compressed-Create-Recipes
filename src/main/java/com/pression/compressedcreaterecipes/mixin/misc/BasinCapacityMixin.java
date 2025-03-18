package com.pression.compressedcreaterecipes.mixin.misc;

import com.pression.compressedcreaterecipes.CommonConfig;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//This mixin sets the capacity of the basin's tanks. Mind that the basin has two input tanks and two output tanks.
@Mixin(BasinBlockEntity.class)
public class BasinCapacityMixin {
    @ModifyArg(
            method = "addBehaviours",
            remap = false,
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/blockEntity/behaviour/fluid/SmartFluidTankBehaviour;<init>(Lcom/simibubi/create/foundation/blockEntity/behaviour/BehaviourType;Lcom/simibubi/create/foundation/blockEntity/SmartBlockEntity;IIZ)V"),
            index = 3
    )
    private int setNewCapacity(int original){
        return CommonConfig.BASIN_CAPACITY.get();
    }
}
