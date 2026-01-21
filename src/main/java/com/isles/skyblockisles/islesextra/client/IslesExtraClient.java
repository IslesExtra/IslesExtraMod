package com.isles.skyblockisles.islesextra.client;

import com.isles.skyblockisles.islesextra.client.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.client.discord.DiscordRPPayload;
import com.isles.skyblockisles.islesextra.client.resources.CustomBlockListener;
import com.isles.skyblockisles.islesextra.client.resources.EmojiListener;
import com.isles.skyblockisles.islesextra.client.screen.IslesHudHandler;
import com.isles.skyblockisles.islesextra.constants.ChatPreviewPayload;
import com.isles.skyblockisles.islesextra.constants.IslesKeybindings;
import com.isles.skyblockisles.islesextra.event.handler.ClientEventHandler;
import com.isles.skyblockisles.islesextra.event.handler.ConnectionStateEventHandler;
import com.isles.skyblockisles.islesextra.event.handler.EventHandler;
import com.isles.skyblockisles.islesextra.event.handler.IslesEventHandler;
import com.isles.skyblockisles.islesextra.event.handler.ItemEventHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class IslesExtraClient implements ClientModInitializer {

  public final static String MOD_ID = "islesextra";
  public final static String ISLES_ID = "isles";

  @Override
  public void onInitializeClient() {
    registerPayloads();
    registerKeybinds();
    registerEventHandlers();
    registerResourceReloaders();
    DiscordHandler.start();
  }

  private void registerEventHandlers() {
    var handlers = List.of(
        new ClientEventHandler(),
        new ConnectionStateEventHandler(),
        new IslesEventHandler(),
        new ItemEventHandler(),
        new IslesHudHandler());

    handlers.forEach(EventHandler::register);
  }

  private void registerResourceReloaders() {
    var registerer = ResourceLoader.get(ResourceType.CLIENT_RESOURCES);
    var reloaders = Map.of(
        "emoji_listener", new EmojiListener(),
        "custom_block_listener", new CustomBlockListener());

    reloaders.forEach(
        (id, reloader) -> registerer.registerReloader(Identifier.of(ISLES_ID, id), reloader));
  }

  private void registerPayloads() {
    PayloadTypeRegistry.playS2C().register(DiscordRPPayload.ID, DiscordRPPayload.CODEC);
    PayloadTypeRegistry.playC2S().register(ChatPreviewPayload.ID, ChatPreviewPayload.CODEC);
    PayloadTypeRegistry.playS2C().register(ChatPreviewPayload.ID, ChatPreviewPayload.CODEC);
  }

  private void registerKeybinds() {
    Arrays.stream(IslesKeybindings.values()).forEach(keybind -> KeyBindingHelper.registerKeyBinding(keybind.getBinding()));
  }
}
