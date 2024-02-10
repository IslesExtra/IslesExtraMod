package com.isles.skyblockisles.islesextra.bossrush.frog;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class StomachExplodeWarning {

    //TODO: FIND AN EVENT LISTENER THAT SUITS THIS FUNCTION

    private static final Map<Block, String> BLOCK_WARNING_MAP = new HashMap<>();
    static {
        BLOCK_WARNING_MAP.put(Blocks.YELLOW_CONCRETE, "§eWARN");
        BLOCK_WARNING_MAP.put(Blocks.ORANGE_CONCRETE, "§6WARN");
        BLOCK_WARNING_MAP.put(Blocks.RED_CONCRETE, "§4WARN");
    }
    public static void stomachExplodeWarn() {

        Block blockUnderPlayer = ClientUtils.getWorld().getBlockState(ClientUtils.getPlayer().getBlockPos().down()).getBlock();

        String warningMessage = BLOCK_WARNING_MAP.get(blockUnderPlayer);
        if (warningMessage != null) {
            ClientUtils.sendTitle(warningMessage);
        }
    }
}
