package com.isles.skyblockisles.islesextra.bossrush.frog;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class StomachExplosionWarning {

    //TODO: FIND AN EVENT LISTENER THAT SUITS THIS FUNCTION
    private static final Map<Block, String> BLOCK_WARNING_MAP = new HashMap<>();
    static {
        BLOCK_WARNING_MAP.put(Blocks.YELLOW_CONCRETE, "§l§eWARN");
        BLOCK_WARNING_MAP.put(Blocks.ORANGE_CONCRETE, "§l§6WARN");
        BLOCK_WARNING_MAP.put(Blocks.RED_CONCRETE, "§l§4WARN");
    }
    public static void init() {

        Block blockUnderPlayer = ClientUtils.getWorld().getBlockState(ClientUtils.getPlayer().getBlockPos().down()).getBlock();

        String warningMessage = BLOCK_WARNING_MAP.get(blockUnderPlayer);
        if (warningMessage != null) {
            ClientUtils.sendTitle(warningMessage, 0, 2, 0);
        }
    }
}
