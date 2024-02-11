package com.isles.skyblockisles.islesextra.bossrush.general;

import com.isles.skyblockisles.islesextra.utils.ClientUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class LowAmmoWarning {

    //TODO: ADD CONFIGURABILITY && ADD CHECK FOR ACTUAL ITEM INSTEAD OF JUST TYPE
    public static void init() {
        int copperArrows = countItem(Items.SPECTRAL_ARROW);
        int honingStones = countItem(Items.NETHERITE_SCRAP);
        int magicRunes = countItem(Items.PHANTOM_MEMBRANE);
        int threshold = 128;

        ItemStack mainStack = ClientUtils.getPlayer().getInventory().getMainHandStack();

        if (ClientUtils.inBoss()) {
            if (copperArrows < threshold)
                ClientUtils.sendTitle("§4Low Arrows");
            if (honingStones < threshold)
                ClientUtils.sendTitle("§4Low Stones");
            if (magicRunes < threshold)
                ClientUtils.sendTitle("§4Low Runes");
        }
    }
    private static int countItem(Item item) {
        int count = 0;
        // Iterate through every Slot and add it to count if it finds the Item
        for (int i = 0; i < ClientUtils.getPlayer().getInventory().size(); i++) {
            ItemStack currentStack = ClientUtils.getPlayer().getInventory().getStack(i);
            if (currentStack != null && currentStack.isOf(item))
                count += currentStack.getCount();
        }
        return count;
    }

}
