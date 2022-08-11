package com.kyllian.islesextra.islesextra.mixin;

import com.kyllian.islesextra.islesextra.rendering.RenderingUtils;
import com.kyllian.islesextra.islesextra.rendering.TrackerRenderer;
import com.kyllian.islesextra.islesextra.utility.Dialogue;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.*;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class RenderCalls {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V", at = @At("RETURN"))
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        TrackerRenderer.getInstance().render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        RenderingUtils.renderAll(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }

}
