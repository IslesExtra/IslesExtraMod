package com.isles.skyblockisles.islesextra;

import com.isles.skyblockisles.islesextra.client.IslesEventHandler;
import com.isles.skyblockisles.islesextra.client.IslesExtraClient;
import com.isles.skyblockisles.islesextra.client.resources.CustomBlockListener;
import com.isles.skyblockisles.islesextra.client.resources.EmojiListener;
import com.isles.skyblockisles.islesextra.utils.InitUtils;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resource.ResourceType;

import java.util.ArrayList;
import net.minecraft.util.Identifier;

public class IslesExtra implements ModInitializer {

    public static ArrayList<Runnable> tasks = new ArrayList<>(2); // shouldn't expect more than 2 tasks at a time

    public final static String MOD_ID = "islesextra";
    public final static String ISLES_ID = "isles";

    @Override
    public void onInitialize() {

        IslesEventHandler.init();
        IslesExtraClient.registerClientEvents();

        InitUtils.events();

        var registerer = ResourceLoader.get(ResourceType.CLIENT_RESOURCES);
        registerer.registerReloader(Identifier.of(ISLES_ID, "emoji_listener"), new EmojiListener());
        registerer.registerReloader(Identifier.of(ISLES_ID, "building_blocks"), new CustomBlockListener());
    }

}
