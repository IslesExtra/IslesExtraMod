package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.PartyUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HighlightMembers {

    public static final List<PlayerEntity> glowingPlayers = new ArrayList<>(4);
    public static List<PlayerEntity> getGlowingPlayers() {
        return glowingPlayers;
    }

    public static void init() {
        if(!ClientUtils.inBoss() || PartyUtils.getMembers().isEmpty() || ClientUtils.getClient().getNetworkHandler() == null) return;

        glowingPlayers.addAll(PartyUtils.getEntities());
        glowingPlayers.removeIf(Objects::isNull);

        for(PlayerEntity partyMember : HighlightMembers.glowingPlayers) {
            MinecraftClient.getInstance().hasOutline(partyMember);}
    }

}
