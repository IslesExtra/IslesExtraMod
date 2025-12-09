package com.isles.skyblockisles.islesextra.mixin.chat;

import com.isles.skyblockisles.islesextra.client.IslesClientState;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "clear", at = @At("HEAD"), cancellable = true)
    /*
     prevent chat history being cleared when swapping between isles servers
     does it also prevent history being cleared when going from isles to different servers? idk probably, do I care? idk probably not rn
    */
    void preventHistoryClear(boolean clearHistory, CallbackInfo ci) {
        if (IslesClientState.isOnIsles()) ci.cancel();
    }

}
