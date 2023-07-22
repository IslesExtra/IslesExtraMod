package com.kyllian.skyblockisles.islesextra.mixin;

import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
abstract class RenderCalls {

    // TODO: fix

    /*@Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/render/Camera;)V"
            )
    )
    public void onRenderLast(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        TrackerRenderer.getInstance().render();
        RenderingUtils.renderAll();
    }*/

}
