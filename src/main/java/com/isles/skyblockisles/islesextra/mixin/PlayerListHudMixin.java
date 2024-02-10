package com.isles.skyblockisles.islesextra.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "setHeader", at = @At("HEAD"))
    private void setHeader(Text header, CallbackInfo ci) {
        //System.out.println(header.getString());
    }

}
