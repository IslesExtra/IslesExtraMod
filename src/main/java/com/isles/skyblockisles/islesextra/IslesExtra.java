package com.isles.skyblockisles.islesextra;

import com.google.gson.*;
import com.isles.skyblockisles.islesextra.client.CustomText;
import com.isles.skyblockisles.islesextra.client.IslesEventHandler;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.gui.screen.Screen;
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

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (!tasks.isEmpty()) {
                tasks.forEach(Runnable::run);
                tasks.clear();
            }
        }));
        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {
            NbtCompound nbt = stack.getNbt();
            if (nbt == null) return;
            // TODO; figure out wtf this does again
            for (int i = 0; nbt.contains(IslesExtra.MOD_ID + ".lore." + i); i++) {
                lines.add(new CustomText(nbt.getString(IslesExtra.MOD_ID + ".lore." + i)).getValue());
            }
        }));
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

    private static void registerAttributes() {
        // TODO; UNCOMMENT, just kidding lol!
        //FabricDefaultAttributeRegistry.register(IslesEntities.QUEEN_BEE, QueenBeeEntity.createMobAttributes());
    }

    public static final List<CustomItem> customItems = new ArrayList<>();
    private static void registerCustomItems() {
        File file = new File("mods/isles/custom_blocks.json");
        Gson gson = new Gson();
        try {
            JsonObject itemList = gson.fromJson(new FileReader(file), JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : itemList.entrySet()) {
                String id = entry.getKey();
                JsonObject data = entry.getValue().getAsJsonObject();
                int modelData = data.get("Model").getAsInt();
                String name = data.get("Display").getAsString();
                customItems.add(new CustomItem(id, name, modelData));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(file.getAbsolutePath());
        ItemGroup group = FabricItemGroup.builder()
                        .icon(() -> new ItemStack(Items.DIAMOND)).displayName(Text.of("Skyblock Isles")).build();
        RegistryKey<ItemGroup> key = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("isles", "building_blocks"));
        ItemGroupEvents.modifyEntriesEvent(key).register(content -> {
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
