package com.isles.skyblockisles.islesextra.bossrush.turtle;

import com.isles.skyblockisles.islesextra.constants.MessageSender;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;

public class CoconutBombWarning {

  //TODO: FIND A BETTER EVENT LISTENER THAT SUITS THIS FUNCTION
  private static final Map<Block, String> BLOCK_WARNING_MAP = new HashMap<>();

  static {
    BLOCK_WARNING_MAP.put(Blocks.YELLOW_CONCRETE, "§l§eWARN");
    BLOCK_WARNING_MAP.put(Blocks.ORANGE_CONCRETE, "§l§6WARN");
    BLOCK_WARNING_MAP.put(Blocks.RED_CONCRETE, "§l§4WARN");
  }

  public static void init() {
    var client = MinecraftClient.getInstance();
    var world = client.world;
    var player = client.player;
    if (world == null || player == null) {
      return;
    }

    Block blockUnderPlayer = world.getBlockState(player.getBlockPos().down()).getBlock();

    String warningMessage = BLOCK_WARNING_MAP.get(blockUnderPlayer);
    if (warningMessage != null) {
      MessageSender.sendTitle(warningMessage, 0, 2, 0);
    }
  }

}
