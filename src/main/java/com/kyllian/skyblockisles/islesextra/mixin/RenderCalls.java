package com.kyllian.skyblockisles.islesextra.mixin;

import com.kyllian.skyblockisles.islesextra.rendering.RenderingUtils;
import com.kyllian.skyblockisles.islesextra.rendering.TrackerRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
abstract class RenderCalls {

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/render/Camera;)V"
            )
    )
    public void onRenderLast(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        TrackerRenderer.getInstance().render();
        RenderingUtils.renderAll();
    }

}
