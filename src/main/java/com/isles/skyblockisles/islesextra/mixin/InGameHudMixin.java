package com.isles.skyblockisles.islesextra.mixin;

import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    void renderHud(float tickDelta, DrawContext context, CallbackInfo ci) {
        if (!IslesExtraClient.isOnIsles()) return;
        IslesHudHandler.renderHud(context, this.getTextRenderer());
    }

}
