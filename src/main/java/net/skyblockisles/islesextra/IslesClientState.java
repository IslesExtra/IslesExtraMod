package net.skyblockisles.islesextra;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.ActionResult;
import net.skyblockisles.islesextra.callback.JoinedIslesCallback;
import net.skyblockisles.islesextra.callback.LeftIslesCallback;
import net.skyblockisles.islesextra.callback.SwitchedIslesServerCallback;
import net.skyblockisles.islesextra.party.IslesParty;
import net.skyblockisles.islesextra.annotations.Init;

public class IslesClientState {

  private static boolean onIsles = false;
  private static final String SERVER_IDENTIFIER = "isles";
  private static final Logger LOGGER = LogManager.getLogger();

  @Init
  public static void init() {
    JoinedIslesCallback.EVENT.register(() -> {
      onIsles = true;
      LOGGER.info("User joined isles");
      return ActionResult.PASS;
    });

    LeftIslesCallback.EVENT.register(() -> {
      onIsles = false;
      LOGGER.info("User left isles");
      return ActionResult.PASS;
    });

    SwitchedIslesServerCallback.EVENT.register(() -> {
      onIsles = true;
      LOGGER.info("User switched isles servers");
      return ActionResult.PASS;
    });

    ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
      ServerInfo entry = client.getCurrentServerEntry();
      boolean wasOnIsles = IslesClientState.isOnIsles();
      boolean joinedDifferentServer = entry == null || !isIslesAddress(entry.address);
      if ((joinedDifferentServer && wasOnIsles))
        LeftIslesCallback.EVENT.invoker().interact();
      else if (wasOnIsles) {
        SwitchedIslesServerCallback.EVENT.invoker().interact();
      } else {
        JoinedIslesCallback.EVENT.invoker().interact();
      }
    }));

    ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
      if (IslesClientState.isOnIsles()) {
        LeftIslesCallback.EVENT.invoker().interact();
        IslesParty.clearMembers();
      }
    }));
  }
  private static boolean isIslesAddress(String address) {
    return address.contains(SERVER_IDENTIFIER);
  }

  public static boolean isOnIsles() {
    return onIsles;
  }

}
