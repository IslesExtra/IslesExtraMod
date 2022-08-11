package com.kyllian.islesextra.islesextra.gui;

import com.kyllian.islesextra.islesextra.client.ItemEditor;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class CustomGui extends GenericContainerScreen {

    public static ItemStack GENERIC_BACKGROUND_ITEM = new ItemEditor(Items.BLACK_STAINED_GLASS_PANE).setName("").getResult();

    public CustomGui(PlayerInventory inventory, Text title) {
        super(GenericContainerScreenHandler.createGeneric9x6(0, inventory), inventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        cancelEvents();
        setItems();
    }

    public void setItem(int slot, ItemStack item) { handler.getInventory().setStack(slot, item); }

    public void setItems() { }

    public void onClickSlot(Slot slot) { }

    public void setBackground(ItemStack backgroundItem) {
        for (int i = 0; i<54; i++) { getScreenHandler().getInventory().setStack(i, backgroundItem); }
    }

    public void cancelEvents() {

        ScreenMouseEvents.allowMouseClick(this).register(((screen, mouseX, mouseY, button) -> {
            if (focusedSlot!=null) onClickSlot(focusedSlot);
            return false;
        }));

        ScreenKeyboardEvents.allowKeyPress(this).register(((screen, key, scancode, modifiers) -> key == 256));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
