package net.skyblockisles.islesextra.constants;

import java.util.LinkedList;
import java.util.Queue;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.annotations.Init;

public class MessageScheduler {
  private MessageScheduler() { }
  private static final Queue<Text> titleQueue = new LinkedList<>();
  private static final Queue<Text> messageQueue = new LinkedList<>();

  private static int titleDisplayTicks = 0;
  private static final int TITLE_DURATION = 40;

  @Init
  public static void init() {
    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      if (client.player != null && !messageQueue.isEmpty()) {
        client.player.sendMessage(messageQueue.poll(), false);
      }

      if (titleDisplayTicks > 0) {
        titleDisplayTicks--;
      } else if (client.inGameHud != null && !titleQueue.isEmpty()) {
        client.inGameHud.setTitle(titleQueue.poll());
        titleDisplayTicks = TITLE_DURATION;
      }
    });
  }

  public static void scheduleTitle(Text text) {
    if (!titleQueue.contains(text)) {
      titleQueue.add(text);
    }
  }

  public static void scheduleTitle(String string) {
    Text text = Text.of(string);
    titleQueue.add(text);
  }

  public static void scheduleMessage(Text text) {
    messageQueue.add(text);
  }

  public static void scheduleMessage(String string) {
    Text text = Text.of(string);
    messageQueue.add(text);
  }

}
