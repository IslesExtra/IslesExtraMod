package com.isles.skyblockisles.islesextra.mixin;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Shadow @Final 
    private Map<UUID, ClientBossBar> bossBars;

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    public void onHandlePacket(BossBarS2CPacket packet, CallbackInfo ci) {
        UUID packetUuid = ((BossBarS2CPacketAccessor) packet).getUuid();

        boolean barExists = this.bossBars.containsKey(packetUuid);
        
        if (!barExists) {
            if (!isAddPacket(packet)) {
                ci.cancel();
            }
        }
    }

    @Unique
    private boolean isAddPacket(BossBarS2CPacket packet) {
        return packet.toString().contains("AddAction");
    }
}