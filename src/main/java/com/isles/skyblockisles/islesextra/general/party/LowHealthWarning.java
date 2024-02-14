package com.isles.skyblockisles.islesextra.general.party;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.minecraft.entity.player.PlayerEntity;

public class LowHealthWarning {
    public static void init() {
        if(IslesParty.getMembers().isEmpty() || ClientUtils.getClient().getNetworkHandler() == null) return;
        for (PlayerEntity playerEntity : IslesParty.getEntities()) {
            if (playerEntity == null) ClientUtils.sendTitle("§4§l A Teammate is dead!",0,2,0);
            else if (playerEntity.getHealth() < 5) ClientUtils.sendTitle("§4§l" + playerEntity.getName().getString() + " is low!",0,2,0);
        }
    }

}
