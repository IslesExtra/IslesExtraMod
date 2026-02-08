package net.skyblockisles.islesextra.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.skyblockisles.islesextra.IslesClientState;
import net.skyblockisles.islesextra.bosstimer.AlphaBoarTimer;
import net.skyblockisles.islesextra.config.IslesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onGameMessage", at = @At("TAIL"))
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (IslesClientState.isOnIsles() && IslesConfig.HANDLER.instance().enableWorldBossTimer) {
            String chat = packet.comp_763().getString();
            if (chat.contains("\uE119☠ Alpha Boar ☠")) {
                System.out.println("Isles Refresh - ALPHA BOAR WILL SPAWN IN 2 minutes!");
                AlphaBoarTimer.willSummonBoar = true;
                AlphaBoarTimer.alphaBoarTimer = 120;
            }
        }
    }
}
