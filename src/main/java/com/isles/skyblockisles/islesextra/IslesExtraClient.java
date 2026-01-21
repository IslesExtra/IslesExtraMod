package com.isles.skyblockisles.islesextra;

import com.isles.skyblockisles.islesextra.discord.DiscordHandler;
import com.isles.skyblockisles.islesextra.discord.DiscordRPPayload;
import com.isles.skyblockisles.islesextra.resources.EmojiListener;
import com.isles.skyblockisles.islesextra.resources.CustomBlockListener;
import com.isles.skyblockisles.islesextra.chat.ChatPreviewPayload;
import com.isles.skyblockisles.islesextra.constants.IslesKeybindingsManager;
import com.isles.skyblockisles.islesextra.event.handler.EventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
    IslesKeybindingsManager.register();
    EventHandler.registerAll();
    registerResourceReloaders();
    DiscordHandler.start();
  }

  private void registerResourceReloaders() {
    ResourceLoader loader = ResourceLoader.get(ResourceType.CLIENT_RESOURCES);

    loader.registerReloader(Identifier.of(ISLES_ID, "emoji_listener"), new EmojiListener());
    loader.registerReloader(Identifier.of(ISLES_ID, "custom_block_listener"), new CustomBlockListener());
  }

  private void registerPayloads() {
    PayloadTypeRegistry.playS2C().register(DiscordRPPayload.ID, DiscordRPPayload.CODEC);
    PayloadTypeRegistry.playC2S().register(ChatPreviewPayload.ID, ChatPreviewPayload.CODEC);
    PayloadTypeRegistry.playS2C().register(ChatPreviewPayload.ID, ChatPreviewPayload.CODEC);
  }
}
