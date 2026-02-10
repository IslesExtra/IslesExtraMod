package net.skyblockisles.islesextra.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.UUID;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    @Shadow @Final private Map<UUID, ClientBossBar> bossBars;

    /**
     * Injects at the start of handlePacket to check if the update is valid.
     * If the packet is an update but the client doesn't have the bar, we cancel.
     */
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private void protectAgainstNullBar(BossBarS2CPacket packet, CallbackInfo ci) {
        packet.accept(new BossBarS2CPacket.Consumer() {
            @Override
            public void add(UUID uuid, net.minecraft.text.Text name, float percent, net.minecraft.entity.boss.BossBar.Color color, net.minecraft.entity.boss.BossBar.Style style, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
            }

            @Override
            public void remove(UUID uuid) {
                if (!bossBars.containsKey(uuid)) ci.cancel();
            }

            @Override
            public void updateProgress(UUID uuid, float percent) {
                if (!bossBars.containsKey(uuid)) ci.cancel();
            }

            @Override
            public void updateName(UUID uuid, net.minecraft.text.Text name) {
                if (!bossBars.containsKey(uuid)) ci.cancel();
            }

            @Override
            public void updateStyle(UUID uuid, net.minecraft.entity.boss.BossBar.Color color, net.minecraft.entity.boss.BossBar.Style style) {
                if (!bossBars.containsKey(uuid)) ci.cancel();
            }

            @Override
            public void updateProperties(UUID uuid, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
                if (!bossBars.containsKey(uuid)) ci.cancel();
            }
        });
    }
}