package net.skyblockisles.islesextra.callback;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.text.Text;
import net.skyblockisles.islesextra.party.IslesParty;
import net.skyblockisles.islesextra.party.screen.PartyManagmentScreen;
import net.skyblockisles.islesextra.annotations.Init;
import net.skyblockisles.islesextra.keybindings.IslesKeybinding;
import net.skyblockisles.islesextra.keybindings.IslesKeybindingsManager;

public class ClientEventHandler {

  @Init
  public static void init() {
    ClientTickEvents.START_CLIENT_TICK.register(client -> {
      if (client.player == null || client.world == null) {
        return;
      }

      if (IslesKeybindingsManager.getBinding(IslesKeybinding.OPEN_PARTY_SCREEN).isPressed()) {
        client.setScreen(new PartyManagmentScreen(Text.translatable("party_managment.islesextra.name")));
      }

      IslesParty.lowHealthWarning();
    });

    ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
      IslesParty.onChat(message.getString());
    });
  }
}
