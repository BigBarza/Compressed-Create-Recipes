package com.pression.compressedcreaterecipes.mixin.ponder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.pression.compressedcreaterecipes.helpers.IPonderScene;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ExperienceOrbRenderer;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// XP orbs in ponders use the player's camera angle instead of the ponder scene camera's.
// This fixes that. It specifically checks if the orb is in a ponder scene.
@Mixin(ExperienceOrbRenderer.class)
public class XPOrbRendererMixin {
    @Redirect(method = "render(Lnet/minecraft/world/entity/ExperienceOrb;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;cameraOrientation()Lcom/mojang/math/Quaternion;"))
    private Quaternion changeCamera(EntityRenderDispatcher instance, ExperienceOrb orb, float p_114600_, float p_114601_, PoseStack p_114602_, MultiBufferSource p_114603_, int p_114604_){
        if(orb.getLevel() instanceof PonderWorld ponder){
            PonderScene.SceneCamera camera = ((IPonderScene) ponder.scene).getCamera();
            // Right. I am not going to pretend to fully understand this as it would mean delving into
            // more complex modelling and rendering stuff. It's a fancier angle with a side of...imaginary numbers?
            // Some called this method a bit of a dirty hack. I don't care. It does what i need it to do.
            Quaternion q = Vector3f.YP.rotationDegrees(-camera.getYRot());
            q.mul(Vector3f.XP.rotationDegrees(camera.getXRot()));
            //But yeah. If we're in a PonderWorld, use this camera angle instead.
            return q;
        }
        //Otherwise, use the normal one.
        return instance.cameraOrientation();
    }
}
