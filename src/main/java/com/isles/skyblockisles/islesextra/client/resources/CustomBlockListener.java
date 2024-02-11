package com.isles.skyblockisles.islesextra.client.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomBlockListener implements SimpleSynchronousResourceReloadListener {

    private static final List<CustomItem> customItems = new ArrayList<>();
    private static FabricItemGroupEntries entries;

    public record CustomItem(String id, String display, int model) { }


    final RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("isles", "building_blocks"));
    public CustomBlockListener() {
        ItemGroup group = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.DIAMOND)).displayName(Text.of("Skyblock Isles")).build();


        ItemGroupEvents.modifyEntriesEvent(key).register(content -> {
            entries = content;
            for (CustomItem customItem : customItems) {
                ItemStack item = new ItemStack(Items.STONE);
                Text text = Text.of(customItem.display()).copyContentOnly().setStyle(Style.EMPTY.withItalic(false));
                item.setCustomName(text);
                NbtCompound nbt = item.getOrCreateNbt();
                nbt.putString("MYTHIC_TYPE", customItem.id());
                nbt.putInt("CustomModelData", customItem.model());
                item.setNbt(nbt);
                content.add(item);
            }
        });
        Registry.register(Registries.ITEM_GROUP, key, group);

    }

    final Identifier fabricId = new Identifier("isles", "custom_block_listener");
    @Override
    public Identifier getFabricId() {
        return fabricId;
    }

    @Override
    public void reload(ResourceManager manager) {
        Optional<Resource> resource = manager.getResource(new Identifier("minecraft:models/item/stone.json"));
        if (resource.isPresent()) {
            try {
                JsonObject object = JsonParser.parseReader(new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
                if (object.has("overrides")) {
                    // TODO; also add more checks, just like in EmojiListener
                    JsonArray overrides = object.getAsJsonArray("overrides");
                    for (JsonElement element : overrides) {
                        JsonObject customBlock = element.getAsJsonObject();
                        String id = customBlock.get("model").getAsString().replace("block/custom/", "");
                        int modelData = customBlock.getAsJsonObject("predicate").get("custom_model_data").getAsInt();
                        customItems.add(new CustomItem(id, convertToTitleCase(id), modelData));
                    }
                    if (entries != null) ItemGroupEvents.modifyEntriesEvent(key).invoker().modifyEntries(entries);
                }
            }
            catch (IOException ignored) { }
        }
    }

    public static String convertToTitleCase(String input) {
        String[] parts = input.split("_");
        StringBuilder titleCase = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                String firstLetter = part.substring(0, 1).toUpperCase();
                String restLetters = part.substring(1).toLowerCase();
                titleCase.append(firstLetter).append(restLetters).append(" ");
            }
        }

        return titleCase.toString().trim();
    }

    private static final String[] instruments = new String[] {"basedrum", "snare", "hat", "bass", "flute"};
    public static String getId(String instrument, int note) {
        int m = 0;
        for (int i = 0; i<instruments.length; i++) {
            if (instrument.equalsIgnoreCase(instruments[i])) m = i;
        }
        int index = m*24 + note;
        if (m==0) index -= 2;
        return CustomBlockListener.customItems.get(index).id();
    }

}
