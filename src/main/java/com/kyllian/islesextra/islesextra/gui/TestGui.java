package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.client.ClientUtils;
import com.kyllian.islesextra.islesextra.client.CustomText;
import com.kyllian.islesextra.islesextra.client.ItemEditor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;

public class TestGui extends CustomGui {

    public TestGui(PlayerInventory inventory) {
        super(inventory, new LiteralText("Test Gui"));
    }

    @Override
    public void setItems() {
        setBackground(GENERIC_BACKGROUND_ITEM);
        ItemStack diamond = new ItemEditor(Items.DIAMOND).setGlowing().addLore("Lore 1", "Test").setName(new CustomText("&#03ecfcDiamond").getValue()).getResult();
        setItem(31, diamond);
    }

    @Override
    public void onClickSlot(Slot slot) {
        ClientUtils.sendMessage(slot.getStack().getName());
        if (slot.id == 31) {
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.closeScreen();
        }
    }
}
