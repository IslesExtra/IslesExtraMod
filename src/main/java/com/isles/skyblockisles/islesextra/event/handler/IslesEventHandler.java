package com.isles.skyblockisles.islesextra.event.handler;

import com.isles.skyblockisles.islesextra.client.IslesClientState;
import com.isles.skyblockisles.islesextra.constants.IslesGui;
import com.isles.skyblockisles.islesextra.event.IslesLocationChangedCallback;
import com.isles.skyblockisles.islesextra.event.JoinedIslesCallback;
import com.isles.skyblockisles.islesextra.event.LeftIslesCallback;
import com.isles.skyblockisles.islesextra.event.OpenedIslesGuiCallback;
import com.isles.skyblockisles.islesextra.event.SwitchedIslesServerCallback;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IslesEventHandler implements EventHandler {

  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void register() {
    JoinedIslesCallback.EVENT.register(() -> {
      IslesClientState.joinedIsles();
      LOGGER.info("User joined isles");
      return ActionResult.PASS;
    });

    LeftIslesCallback.EVENT.register(() -> {
      IslesClientState.leftIsles();
      LOGGER.info("User left isles");
      return ActionResult.PASS;
    });

    SwitchedIslesServerCallback.EVENT.register(() -> {
      IslesClientState.joinedIsles();
      LOGGER.info("User switched isles servers");
      return ActionResult.PASS;
    });

    OpenedIslesGuiCallback.EVENT.register(gui -> {
      if (gui != IslesGui.ESCAPE_MENU) {
        IslesGui.openGui(gui);
      }
      return ActionResult.PASS;
    });

    IslesLocationChangedCallback.EVENT.register( location -> ActionResult.PASS);
  }
}
