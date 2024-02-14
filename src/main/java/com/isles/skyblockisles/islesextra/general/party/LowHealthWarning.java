package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import com.isles.skyblockisles.islesextra.utils.PartyUtils;
import net.minecraft.entity.player.PlayerEntity;

public class LowHealthWarning {

    //TODO: FIND A MORE SUITABLE EVENT
    public static void init() {
        if(PartyUtils.getMembers().isEmpty() || ClientUtils.getClient().getNetworkHandler() == null) return;
        for (PlayerEntity playerEntity : PartyUtils.getEntities()) {
            if (playerEntity.getHealth() <= 0) ClientUtils.sendTitle("§4§l" + playerEntity.getName().getString() + " is dead!",0,20,0);
            if (playerEntity.getHealth() < 20) ClientUtils.sendTitle("§4§l" + playerEntity.getName().getString() + " is low!",0,2,0);
        }
    }

}
