package com.kyllian.skyblockisles.islesextra;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kyllian.skyblockisles.islesextra.client.CustomText;
import com.kyllian.skyblockisles.islesextra.client.IslesEventHandler;
import com.kyllian.skyblockisles.islesextra.client.PickupLogger;
import com.kyllian.skyblockisles.islesextra.client.discord.DiscordHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IslesExtra implements ModInitializer {

    public static Screen nextScreen = null;

    public final static String MOD_ID = "islesextra";

    public static DiscordHandler discord;

    @Override
    public void onInitialize() {

        IslesEventHandler.init();

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (nextScreen!=null) client.setScreen(nextScreen);
            nextScreen = null;
            if (client.player == null) return;
            PickupLogger.updatePickedUpItems();
        }));
        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {
            NbtCompound nbt = stack.getNbt();
            if (nbt != null) {
                int i = 0;
                while (nbt.contains(IslesExtra.MOD_ID + ".lore." + i)) {
                    lines.add(new CustomText(nbt.getString(IslesExtra.MOD_ID + ".lore." + i)).getValue());
                    i++;
                }
            }
        }));
        registerAttributes();
        registerCustomItems();
    }

    private static void registerAttributes() {
        // TODO; UNCOMMENT
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
