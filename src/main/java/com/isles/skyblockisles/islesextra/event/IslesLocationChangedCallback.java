package com.isles.skyblockisles.islesextra.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface IslesLocationChangedCallback {

  Event<IslesLocationChangedCallback> EVENT = EventFactory.createArrayBacked(
      IslesLocationChangedCallback.class, (listeners) -> (loc) -> {
        for (IslesLocationChangedCallback listener : listeners) {
          ActionResult result = listener.interact(loc);
          if (result != ActionResult.PASS && result != ActionResult.SUCCESS) {
            return result;
          }
        }
        return ActionResult.PASS;
      });

  ActionResult interact(String location);

}
