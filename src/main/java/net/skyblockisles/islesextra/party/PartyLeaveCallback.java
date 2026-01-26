package net.skyblockisles.islesextra.party;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PartyLeaveCallback {

  Event<PartyLeaveCallback> EVENT = EventFactory.createArrayBacked(PartyLeaveCallback.class,
      (listeners) -> (playerName, kicked) -> {
        for (PartyLeaveCallback listener : listeners) {
          ActionResult result = listener.interact(playerName, kicked);
          if (result != ActionResult.PASS && result != ActionResult.SUCCESS) {
            return result;
          }
        }
        return ActionResult.PASS;
      });

  ActionResult interact(String playerName, boolean forced);

}
