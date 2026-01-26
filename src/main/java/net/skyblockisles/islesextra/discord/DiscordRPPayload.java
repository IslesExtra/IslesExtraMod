package net.skyblockisles.islesextra.discord;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.skyblockisles.islesextra.IslesExtra;

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
