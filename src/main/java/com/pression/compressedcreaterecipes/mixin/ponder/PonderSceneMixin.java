package com.pression.compressedcreaterecipes.mixin.ponder;

import com.pression.compressedcreaterecipes.helpers.IPonderScene;
import com.simibubi.create.foundation.ponder.PonderScene;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

// This mixin serves to extract the camera from a ponder scene.
// The camera is used to recalculate the direction of xp orbs in a ponder.
@Mixin(PonderScene.class)
public class PonderSceneMixin implements IPonderScene {
    @Shadow(remap = false)
    private PonderScene.SceneCamera camera;

    @Unique
    public PonderScene.SceneCamera getCamera() {
        return camera;
    }
}
