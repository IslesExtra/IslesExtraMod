package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.PartyUtils;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class HighlightMembers {

    private static final List<PlayerEntity> glowingPlayers = new ArrayList<>(4);

    public static List<PlayerEntity> getGlowingPlayers() {
        return glowingPlayers;
    }

    public static void init() {
        if(PartyUtils.getMembers().isEmpty() || ClientUtils.getClient().getNetworkHandler() == null) return;
        //TODO: ADD GLOWING EFFECT
        glowingPlayers.addAll(PartyUtils.partyMemberEntities);
    }

}
