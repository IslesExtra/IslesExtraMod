package com.isles.skyblockisles.islesextra;

import com.google.gson.*;
import com.isles.skyblockisles.islesextra.client.IslesEventHandler;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.utils.InitUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
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
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IslesExtra implements ModInitializer {

    public static ArrayList<Runnable> tasks = new ArrayList<>(2); // shouldn't expect more than 2 tasks at a time

    public final static String MOD_ID = "islesextra";

    public static DiscordHandler discord;

    // TODO; move
    public static Set<String> emojis = new HashSet<>();

    // TODO; move like 90% of the code in this class to new / different classes

    @Override
    public void onInitialize() {

        IslesEventHandler.init();
        IslesExtraClient.registerClientEvents();

        InitUtils.events();

        registerAttributes();
        registerCustomItems();

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("isles", "emoji_listener");
            }

            @Override
            public void reload(ResourceManager manager) {
                HashMap<String, String> emojiMap = new HashMap<>();
                for (Identifier id : manager.findResources("font", path -> path.getPath().endsWith(".json")).keySet()) {
                    Optional<Resource> resource = manager.getResource(id);
                    if (resource.isEmpty()) continue;
                    try (InputStream stream = resource.get().getInputStream()) {
                        //JsonParser jsonParser = new JsonParser(); // TODO; make sure static method works fine
                        if (stream == null) continue;
                        JsonObject o = (JsonObject) JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        JsonArray providers = o.getAsJsonArray("providers");
                        for (JsonElement e : providers) {
                            // TODO; add some more checks here to prevent errors
                            JsonObject provider = (JsonObject) e;
                            if (!provider.get("type").getAsString().equals("bitmap")) continue;
                            if (!provider.has("file")) continue;
                            String path = provider.get("file").getAsString();
                            if (!path.startsWith("custom/emojis")) continue;
                            String emojiName = path.replace("custom/emojis/", "").replace(".png", "");
                            emojiMap.put(":" + emojiName + ":", provider.getAsJsonArray("chars").get(0).getAsString());
                        }
                    } catch(Exception e) {
                        e.printStackTrace(); // TODO; make this better, perhaps implement a LOGGER
                    }
                }
                emojis.clear();
                for (String emoji : emojiMap.keySet()) {
                    emojis.add(emojiMap.get(emoji) + " " + emoji);
                }
            }
        });
    }

    public static final List<CustomItem> customItems = new ArrayList<>();
    private static FabricItemGroupEntries entries;
    private static void registerCustomItems() {

        RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("isles", "building_blocks"));
        ItemGroup group = FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.DIAMOND)).displayName(Text.of("Skyblock Isles")).build();

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("isles", "custom_block_listener");
            }

            @Override
            public void reload(ResourceManager manager) {
                Optional<Resource> resource = manager.getResource(new Identifier("minecraft:models/item/stone.json"));
                if (resource.isPresent()) {
                    try {
                        JsonObject object = JsonParser.parseReader(new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
                        System.out.println(object);
                        if (object.has("overrides")) {
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
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
        return IslesExtra.customItems.get(index).id();
    }

     public record CustomItem(String id, String display, int model) { }

    // this.setDefaultState(((BlockState)this.stateManager.getDefaultState()).with(INSTRUMENT, Instrument.HARP)).with(NOTE, 0)).with(POWERED, false));

}
