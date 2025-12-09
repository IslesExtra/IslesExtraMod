package com.isles.skyblockisles.islesextra.constants;

import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public record ChatPreviewPayload(Text text) implements CustomPayload {
    public static final Id<ChatPreviewPayload> ID = new Id<>(Identifier.of(
        IslesExtraClient.MOD_ID, "preview_packet"));

    public static final PacketCodec<RegistryByteBuf, ChatPreviewPayload> CODEC = PacketCodec.tuple(
            TextCodecs.REGISTRY_PACKET_CODEC, ChatPreviewPayload::text,
            ChatPreviewPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}