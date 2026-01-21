package com.isles.skyblockisles.islesextra.constants;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class MessageSender {
  private MessageSender() { }

  public static void sendMessage(String message) {
    sendMessage(Text.of(message));
  }

  public static void sendMessage(Text message) {
    ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (player != null)
      player.sendMessage(message, false);
  }

  public static void sendTitle(String title, int fade, int stay, int leave) {
    InGameHud hud = MinecraftClient.getInstance().inGameHud;
    hud.setTitleTicks(fade, stay, leave);
    hud.setTitle(Text.of(title));
  }

  public static void sendTitle(String title) {
    sendTitle(title, 10, 70, 20);
  }
}
