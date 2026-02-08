package net.skyblockisles.islesextra.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ActionResult;
import net.skyblockisles.islesextra.IslesClientState;
import net.skyblockisles.islesextra.callback.JoinedIslesCallback;
import net.skyblockisles.islesextra.callback.LeftIslesCallback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.skyblockisles.islesextra.annotations.Init;

public class DiscordHandler {

  private static final Logger LOGGER = LogManager.getLogger();

    public static DiscordService Discord;
    public static DiscordRichPresenceFeature discordRichPresenceFeature;

    private static int clientTick = 1;
    private static int discordAppCount = 0;

  private DiscordHandler() { }
  
  @Init
  public static void init() {
      Discord = new DiscordService();
      discordRichPresenceFeature = new DiscordRichPresenceFeature();
      start();
  }

  private static void start() {
      JoinedIslesCallback.EVENT.register(() -> {
          discordRichPresenceFeature.enableRichPresence();
          setRichPresence("In Wharfmolo", "Killing innocent citizens");
          return ActionResult.PASS;
      });
      LeftIslesCallback.EVENT.register(() -> {
          discordRichPresenceFeature.disableRichPresence();
          return ActionResult.PASS;
      });

      ClientLifecycleEvents.CLIENT_STOPPING.register((handler) -> discordRichPresenceFeature.disableRichPresence());

      ClientTickEvents.END_WORLD_TICK.register(clientWorld -> runnableRunner());
  }

  private static void setRichPresence(String... lines) {
    ClientPlayerEntity player = MinecraftClient.getInstance().player;

    Discord.setDetails(lines.length > 0 ? lines[0] : "");
    Discord.setState(lines.length > 1 ? lines[1] : "");
    Discord.setLargeImage("https://cdn.discordapp.com/app-icons/1015667892601241640/5beeb6c9d0196f7d8a45ea8b6123b13b.png");
    Discord.setLargeImageText("play.skyblockisles.com");
    Discord.setSmallImage("https://mc-heads.net/avatar/" + player.getUuidAsString() + "/100");
    Discord.setSmallImageText(player.getName().getString());
  }

    private static void runnableRunner() {
        clientTick++;

        // run discord service tick events
        Discord.onTick();

        if (clientTick > 20) clientTick = 1;
        else if (clientTick == 20) {
            discordAppCount++;
            if (discordAppCount > 5) discordAppCount = 0;
            else if (discordAppCount == 5) {
                if (IslesClientState.isOnIsles()) {
                    if (MinecraftClient.getInstance().player != null) Discord.setDetails(MinecraftClient.getInstance().player.getName().getString());
                }
            }
        }
    }


}
