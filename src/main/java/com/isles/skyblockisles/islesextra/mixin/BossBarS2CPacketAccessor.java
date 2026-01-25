package com.isles.skyblockisles.islesextra.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;

@Mixin(BossBarS2CPacket.class)
public interface BossBarS2CPacketAccessor {
    @Accessor("uuid")
    UUID getUuid();
}