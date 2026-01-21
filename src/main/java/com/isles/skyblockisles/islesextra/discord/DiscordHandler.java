package com.isles.skyblockisles.islesextra.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import com.isles.skyblockisles.islesextra.constants.MessageSender;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiscordHandler {

  private static final Logger LOGGER = LogManager.getLogger();

  private static final IPCClient discordClient = new IPCClient(1128526559016394874L);
  private static final JsonObject BUTTON = createButton();
  private static final RichPresence.Builder richPresenceBuilder = new RichPresence.Builder().setStartTimestamp(System.currentTimeMillis());

  private static boolean ready = false;
  private static boolean active;

  private DiscordHandler() { }

  public static void start() {
    JoinedIslesCallback.EVENT.register(() -> {
      discordClient.setListener(new IPCListenerImpl());
      enable();
      setRichPresence(true, "In Wharfmolo", "Killing innocent citizens");
      return ActionResult.PASS;
    });

    LeftIslesCallback.EVENT.register(() -> {
      if (active) {
        disable();
      }
      return ActionResult.PASS;
    });

    ClientPlayNetworking.registerGlobalReceiver(DiscordRPPayload.ID, (payload, context) -> {
      String s = payload.text();
      String[] lines = s.split(";");
      if (lines.length == 2) {
        setRichPresence(false, lines[0], lines[1]);
      }
    });
  }

  private static void setRichPresence(boolean reset, String... lines) {
    if (!ready) {
      if (reset) {
        MessageSender.sendMessage("§4Discord not found.");
      }
      return;
    }

    if (MinecraftClient.getInstance().player == null) {
      return;
    }

    final ClientPlayerEntity player = MinecraftClient.getInstance().player;

    /* Buttons */
    JsonObject playerButton = new JsonObject();
    playerButton.addProperty("label", player.getName().getString());
    playerButton.addProperty("url", "https://www.youtube.com/watch?v=lpiB2wMc49g");
    JsonArray buttons = new JsonArray(2);
    buttons.add(BUTTON);
    buttons.add(playerButton);

    richPresenceBuilder
        .setDetails(lines.length > 0 ? lines[0] : "")
        .setState(lines.length > 1 ? lines[1] : "")
        .setLargeImage(
            "https://cdn.discordapp.com/app-icons/1015667892601241640/5beeb6c9d0196f7d8a45ea8b6123b13b.png",
            "play.skyblockisles.com"
        )
        .setSmallImage(
            "https://crafatar.com/renders/head/" + player.getUuidAsString()
                + "?overlay&default=MHF_Steve.png",
            player.getName().getString()
        )
        .setButtons(buttons);
    //.setButtons(new RichPresenceButton[]{BUTTON, new RichPresenceButton("https://www.youtube.com/watch?v=lpiB2wMc49g", player.getName().getString() + " - Stats")});
    if (reset) {
      richPresenceBuilder.setStartTimestamp(System.currentTimeMillis());
    }
    discordClient.sendRichPresence(richPresenceBuilder.build());
    LOGGER.info("RICH PRESENCE SET");
  }

  public static void setReady() {
    ready = true;
  }

  private static void disable() {
    discordClient.close();
    ready = active = false;
  }

  private static void enable() {
    try {
      discordClient.connect();
      active = true;
    } catch (NoDiscordClientException | InterruptedException e) {
      LOGGER.error("Failed to connect to Discord!", e);
    }
  }

  private static JsonObject createButton() {
    var button = new JsonObject();
    button.addProperty("label", "Skyblock Isles");
    button.addProperty("url", "https://skyblockisles.com");
    return button;
  }

}
