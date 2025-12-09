package com.isles.skyblockisles.islesextra.event.handler;

import com.isles.skyblockisles.islesextra.client.IslesClientState;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.SwitchedIslesServerCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.ServerInfo;

public final class ConnectionStateEventHandler implements EventHandler {

  private static final String SERVER_IDENTIFIER = "isles";

  @Override
  public void register() {
    ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
      ServerInfo entry = client.getCurrentServerEntry();
      var wasOnIsles = IslesClientState.isOnIsles();
      var joinedDifferentServer = entry == null || !isIslesAddress(entry.address);

      var event = (joinedDifferentServer && wasOnIsles) ? LeftIslesCallback.EVENT
          : wasOnIsles ? SwitchedIslesServerCallback.EVENT : JoinedIslesCallback.EVENT;
      event.invoker().interact();
    }));

    ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
      if (IslesClientState.isOnIsles()) {
        LeftIslesCallback.EVENT.invoker().interact();
      }
    }));
  }

  private static boolean isIslesAddress(String address) {
    return address.contains(SERVER_IDENTIFIER);
  }
}
