package com.isles.skyblockisles.islesextra.constants;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public abstract class MessageSender {

  public static void sendMessage(String message) {
    sendMessage(Text.of(message));
  }

  public static void sendMessage(Text message) {
    if (MinecraftClient.getInstance().player == null) {
      return;
    }
    MinecraftClient.getInstance().player.sendMessage(message, false);
  }

  public static void sendTitle(String title, int fade, int stay, int leave) {
    var hud = MinecraftClient.getInstance().inGameHud;
    hud.setTitleTicks(fade, stay, leave);
    hud.setTitle(Text.of(title));
  }

  public static void sendTitle(String title) {
    sendTitle(title, 5, 30, 5);
  }
}
