package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.client.ClientUtils;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class TextInputGui extends AnvilScreen {

    public TextInputGui(PlayerInventory inventory) {
        super(new AnvilScreenHandler(0, inventory), inventory, Text.literal("Text Input"));
    }

    @Override
    public void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        cancelEvents();
        ItemStack placeholder = new ItemStack(Items.PAPER);
        placeholder.setCustomName(Text.literal(""));
        getScreenHandler().setStackInSlot(0, 0, placeholder);
        getScreenHandler().updateResult();
    }

    public void cancelEvents() {

        ScreenMouseEvents.allowMouseClick(this).register(((screen, mouseX, mouseY, button) -> {
            if (focusedSlot!=null) onClickSlot(focusedSlot);
            return false;
        }));

        ScreenKeyboardEvents.allowKeyPress(this).register(((screen, key, scancode, modifiers) -> key == 256 || this.focusedSlot == null));
    }

    public void onClickSlot(Slot slot) {
        assert MinecraftClient.getInstance().player != null;
        if (slot.id == 2) {
            MinecraftClient.getInstance().player.closeScreen();
            ClientUtils.sendMessage(slot.getStack().getName());
        }
    }

}
