package com.kyllian.islesextra.islesextra.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;

public class StatsGui extends CustomGui{

    public StatsGui(PlayerInventory inventory, Text title) {
        super(inventory, title);
    }

    @Override
    public void setItems() {
        Inventory inventory = handler.getInventory();
        this.setBackground(GENERIC_BACKGROUND_ITEM);
    }

}
