package com.kyllian.skyblockisles.islesextra.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientData {

    public static final List<PickedUpItem> pickedUpItems = new ArrayList<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final int DELETE_MILLIS = 5000;

    // This would be used for negative "count" values
    public static void addPickedUpItem(ItemStack singleItem, int count) {
        for (PickedUpItem pItem : pickedUpItems) {
            if (pItem.name.equals(singleItem.getName())) {
                pItem.count = pItem.count + count;
                pItem.deleteTime = System.currentTimeMillis() + DELETE_MILLIS;

                if (pItem.count == 0) pickedUpItems.remove(pItem);
                return;
            }
        }
        PickedUpItem newItem = new PickedUpItem(singleItem);
        newItem.count = count;
        pickedUpItems.add(newItem);
    }

    public static void updatePickedUpItems() {
        getInventoryDifference();
        pickedUpItems.removeIf(pItem -> pItem.deleteTime <= System.currentTimeMillis());
    }

    private static List<ItemStack> oldInventory;
    public static void getInventoryDifference() {
        if (client.player == null) return;
        List<ItemStack> inventory = new ArrayList<>();
        if (client.player.playerScreenHandler.getCursorStack() != null && !client.player.playerScreenHandler.getCursorStack().getItem().equals(Items.AIR)) inventory.add(client.player.playerScreenHandler.getCursorStack().copy());
        for (ItemStack item : client.player.getInventory().main) {
            if (item.getItem().equals(Items.AIR)) continue;
            boolean hasItem = false;
            for (ItemStack itemStack : inventory) {
                if (itemStack.getName().equals(item.getName())) {
                    itemStack.setCount(item.getCount() + itemStack.getCount());
                    hasItem = true;
                    break;
                }
            }
            if (!hasItem) inventory.add(item.copy());
        }
        if (oldInventory!=null) {
            for (ItemStack item : inventory) {
                boolean hadItem = false;
                for (ItemStack oldItem : oldInventory) {
                    if (oldItem.getName().equals(item.getName())) {
                        hadItem = true;
                        if (oldItem.getCount() != item.getCount()) {
                            int difference = item.getCount() - oldItem.getCount();
                            if (difference <= 0) break;
                            ItemStack diffItem = item.copy();
                            diffItem.setCount(difference);
                            ClientData.addPickedUpItem(diffItem, difference);
                            break;
                        }
                    }
                }
                if (!hadItem) ClientData.addPickedUpItem(item.copy(), item.getCount());
            }
            for (ItemStack oldItem : oldInventory) {
                boolean hasItem = false;
                for (ItemStack item : inventory) {
                    if (item.getName().equals(oldItem.getName())) {
                        hasItem = true;
                        if (oldItem.getCount() != item.getCount()) {
                            int difference = oldItem.getCount() - item.getCount();
                            if (difference <= 0) break;
                            ItemStack single = oldItem.copy();
                            single.setCount(1);
                            ClientData.addPickedUpItem(single, -difference);
                            break;
                        }
                    }
                }
                if (!hasItem) ClientData.addPickedUpItem(oldItem.copy(), -oldItem.getCount());
            }
        }
        oldInventory = inventory;
    }

    public static class PickedUpItem {
        public final Item item;
        public final Text name;
        long deleteTime;
        public int count;
        public final int customModelData;

        public PickedUpItem(ItemStack stack) {
            this(stack.getItem(), stack.getName(), System.currentTimeMillis() + DELETE_MILLIS, stack.getCount(), stack.getOrCreateNbt().contains("CustomModelData") ? stack.getOrCreateNbt().getInt("CustomModelData") : -1);
        }

        public PickedUpItem(Item item, Text name, long deleteTime, int count, int customModelData) {
            this.item = item;
            this.name = name;
            this.deleteTime = deleteTime;
            this.count = count;
            this.customModelData = customModelData;
        }

    }

}
