package com.pression.compressedcreaterecipes.mixin.ponder;

import com.pression.compressedcreaterecipes.ClientConfig;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.pression.compressedcreaterecipes.CompressedCreateRecipes.SPOOFED_GAME_TIME;

// Viewing ponders in singleplayer has a frozen texture atlas, since the game is paused.
// This can interfere with the ponder itself, as items won't animate so this mixin makes the texture atlas tick anyway.
@Mixin(PonderUI.class)
public class PonderScreenMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tickAtlas(CallbackInfo ci){
        SPOOFED_GAME_TIME++;
        Minecraft inst = Minecraft.getInstance();
        // The isPaused is necessary because the texture atlas does NOT get
        // frozen in multiplayer as the game doesn't actually pause there.
        if(inst.isPaused() && ClientConfig.TICK_ATLAS_IN_PONDERS.get()){
            inst.getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).tick();
        }
    }
}
