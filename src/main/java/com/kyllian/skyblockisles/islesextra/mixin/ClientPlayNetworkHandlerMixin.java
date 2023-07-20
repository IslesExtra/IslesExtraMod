package com.kyllian.skyblockisles.islesextra.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendCommand", at = @At("HEAD"))
    void onCommand(String command, CallbackInfoReturnable<Boolean> cir) {
        System.out.println(command);
    }

}
