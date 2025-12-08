package com.isles.skyblockisles.islesextra.client.discord;

import com.isles.skyblockisles.islesextra.IslesExtra;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DiscordRPPayload(String text) implements CustomPayload {
  public static final Id<DiscordRPPayload> ID = new Id<>(Identifier.of(IslesExtra.MOD_ID, "discord_packet"));

  public static final PacketCodec<RegistryByteBuf, DiscordRPPayload> CODEC = PacketCodec.tuple(
      PacketCodecs.STRING, DiscordRPPayload::text,
      DiscordRPPayload::new
  );

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
