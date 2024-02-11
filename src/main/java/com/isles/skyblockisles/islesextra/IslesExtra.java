package com.isles.skyblockisles.islesextra;

import com.google.gson.*;
import com.isles.skyblockisles.islesextra.client.IslesEventHandler;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.client.resources.CustomBlockListener;
import com.isles.skyblockisles.islesextra.client.resources.EmojiListener;
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

    @Override
    public void onInitialize() {

        IslesEventHandler.init();
        IslesExtraClient.registerClientEvents();

        InitUtils.events();

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new EmojiListener());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new CustomBlockListener());
        
    }

}
