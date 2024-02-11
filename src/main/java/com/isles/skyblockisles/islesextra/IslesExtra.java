package com.isles.skyblockisles.islesextra;

import com.isles.skyblockisles.islesextra.client.IslesEventHandler;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.client.resources.CustomBlockListener;
import com.isles.skyblockisles.islesextra.client.resources.EmojiListener;
import com.isles.skyblockisles.islesextra.utils.InitUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;

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
