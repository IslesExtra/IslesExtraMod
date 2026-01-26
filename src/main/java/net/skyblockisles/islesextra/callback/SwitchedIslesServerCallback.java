package net.skyblockisles.islesextra.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface SwitchedIslesServerCallback extends IslesCallback {

  Event<SwitchedIslesServerCallback> EVENT = EventFactory.createArrayBacked(
      SwitchedIslesServerCallback.class, (listeners) -> () -> {
        for (SwitchedIslesServerCallback listener : listeners) {
          ActionResult result = listener.interact();
          if (result != ActionResult.PASS && result != ActionResult.SUCCESS) {
            return result;
          }
        }
        return ActionResult.PASS;
      });
}
