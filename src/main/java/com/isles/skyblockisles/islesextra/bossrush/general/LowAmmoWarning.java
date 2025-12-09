package com.isles.skyblockisles.islesextra.bossrush.general;

import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.constants.IslesBoss;
import com.isles.skyblockisles.islesextra.constants.MessageSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LowAmmoWarning {

    public static void init() {
        int copperArrows = countItem(Items.SPECTRAL_ARROW);
        int honingStones = countItem(Items.NETHERITE_SCRAP);
        int magicRunes = countItem(Items.PHANTOM_MEMBRANE);
        int threshold = 128;

        if (IslesHudHandler.inBoss) {
            if (copperArrows < threshold)
                MessageSender.sendTitle("§4Low Arrows");
            if (honingStones < threshold)
                MessageSender.sendTitle("§4Low Stones");
            if (magicRunes < threshold)
                MessageSender.sendTitle("§4Low Runes");
        }
    }

    private static int countItem(Item item) {
        int count = 0;
        // Iterate through every Slot and add it to count if it finds the Item

        var player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack currentStack = player.getInventory().getStack(i);
            if (currentStack != null && currentStack.isOf(item))
                count += currentStack.getCount();
        }
        return count;
    }

}
