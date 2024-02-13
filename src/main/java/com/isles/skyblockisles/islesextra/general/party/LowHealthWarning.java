package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.PartyUtils;
import net.minecraft.entity.player.PlayerEntity;

public class LowHealthWarning {

    //TODO: FIND A MORE SUITABLE EVENT
    public static void init() {
        if(PartyUtils.getMembers().isEmpty() || ClientUtils.getClient().getNetworkHandler() == null) return;
        for (PlayerEntity playerEntity : PartyUtils.partyMemberEntities) {
            if (playerEntity.getHealth() < 20) ClientUtils.sendTitle("§4§l" + playerEntity.getName() + " is low!");
        }
    }

}
