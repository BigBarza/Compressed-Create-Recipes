package com.pression.compressedcreaterecipes.mixin.sequenced;

import com.pression.compressedcreaterecipes.helpers.VersionHelper;
import com.simibubi.create.compat.jei.CreateJEI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.concurrent.atomic.AtomicInteger;

//Yes, this is JUST to get the extra vertical clearance.
@Mixin(CreateJEI.class)
public abstract class CreateJEIMixin {
    @Unique
    private static final AtomicInteger callCount = new AtomicInteger(0);
    @ModifyArg(
            method = "loadCategories",
            at = @At(value = "INVOKE",
                    target = "Lcom/simibubi/create/compat/jei/CreateJEI$CategoryBuilder;emptyBackground(II)Lcom/simibubi/create/compat/jei/CreateJEI$CategoryBuilder;"),
            remap = false,
            index = 1
    )
    private int modifyEmptyBackground(int height){
        int categoryPos = (VersionHelper.isV6) ? 22 : 23;
        // I hate this. I hate this so much.
        // We can't access the category builder at ALL. Best i can do is this.
        // Sequenced assembly is the 23rd one in the list.
        // Edit: I hate this even more. It's the 22nd element on 6.0
        if(callCount.incrementAndGet() == categoryPos){
            return height + 20; //Not too much, just the clearance to add a row of items underneath and a sliver of free space.
        }

        return height;
    }
}
