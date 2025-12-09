package com.isles.skyblockisles.islesextra.event;

import com.isles.skyblockisles.islesextra.constants.IslesGui;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface OpenedIslesGuiCallback {

  Event<OpenedIslesGuiCallback> EVENT = EventFactory.createArrayBacked(OpenedIslesGuiCallback.class,
      (listeners) -> (gui) -> {
        for (OpenedIslesGuiCallback listener : listeners) {
          ActionResult result = listener.interact(gui);
          if (result != ActionResult.PASS && result != ActionResult.SUCCESS) {
            return result;
          }
        }
        return ActionResult.PASS;
      });

  ActionResult interact(IslesGui gui);
}
