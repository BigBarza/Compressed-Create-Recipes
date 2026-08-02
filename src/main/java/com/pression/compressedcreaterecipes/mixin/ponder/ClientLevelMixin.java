package com.pression.compressedcreaterecipes.mixin.ponder;

import com.pression.compressedcreaterecipes.ClientConfig;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.pression.compressedcreaterecipes.CompressedCreateRecipes.SPOOFED_GAME_TIME;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelMixin {


    @Inject(method = "getGameTime", at = @At("RETURN"), cancellable = true)
    private void substituteGameTime(CallbackInfoReturnable<Long> cir){
        if(!Minecraft.getInstance().isPaused() || !ClientConfig.TICK_ATLAS_IN_PONDERS.get()) return; //Should it not be paused, we risk mucking up the real game time.
        if(Minecraft.getInstance().screen instanceof PonderUI ponder){
            cir.setReturnValue(SPOOFED_GAME_TIME);
        }
    }

}
