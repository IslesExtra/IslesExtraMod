package com.isles.skyblockisles.islesextra.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface JoinedIslesCallback extends IslesCallback {

  Event<JoinedIslesCallback> EVENT = EventFactory.createArrayBacked(JoinedIslesCallback.class,
      (listeners) -> () -> {
        for (JoinedIslesCallback listener : listeners) {
          ActionResult result = listener.interact();
          if (result != ActionResult.PASS && result != ActionResult.SUCCESS) {
            return result;
          }
        }
        return ActionResult.PASS;
      });
}
