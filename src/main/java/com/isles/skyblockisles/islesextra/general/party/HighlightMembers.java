package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.PartyUtils;
import net.minecraft.entity.player.PlayerEntity;

public class HighlightMembers {

    public static void init() {
        if(PartyUtils.getMembers().isEmpty() || ClientUtils.getClient().getNetworkHandler() == null) return;
        for (PlayerEntity playerEntity : PartyUtils.partyMemberEntities) {
            //TODO: ADD GLOWING EFFECT
        }

    }

}
