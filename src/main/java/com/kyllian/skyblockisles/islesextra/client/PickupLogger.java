package com.kyllian.skyblockisles.islesextra.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kyllian.skyblockisles.islesextra.annotation.OnIslesLeave;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class PickupLogger {

    public static final List<PickedUpItem> pickedUpItems = new ArrayList<>();
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final int DELETE_MILLIS = 5000;

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

    @OnIslesLeave
    static void clearLogger() {
        oldItemMap.clear();
    }

    public static void updatePickedUpItems() {
        if (!IslesExtraClient.isOnIsles()) return;
        getInvDifference();
        pickedUpItems.removeIf(pItem -> pItem.deleteTime <= System.currentTimeMillis());
    }

    static HashMap<String, Integer> oldItemMap = new HashMap<>();
    public static void getInvDifference() {
        if (client.player == null) return;
        HashMap<String, Integer> itemMap = new HashMap<>();
        for (ItemStack item : client.player.getInventory().main) {
            String key = getItemKey(item);
            if (key.contains("air")) continue;
            if (!itemMap.containsKey(key)) itemMap.put(key, item.getCount());
            else itemMap.put(key, itemMap.get(key) + item.getCount());
        }

        if (oldItemMap != null && client.currentScreen == null) {
            HashMap<String, Integer> differences = new HashMap<>();
            List<String> keys = new ArrayList<>();
            keys.addAll(itemMap.keySet());
            keys.addAll(oldItemMap.keySet());
            for (String key : keys) {
                int oldValue = oldItemMap.getOrDefault(key, 0);
                int newValue = itemMap.getOrDefault(key, 0);
                differences.put(key, newValue - oldValue);
                // Check for positive difference => pickup notifier
            }
            for (String key : differences.keySet()) {
                if (differences.get(key) == 0) continue;
                JsonObject data = (JsonObject) new JsonParser().parse(key);
                // TODO; FIX THIS
                /*ItemStack item = new ItemStack(Registry.ITEM.get(new Identifier(data.get("item").getAsString())));
                try {
                    item.setNbt(StringNbtReader.parse(data.get("nbt").toString()));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
                addPickedUpItem(item, differences.get(key));*/
            }
        }
        oldItemMap = itemMap;
    }

    private static String getItemKey(ItemStack stack) {
        return "{" +
                "item:\"" +
                stack.getItem().toString() +
                "\"," +
                "nbt:" +
                stack.getOrCreateNbt().asString() +
                "}";
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
